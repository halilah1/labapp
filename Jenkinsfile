pipeline {
    agent none
    stages {
        stage('OWASP Dependency-Check') {
            agent any
            steps {
                dependencyCheck additionalArguments: '''
                    --noupdate''',
                odcInstallation: 'OWASP Dependency Check'
                
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }
		
		stage('Warnings') {
			agent any 
			steps{
				git branch: 'main', url: 'https://github.com/halilah/labapp.git'
				sh '/var/jenkins_home/apache-maven-3.9.8/bin/mvn --batch-mode -V -U -e clean verify -Dsurefire.useFile=false -Dmaven.test.failure.ignore'
				sh '/var/jenkins_home/apache-maven-3.9.8/bin/mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd findbugs:findbugs'
			}
			post {
				always {
				junit testResults: '**/target/surefire-reports/TEST-*.xml'

				recordIssues(enabledForFailure: true, tools: [
					mavenConsole(),
					java(),
					javaDoc()
				])
				recordIssues(enabledForFailure: true, tool: checkStyle())
				recordIssues(enabledForFailure: true, tool: spotBugs(pattern: '**/target/findbugsXml.xml'))
				recordIssues(enabledForFailure: true, tool: cpd(pattern: '**/target/cpd.xml'))
				recordIssues(enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml'))
				}
			}
		}

        stage('Integration UI Test') {
            parallel {
                stage('Deploy') {
                    agent any
                    steps {
                        sh 'chmod +x ./jenkins/scripts/deploy.sh'
                        sh './jenkins/scripts/deploy.sh'
                        input message: 'Finished using the web site? (Click "Proceed" to continue)'
                        sh 'chmod +x ./jenkins/scripts/kill.sh'
                        sh './jenkins/scripts/kill.sh'
                    }
                }

                stage('Headless Browser Test') {
                    agent {
                        docker {
                            image 'maven:3-alpine' 
       	                    args '-v /root/.m2:/root/.m2'
                        }
                    }
                    steps {
                        sh 'mvn -B -DskipTests clean package'
                        sh 'mvn test'
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/*.xml'
                        }
                    }
                }
            }
        }

        stage('SonarQube'){
			agent any
			steps{
				git branch: 'main', url: 'https://github.com/halilah/labapp.git'
				script {
                    def scannerHome = tool 'SonarQube'
                    // Print the path of SonarQube scanner
                    sh "echo SonarQube Scanner Path: ${scannerHome}"
                    withSonarQubeEnv('SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=OWASP -Dsonar.sources=. -Dsonar.host.url=http://192.168.136.219:9000 -Dsonar.token=sqp_3812b98a8f16ea1f95a5f9b77486f7650e410d61"
                    }
                }
			}
			post {
				always {
					recordIssues enabledForFailure: true, tool: sonarQube()
				}
			}
		}

        stage('Composer Build and Test') {
            agent {
                docker {
                    image 'composer:latest'
                    args '--entrypoint=""'
                }
            }
            stages {
                stage('Build') {
                    steps {
                        sh 'composer install'
                    }
                }
            }
        }
    }
}

