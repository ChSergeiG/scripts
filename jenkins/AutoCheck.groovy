pipeline {
    agent any
    options {
        timeout(time: 5, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    triggers {
        cron('*/10 4-9 * * 5 \n */5 10-19 * * 5 \n */10 20-23 * * 5')
    }
    environment {
        ZZ_USER_NAME = credentials('Autozzap_login')
        ZZ_USER_PASSWORD = credentials('Autozzap_pass')
        ZZ_USER_FIO = credentials('Autozzap_fio')
        DOCKER_HOST = 'localhost:2375'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/ChSergeiG/autozzap.git'
            }
        }
        stage('Docker start') {
            steps {
                sh 'make -sC docker build'
            }
        }
        stage('Check anything') {
            steps {
                sh './gradlew test'
            }
            post {
                success {
                    script {
                        currentBuild.setKeepLog(true)
                    }
                }
            }
        }
    }
    post {
        always {
            sh 'make -sC docker destroy'
        }
        failure {
            junit 'build/test-results/test/*.xml'
        }
    }
}
