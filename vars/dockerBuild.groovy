def call(String resourcePath, String fullImageName, String buildContext = '.') {
    // read resources/docker/reactjs.Dockerfile out of the shared library
    String dockerfileText = libraryResource(resourcePath)

    // materialise it in the workspace under a fixed name
    writeFile file: 'Dockerfile.ci', text: dockerfileText

    echo "Building ${fullImageName} using ${resourcePath} from the shared library resources"
    sh """
        docker build -t ${fullImageName} -f Dockerfile.ci ${buildContext}
    """
}
