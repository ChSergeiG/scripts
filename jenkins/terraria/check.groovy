pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('PROFIT') {
            steps {
                sh 'ls -la $HOME/terraria/world | grep .wld'
            }
        }
    }
}
