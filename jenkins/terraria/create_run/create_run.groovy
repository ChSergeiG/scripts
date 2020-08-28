pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    parameters {
        choice(name: 'MODE', choices: ['CREATE', 'RUN'])
        choice(name: 'WORLD_SIZE', choices: ['1', '2', '3'], description: '1=Small, 2=Medium, 3=Large')
        string(name: 'WORLD_NAME', description: 'World name')
    }
    environment {
        DOCKER_HOST = 'localhost:2375'
    }
    stages {
        stage('PROFIT') {
            steps {
                script {
                    if (env.MODE == 'CREATE') {
                        sh 'rm docker-compose.yml | true'
                        sh 'cp jenkins/terraria/create_run/docker-compose-create.yml ./docker-compose.yml'
                        sh 'sed -i \"6s/.*/    command: \'-world \\/root\\/.local\\/share\\/Terraria\\/Worlds\\/$WORLD_NAME -autocreate $WORLD_SIZE\'/\" ./docker-compose.yml'
                        sh 'cat ./docker-compose.yml'
                        sh 'docker-compose up'
                    } else {
                        sh 'rm docker-compose.yml | true'
                        sh 'cp jenkins/terraria/create_run/docker-compose-run.yml ./docker-compose.yml'
                        sh 'sed -i \"10s/.*/      - WORLD_FILENAME=$WORLD_NAME.wld/\" ./docker-compose.yml'
                        sh 'cat ./docker-compose.yml'
                        sh 'docker-compose up'
                    }
                }
            }
        }
    }
    post {
        always {
            sh 'docker-compose down'
        }
    }
}
