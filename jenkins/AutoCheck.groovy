pipeline {
    agent any
    options {
        timeout(time: 5, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    triggers {
        cron('*/10 * * * 5')
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
        }
    }
    post {
        always {
            sh 'make -sC docker destroy'
            junit 'build/test-results/test/*.xml'
        }
    }
}
