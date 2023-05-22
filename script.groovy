def build() {
    echo "building the application..."
    sh 'npm install' 
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t mojoe277/nodejs-app:njr-1.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push mojoe277/nodejs-app:njr-1.0'
    }
} 

def deployApp() {
    def dockerCmd = 'docker run -p 3080:3080 -d mojoe277/nodejs-app:njr-1.0 '
   sshagent(['ec2-server-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@52.58.160.9 ${dockerCmd}"
    }
}
return this
