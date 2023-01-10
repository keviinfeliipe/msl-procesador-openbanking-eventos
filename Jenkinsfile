def IMAGEN
def APP_VERSION
def OLD_TAG_REGISTRY
def NEW_TAG_REGISTRY
def jenkinsWorker = 'jenkins-worker'
def nodeLabel = 'jenkins-job'
pipeline {
  agent {
    kubernetes {
      cloud 'openshift'
      label nodeLabel
      yaml """
apiVersion: v1
kind: Pod
metadata:
  namespace: dinersclub-devops
  labels:
    identifier: ${nodeLabel}
spec:
  serviceAccountName: jenkins
  containers:
  - name: tools
    image: 982781317332.dkr.ecr.us-east-1.amazonaws.com/apiservice:1.0.7 # Clients: aws oc klar
    command:
    - cat
    tty: true
"""
    }
  }
    environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
        AWS_REGION = "us-east-1"
        AWS_DEFAULT_REGION = "us-east-1"
        AWS_DEFAULT_OUTPUT = "json"
        APP_NAME = ""
        APP_VERSION = ""
        REGISTRY = "982781317332.dkr.ecr.us-east-1.amazonaws.com"
        REPOSITORY = "msl-procesador-openbanking-eventos"
        PUSH = "${REGISTRY}/${REPOSITORY}"
        NAMESPACE = "dinersclub-migracion-dev"
    }
    options {
        timestamps ()
        timeout(time: 60, unit: 'MINUTES')
    }
    stages {
        stage('Stage: Versioning') {
            agent any
            steps {
                script {
                    def artifact = readMavenPom().getArtifactId()
                    IMAGEN = readMavenPom().getArtifactId()
                    echo "Nombre de la imagen de Docker: ${IMAGEN}"

                    echo "Nombre del Repositorio ECR: ${REPOSITORY}"

                    PUSH = "${REGISTRY}/${REPOSITORY}"
                    echo "Push del Repositorio ECR: ${PUSH}"

                    APP_NAME = readMavenPom().getArtifactId()
                    echo "Nombre del Artefacto Openshift: ${APP_NAME}"

                    APP_VERSION = readMavenPom().getVersion()
                    echo "Version actual: ${APP_VERSION}"
                }
            }
        }
        stage('Stage: Environment') {
            agent any
            steps {
                script {
                    def branch = "${env.BRANCH_NAME}"
                    echo " --> Rama: ${branch}"
// todo: rename BRANCH_NAME and NAMESPACE after update projects in openshift
                    switch(branch) {
                    case 'develop':
                        AMBIENTE  = 'dev'
                        NAMESPACE = 'dinersclub-migracion-dev'
                        break
                    case 'calidad':
                        AMBIENTE  = 'cal'
                        NAMESPACE = 'dinersclub-migracion-cal'
                        break
                    case 'gsf':
                        AMBIENTE  = 'gsf'
                        NAMESPACE = 'dinersclub-migracion-gsf'
                        break
                    case 'prod':
                        AMBIENTE  = 'prod'
                        break
                    default:
                        println("Branch value error: " + branch)
                        currentBuild.getRawBuild().getExecutor().interrupt(Result.FAILURE)
                    }

                    echo " --> Ambiente: ${AMBIENTE}"
                }
            }
        }
        stage('Stage: Build'){
            agent {
                label "${jenkinsWorker}"
            }
            when {
                not {
            	    anyOf {
            		     branch 'main'
            		     branch 'master'
                         branch 'feature/*'
                         branch 'fix/*'
                         branch 'hotfix/*'
            		}
                }
            }
            steps {
                script {

                    echo "Maven build..."
                    sh 'rm -rf infrastructure/src/main/resources/META-INF/microprofile-config.properties'
                    sh 'cp infrastructure/src/main/resources/META-INF/microprofile-config-test.properties infrastructure/src/main/resources/META-INF/microprofile-config.properties'
                    sh 'mvn clean package -U -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true'

                }
            }
        }
        stage('Stage: Test'){
            agent {
                label "${jenkinsWorker}"
            }
            when {
                not {
            	    anyOf {
            		     branch 'main'
            		     branch 'master'
                         branch 'feature/*'
                         branch 'fix/*'
                         branch 'hotfix/*'
            		}
            	}
            }
            stages {
                stage("Unit Test") {
                    steps {
                        script {
                            sh 'mvn test'
                        }
                    }
                }
                stage('Kiuwan Test'){
                    steps {
                        script {
                            echo " --> Kiuwan Scan"
		                    kiuwan connectionProfileUuid: 'JnN2-8Fbg',
		                    sourcePath: "${WORKSPACE}",
		                    applicationName: "${APP_NAME}",
		                    failureThreshold: 40.0,
		                    unstableThreshold: 90.0

		                    def kiuwanOutput = readJSON file: "${WORKSPACE}/kiuwan/output.json"
							def secRating = kiuwanOutput.Security.Rating

							echo "Rating : ${secRating}"
                        }
                        archiveArtifacts artifacts: "kiuwan/output.json"
                    }
                }
            }
        }
        stage('Stage: Package'){
            when {
		       not {
		          anyOf {
		            branch 'main'
		            branch 'master'
                    branch 'feature/*'
                    branch 'fix/*'
                    branch 'hotfix/*'
		          }
		       }
		    }
            stages {
		        stage('ECR Token') {
			        steps {
			            container('tools') {
			                script {

			                    env.LOGIN_DOCKER = sh(script:"aws ecr get-login-password", returnStdout: true).trim()

			                }
			            }
			        }
		        }
		        stage('ECR Push') {
		            agent {
		                label "${jenkinsWorker}"
		            }
		            steps {
		                script {

		                	def branch = "${env.BRANCH_NAME}"

                            def values = APP_VERSION.split('-')
                            APP_VERSION = "${values[0]}-${BUILD_NUMBER}-${AMBIENTE}"
                            echo "Version : ${APP_VERSION}"

		                	sh label: "",
                            script: """
                                #!/bin/bash
                                cat <<EOF > openshift/dockerpass
$env.LOGIN_DOCKER
EOF
                             """

		                    echo "Maven build..."
		                    sh "rm -rf infrastructure/src/main/resources/META-INF/microprofile-config.properties"
		                    sh "cp infrastructure/src/main/resources/META-INF/microprofile-config-dev.properties infrastructure/src/main/resources/META-INF/microprofile-config.properties"

                            if (branch == "develop"){
                                sh "mvn clean package -U -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true"
                                echo "Docker Build..."
                                sh "cd application && docker build -f src/main/docker/Dockerfile.jvm -t ${IMAGEN}:${APP_VERSION} ."
                            }else{
                                sh 'mvn clean package -U -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Pnative -Dquarkus.native.additional-build-args="--allow-incomplete-classpath" -Dquarkus.native.native-image-xmx=10g'
                                echo "Docker Build..."
                                sh "cd application && docker build -f src/main/docker/Dockerfile.native -t ${IMAGEN}:${APP_VERSION} ."
                            }

		                    echo "Docker Tag..."
		                    sh "docker tag ${IMAGEN}:${APP_VERSION} ${PUSH}:${APP_VERSION}"

		                    echo "Docker Push..."
							sh "cat openshift/dockerpass | docker login --username AWS --password-stdin https://${REGISTRY}"
		                    sh "docker push ${PUSH}:${APP_VERSION}"

		                }
		            }
		        }
			}
		}

        stage('Stage: Deployment') {
            when {
		       not {
		          anyOf {
		            branch 'main'
		            branch 'master'
                    branch 'feature/*'
                    branch 'fix/*'
                    branch 'hotfix/*'
                    branch 'prod'
		          }
		       }
		    }
            steps {
                container('tools') {
                    script {
                        openshift.withCluster() {
                            openshift.withProject("${NAMESPACE}") {

	                            env.LOGIN_DOCKER = sh(script:"aws ecr get-login-password", returnStdout: true).trim()

	                            sh label: "",
                                script: """
                                    #!/bin/bash
                                	cat <<EOF > openshift/.dockercfg
{"$REGISTRY":{"username":"AWS","password":"$env.LOGIN_DOCKER"}}
EOF
                                """

                                def branch = "${env.BRANCH_NAME}"

								echo "Version a desplegar: ${APP_VERSION} en la rama ${env.BRANCH_NAME}"
                                // se cambia el proyecto para trabajar sobre el correcto
                                sh "oc whoami"
                                sh "oc project ${NAMESPACE}"

	                            if (openshift.selector("secrets", "ecr-registry").exists()){
                            		sh "oc set data secret/ecr-registry --from-file=.dockercfg=openshift/.dockercfg"
                            	}else{
	                            	sh "oc create secret generic ecr-registry --from-file=.dockercfg=openshift/.dockercfg --type=kubernetes.io/dockercfg"
	                            	sh "oc secrets link default ecr-registry --for=pull"
	                            }

                                // Validando
                                if (!openshift.selector("dc", "${APP_NAME}-${AMBIENTE}").exists()){

                                    // DeploymemtConfig
                                    echo " --> Deploy..."
                                    def app = openshift.newApp("--file=./openshift/template.yaml", "--param=APP_NAME=${APP_NAME}-${AMBIENTE}", "--param=APP_VERSION=${APP_VERSION}", "--param=AMBIENTE=${AMBIENTE}", "--param=REGISTRY=${PUSH}:${APP_VERSION}" )

                                    def dc = openshift.selector("dc", "${APP_NAME}-${AMBIENTE}")
                                    while (dc.object().spec.replicas != dc.object().status.availableReplicas) {
                                        sleep 10
                                    }
                                    echo " --> Desployed $APP_NAME!"
                                }
                                else {
                                    echo " --> Ya existe el Deployment $APP_NAME-${AMBIENTE}!"
                                    echo " --> Updating Deployment..."
                                    sh "oc process -f ./openshift/template.yaml -p APP_NAME=${APP_NAME}-${AMBIENTE} -p APP_VERSION=${APP_VERSION} -p AMBIENTE=${AMBIENTE} -p REGISTRY=${PUSH}:${APP_VERSION} | oc apply -f -"
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Stage: Deployment Test') {
            when {
		       not {
		          anyOf {
		            branch 'main'
		            branch 'master'
                    branch 'feature/*'
                    branch 'fix/*'
                    branch 'hotfix/*'
                    branch 'prod'
		          }
		       }
		    }
            steps {
                container('tools') {
                    script {
                        openshift.withCluster() {
                            openshift.withProject("${NAMESPACE}"){
                                // Validando el Deployment
                                echo " --> Validando el status del Deployment"
                                if (openshift.selector("dc", "${APP_NAME}-${AMBIENTE}").exists()){
                                    def latestDeploymentVersion = openshift.selector('dc',"${APP_NAME}-${AMBIENTE}").object().status.latestVersion
                                    sh "echo ${latestDeploymentVersion}"
                                    def rc = openshift.selector('rc', "${APP_NAME}-${AMBIENTE}-${latestDeploymentVersion}")
                                    rc.untilEach(1){
                                        def rcMap = it.object()
                                        return (rcMap.status.replicas.equals(rcMap.status.readyReplicas))
                                    }

                                    def dc = openshift.selector('dc', "${APP_NAME}-${AMBIENTE}")
                                    def status = dc.rollout().status()

                                    // Validando el Service
                                    sh "echo Validando el Service"
                                    def connected = openshift.verifyService("${APP_NAME}-${AMBIENTE}")
                                    if (connected) {
                                        echo "Able to connect to ${APP_NAME}-${AMBIENTE}"
                                    } else {
                                        echo "Unable to connect to ${APP_NAME}-${AMBIENTE}"
                                        exit 1
                                    }
                                }
                                else {
                                    echo " --> No existe el Deployment $APP_NAME-${AMBIENTE}!"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            echo " ==> SUCCES: Pipeline successful."
        }
        failure {
            echo " ==> ERROR: Pipeline failed."
        }
        always {
            node("${jenkinsWorker}") {
                // Clean Up
                script {
                    echo " ==> Cleanup..."

                    sh label: "",
                    script: """
                        #!/bin/bash
                        set -e
                        REMOVEIMAGES_NONE=\$(docker images | grep none | awk '{print \$3}')
                        if [ "\$REMOVEIMAGES_NONE" != "" ]; then
                        echo " --> Remove Images none..."
                        nohup docker rmi -f \$REMOVEIMAGES_NONE &
                        fi
                        REMOVEIMAGES_OLD=\$(docker images | grep ' [minutes|hours|days|months|weeks]* ago' | awk '{print \$3}')
                        if [ "\$REMOVEIMAGES_OLD" != "" ]; then
                        echo " --> Remove Images old..."
                        nohup docker rmi -f \$REMOVEIMAGES_OLD &
                        fi
                    """

                }
                
            }
        }
    }
}

def rollback(){
    echo " --> Rollback..."

    REVISION = sh (script: "oc rollout history dc ${APP_NAME}-${AMBIENTE} | grep Complete | awk '{print \$1}' | tail -1 | awk '{print \$0-1}'", returnStdout:true).trim()

    echo " --> Revision: ${REVISION}"
    rollback = openshift.selector("dc/${APP_NAME}-${AMBIENTE}").rollout().undo("--to-revision=${REVISION}")
    // def result = rollback.history()

    def dc = openshift.selector('dc', "${APP_NAME}-${AMBIENTE}")
    // this will wait until the desired replicas are available
    dc.rollout().status()
}
