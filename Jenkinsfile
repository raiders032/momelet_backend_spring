node{
    environment {
        registry = "neptunes032/momelet_spring"
        registryCredential = 'docker-hub'
    }

    stage('SCM Checkout'){
        checkout scm
    }

    stage('Build & Test'){
        sh './gradlew build -x test'
    }

    stage ('Build Docker Image'){
        steps {
            script {
                dockerImage = docker.build registry + ":$BUILD_NUMBER"
            }
        }
    }

    stage ('Push Dokcer Image'){
        steps{
            script {
                docker.withRegistry( '', registryCredential ) {
                dockerImage.push()
             }
           }
        }
    }

    stage('Remove Unused docker image'){
        steps{
              sh "docker rmi $registry:$BUILD_NUMBER"
         }
    }
}