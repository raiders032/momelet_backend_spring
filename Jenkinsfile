pipeline {
  environment {
    registry = "neptunes032/momelet_spring"
    registryCredential = 'docker-hub'
    dockerImage = ''
  }
  agent any
  stages {
    stage('Cloning Git') {
        steps{
            script {
                checkout scm
            }
        }
    }
    stage('Build & Test'){
       steps{
            script {
                sh './gradlew build -x test'
            }
        }
    }
    stage('Building image') {
      steps{
        script {
          dockerImage = docker.build registry + ":latest"
        }
      }
    }
    stage('Deploy Image') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            dockerImage.push()
          }
        }
      }
    }
    stage('Remove Unused docker image') {
      steps{
        sh "docker rmi $registry:latest"
      }
    }

    stage('make zip file & upload to AWS S3') {
      steps{
            sh 'mkdir -p before-deploy'
            sh 'cp scripts/*.sh before-deploy/'
            sh 'cp appspec.yml before-deploy/'
            sh 'cd before-deploy && zip -r before-deploy *'
            sh 'cd ../'
            sh 'mkdir -p deploy'
            sh 'mv before-deploy/before-deploy.zip deploy/momelet_spring.zip'
      }
    }

    stage('upload to AWS S3') {
      steps{
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'AWS_CREDENTIALS', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            sh 'aws s3 cp deploy/momelet_spring.zip s3://momelet-deploy/momelet_spring.zip --region ap-northeast-2'
        }
      }
    }

    stage('deploy') {
          steps{
            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'AWS_CREDENTIALS', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
               sh 'aws deploy create-deployment \
                              --application-name momelet-deploy-app \
                              --deployment-group-name momelet-spring \
                              --region ap-northeast-2 \
                              --s3-location bucket=momelet-deploy,bundleType=zip,key=momelet_spring.zip'
            }
          }
        }

  }
}