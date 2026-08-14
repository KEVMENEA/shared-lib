def call(String fullImageName, String containerName, String portMapping) {
    sh """
        # stop & remove any previous container with this name (ignore error if none)
        docker rm -f ${containerName} || true

        # run the new image in the background, mapping the port
        docker run -d --name ${containerName} -p ${portMapping} ${fullImageName}

        docker ps --filter "name=${containerName}"
    """
}
