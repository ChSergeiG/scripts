pipeline {

    agent any

    parameters {
        booleanParam(name: 'SEND_PRIVATE', defaultValue: true, description: 'Send private message after build')
        booleanParam(name: 'SEND_COMMON', defaultValue: true, description: 'Send common message after build')
        choice(name: 'SUCCESS', choices: ['true', 'false'], description: 'Do we need fail build?')
    }

    stages {

        stage('Success') {
            when {
                expression { params.SUCCESS == 'true' }
            }
            steps {

                wrap([$class: 'BuildUser']) {
                    script {
                        def USER_ID = slackUserIdFromEmail(env.BUILD_USER_EMAIL)

                        echo "env.BUILD_USER_EMAIL ${env.BUILD_USER_EMAIL}"
                        echo "USER_ID ${USER_ID}"
                        echo "env.BUILD_USER ${env.BUILD_USER}"

                        if (params.SEND_COMMON) {
                            echo 'sending common about success'
                            slackSend channel: '#jenkins-successes',
                                    color: 'good',
                                    message: "SUCCESS: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} started by ${env.BUILD_USER}\n Link: ${env.BUILD_URL}"
                        }
                        if (params.SEND_PRIVATE) {
                            echo 'sending private about success'
                            slackSend color: 'good',
                                    message: "<@$USER_ID> SUCCESS: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} started by ${env.BUILD_USER}\n Link: ${env.BUILD_URL}"
                        }
                        echo 'Success???'
                    }
                }
            }

        }

        stage('Fail') {
            when {
                expression { params.SUCCESS == 'false' }
            }
            steps {
                wrap([$class: 'BuildUser']) {
                    script {
                        def USER_ID = slackUserIdFromEmail(env.BUILD_USER_EMAIL)
                        if (params.SEND_COMMON) {
                            echo 'sending common about not success'
                            slackSend channel: '#jenkins-fails',
                                    color: 'danger',
                                    message: "FAIL: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} started by ${env.BUILD_USER}\n Link: ${env.BUILD_URL}"
                        }
                        if (params.SEND_PRIVATE) {
                            echo 'sending private about not success'
                            slackSend color: 'danger',
                                    message: "<@$USER_ID> FAIL: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} started by ${env.BUILD_USER}\n Link: ${env.BUILD_URL}"
                        }
                    }
                    echo 'FAIL!!!'
                    error 'Test failed'
                }
            }
        }
    }

}
