def call(String projectKey, String projectName, String projectVersion) {

    def scannerHome = tool 'sonar-scanner'

    withSonarQubeEnv('sonar-scanner') {

        sh """
            ${scannerHome}/bin/sonar-scanner \
              -Dsonar.projectKey=${projectKey} \
              -Dsonar.projectName=${projectName} \
              -Dsonar.projectVersion=${projectVersion}
        """
    }
}