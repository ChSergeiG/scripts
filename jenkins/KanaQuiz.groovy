pipeline {

    agent any

    environment {
        PUBLIC_PATH = '/kana/'
    }
    stages {

        stage('Build') {
            steps {
                git 'https://github.com/abliarsar/kana-quiz.git'
                sh 'npm install'
                sh 'npm run build'
            }
        }
    }

    post {
        success {
            sh "rm -rf /var/www/html/kana/*"
            sh "mv $WORKSPACE/dist/* /var/www/html/kana"
            //sh "nginx -s reload"
        }
    }

}
