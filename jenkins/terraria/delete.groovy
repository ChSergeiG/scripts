pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    parameters {
        string(name: 'WORLD_NAME', description: 'World name to delete')
    }
    stages {
        stage('PROFIT') {
            steps {
                sh 'sudo rm $HOME/terraria/world/$WORLD_NAME*'
            }
        }
    }
}
