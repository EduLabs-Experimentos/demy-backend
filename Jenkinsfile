pipeline {
    agent any

    environment {
        AZURE_SCM_URL = 'demy-app-backend-eygre7eda5g3hkfh.scm.southeastasia-01.azurewebsites.net'
        JAR_FILE      = 'target\\platform-2.3.0.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvnw.cmd -B -DskipTests clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvnw.cmd -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvnw.cmd -B package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', excludes: 'target/*.jar.original', fingerprint: true
            }
        }

        stage('Deploy to Azure') {
            when {
                expression { env.GIT_BRANCH == 'origin/main' }
            }
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'azure-publish-profile', usernameVariable: 'AZURE_PUBLISH_USER', passwordVariable: 'AZURE_PUBLISH_PWD')
                ]) {
                    bat 'curl -X POST -u %AZURE_PUBLISH_USER%:%AZURE_PUBLISH_PWD% "https://%AZURE_SCM_URL%/api/publish?type=jar" --data-binary @%JAR_FILE% -H "Content-Type: application/octet-stream"'
                }
            }
        }
    }
}
