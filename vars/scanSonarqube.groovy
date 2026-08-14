def call(String projectKey, String projectName, String projectVersion) {

    withSonarQubeEnv('SonarQube') {

        sh """
            sonar-scanner \
              -Dsonar.projectKey=${projectKey} \
              -Dsonar.projectName=${projectName} \
              -Dsonar.projectVersion=${projectVersion}
        """
    }
}