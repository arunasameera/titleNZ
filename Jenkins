pipeline {
    agent any

    options {
        ansiColor('xterm')
    }    

    environment {        
        DB_USERNAME = "dbuser"
        DB_PASSWORD = "dbpassword"
        DB_CONNECTION_STRING = "jdbc:oracle:oci:@myhost:1521:orcl"
    }

    stages {
        stage('Stage 1') {
            steps {
            	script {            		
                	sh """#!/bin/bash -xe
						docker build -t web-app/devops:latest -t 725496895483.dkr.ecr.ap-southeast-2.amazonaws.com/web-app/devops:latest master/
                	"""	                
            	}
            }
        }
        stage('Stage 2'){                    	
            steps {
                script {
                	sh """#!/bin/bash -xe
                		docker run -e DB_CONNECTION_STRING=${env.DB_CONNECTION_STRING} -e DB_USERNAME=${env.DB_USERNAME} -e DB_PASSWORD=${env.DB_PASSWORD} web-app/devops:latest
					"""    
                }
            }            
        }        
    }
    post {        
        always {
            cleanWs()
        }
    }
}
