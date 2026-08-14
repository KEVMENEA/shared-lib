@Library('shard-lib') _

pipeline {
    agent any

    environment {
        TAG       = "v1.0.${env.BUILD_NUMBER}"
        IMG_NAME  = "jenkins-g12-reactjs"
        DH_USER   = "meneakev"
        FULL_IMG  = "${DH_USER}/${IMG_NAME}:${TAG}"
        CHAT_ID = "MY_CHAT_ID"
        TOKEN = "MY_TOKEN"
        CONTAINER = "reactjs-app"
        PORTS     = "8081:80"          
    }

    stages {

        stage("Checkout") {
            steps {
                git branch: 'master', url: 'https://github.com/keoKAY/reactjs-devop8-template'
            }
        }

        stage("Scan with SonarQube") {
            steps {
                script {
                    // function from the shared library (vars/scanSonarqube.groovy)
                    scanSonarqube("REACT-DEMO", "react-demo", "1.0.0")
                }
            }
        }

        stage("Wait for Quality Gate") {
            steps {
                script {
                    def qg = waitForQualityGate()    
                    env.QG_STATUS = qg.status
                    if (qg.status != 'OK') {
                        error("Quality Gate failed: ${qg.status}")
                    }
                    echo "Quality Gate passed."
                }
            }
        }

        stage("Build") {
            steps {
                script {
                    // Dockerfile comes from the library's resources/docker/ folder.
                    dockerBuild('docker/reactjs.Dockerfile', "${FULL_IMG}")
                }
            }
        }

        stage("Push") {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'DH_CREDIT',
                        usernameVariable: 'USERNAME',
                        passwordVariable: 'PASSWORD')]) {
                    // single-quoted sh: shell reads the env vars, Groovy never sees the secret.
                    // FULL_IMG is exported from environment{} so the shell can use $FULL_IMG too.
                    sh '''
                        echo "$PASSWORD" | docker login -u "$USERNAME" --password-stdin
                        docker push "$FULL_IMG"
                    '''
                }
            }
        }

        stage("Deploy") {
            steps {
                script {
                    // run the image as a container on the Jenkins host
                    deployContainer("${FULL_IMG}", "${CONTAINER}", "${PORTS}")
                }
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'TOKEN'),
                             string(credentialsId: 'TG_CHAT',  variable: 'CHAT_ID')]) {
                sendTelegram("✅ *${env.JOB_NAME}* build #${env.BUILD_NUMBER} SUCCESS\nImage: ${env.FULL_IMG}", TOKEN, CHAT_ID)
            }
        }
        failure {
            withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'TOKEN'),
                             string(credentialsId: 'TG_CHAT',  variable: 'CHAT_ID')]) {
                sendTelegram("❌ *${env.JOB_NAME}* build #${env.BUILD_NUMBER} FAILED\nQuality Gate: ${env.QG_STATUS ?: 'UNKNOWN'}", TOKEN, CHAT_ID)
            }
        }
        always {
            echo "Finished. Quality Gate: ${env.QG_STATUS ?: 'UNKNOWN'}"
        }
    }
}
