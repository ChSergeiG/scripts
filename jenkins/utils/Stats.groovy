pipeline {

    agent any

    stages {
        stage('top') {
            steps {
                echo "Do top-top"
                sh "TERM=xterm top -n 1 -b || true"
            }
        }

        stage('df') {
            steps {
                echo "Do df -h"
                sh "TERM=xterm df -h || true"
            }
        }

        stage('iptstate') {
            steps {
                echo "Do iptstate"
                sh "TERM=xterm iptstate -1 || true"
            }
        }
    }

}
