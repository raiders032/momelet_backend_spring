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
          dockerImage = docker.build registry + ":$BUILD_NUMBER"
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
        sh "docker rmi $registry:$BUILD_NUMBER"
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
        withAWS(credentials:"$AWS_CREDENTIALS") {
            sh 'aws s3 cp deploy/momelet_spring.zip s3://momelet-deploy/momelet_spring.zip --region ap-northeast-2'
        }
      }
    }

  }
}