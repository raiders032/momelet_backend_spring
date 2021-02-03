node{
    stage('SCM Checkout'){
        checkout scm
    }

    stage('Build & Test'){
        sh './gradlew build -x test'
    }

    stage ('Build Docker Image'){
        //
    }

    stage ('Push Dokcer Image'){
      //
    }

    stage('Deploy'){
      //
    }
}