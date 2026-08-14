def call(String projectName, String projectKey, String projectVersion) {
    stage("Scan with Sonarqube" ) {
        environment{
            scannerHome = tool 'sonar-scanner'
        }

        steps{
            withSonarQubeEnv(credentialId: 'SONAR-TOKEN', installationName: 'sonar-scanner') {
                script{
                    sh """
                        ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=${projectKey} \
                        -Dsonar.projectName=${projectName} \
                        -Dsonar.projectVersion=${projectVersion} \

                    """
                }
            }
        }
    }

}