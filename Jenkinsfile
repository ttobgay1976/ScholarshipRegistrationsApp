pipeline {
    agent {
        label 'base'
    }

    environment {
        HARBOR_LOGIN_URL        = 'dev-harbor.systems.gov.bt'
        HARBOR_PROJECT          = 'spms'
        IMAGE_NAME              = 'sprms_regis'
        FULL_IMAGE              = "${HARBOR_LOGIN_URL}/${HARBOR_PROJECT}/${IMAGE_NAME}"

        NAMESPACE               = 'spms'
        HELM_RELEASE            = 'sprms-regis'
        HELM_RELEASE_DEPLOYMENT = 'sprms-regis-deployment'

        HELM_REPO_URL           = 'https://github.com/ravi4410-dev/helmcharts.git'
        HELM_CHART_PATH         = 'helm-charts/sprms-regis'

        KUBECONFIG_CRED         = 'yangkhor-kubeconfig'
        HARBOR_CRED             = 'harbor-creds'
        GIT_CRED                = 'ravi-github-creds'
        VPN_CRED                = 'vpn-creds'

        REPORT_DIR              = 'reports'

        // SPRMS Regis NodePort
        TARGET_URL              = 'http://172.30.3.10:31128'
    }

    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['dev', 'prod'],
            description: 'Target deployment environment'
        )
    }

    stages {
        stage('Checkout App Repo') {
            steps {
                checkout scm
            }
        }

        stage('Checkout Helm Repo') {
            steps {
                dir('helm-charts') {
                    deleteDir()

                    git branch: 'main',
                        credentialsId: env.GIT_CRED,
                        url: env.HELM_REPO_URL
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    set -e

                    echo "=========================================="
                    echo "Building Docker image"
                    echo "=========================================="

                    docker build \
                        --pull \
                        -t "${FULL_IMAGE}:${BUILD_NUMBER}" \
                        -t "${FULL_IMAGE}:${DEPLOY_ENV}" \
                        .
                '''
            }
        }

        stage('Connect VPN') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: env.VPN_CRED,
                        usernameVariable: 'VPN_USERNAME',
                        passwordVariable: 'VPN_PASSWORD'
                    )
                ]) {
                    sh '''
                        set -e

                        echo "=========================================="
                        echo "Connecting VPN"
                        echo "=========================================="

                        mkdir -p "${REPORT_DIR}"
                        rm -f vpn.pid

                        export JENKINS_NODE_COOKIE=dontKillMe

                        printf '%s\n' "$VPN_PASSWORD" | sudo -n openconnect \
                            --protocol=anyconnect \
                            --authgroup=private-consultant-vpn \
                            --user="$VPN_USERNAME" \
                            --passwd-on-stdin \
                            --servercert='pin-sha256:8tYR3p2X2PQwMT712p8IwLTcKF/Nm3Sq2dx+iO8ucxo=' \
                            secure.neyduetewa.gov.bt \
                            > "${REPORT_DIR}/vpn.log" 2>&1 &

                        VPN_PID=$!
                        echo "$VPN_PID" > vpn.pid

                        echo "VPN PID: $VPN_PID"

                        sleep 10

                        if ! ps -p "$VPN_PID" > /dev/null 2>&1; then
                            echo "OpenConnect stopped unexpectedly."
                            echo "===== VPN LOG ====="
                            cat "${REPORT_DIR}/vpn.log" || true
                            exit 1
                        fi

                        echo "OpenConnect process is running."

                        for i in $(seq 1 12); do
                            echo "Checking VPN route, attempt $i/12"

                            ip route get 172.30.3.10 \
                                > "${REPORT_DIR}/vpn-route-check.txt" 2>&1 || true

                            if curl \
                                --silent \
                                --show-error \
                                --output "${REPORT_DIR}/vpn-target-response.txt" \
                                --write-out "%{http_code}" \
                                --connect-timeout 5 \
                                --max-time 10 \
                                "${TARGET_URL}" \
                                > "${REPORT_DIR}/vpn-http-code.txt" 2>&1; then

                                HTTP_CODE=$(cat "${REPORT_DIR}/vpn-http-code.txt")

                                echo "Target is reachable through VPN."
                                echo "HTTP status: $HTTP_CODE"

                                cat "${REPORT_DIR}/vpn-route-check.txt" || true
                                exit 0
                            fi

                            echo "Target is not reachable yet."
                            cat "${REPORT_DIR}/vpn-route-check.txt" || true
                            cat "${REPORT_DIR}/vpn-http-code.txt" || true

                            sleep 5
                        done

                        echo "Target is not reachable through VPN: ${TARGET_URL}"

                        echo "===== VPN PROCESS ====="
                        ps -fp "$VPN_PID" || true

                        echo "===== VPN LOG ====="
                        cat "${REPORT_DIR}/vpn.log" || true

                        echo "===== ROUTING TABLE ====="
                        ip route || true

                        echo "===== TARGET ROUTE ====="
                        ip route get 172.30.3.10 || true

                        echo "===== TARGET CHECK ====="
                        curl -v \
                            --connect-timeout 5 \
                            --max-time 10 \
                            "${TARGET_URL}" || true

                        exit 1
                    '''
                }
            }
        }

        stage('Push to Harbor') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: env.HARBOR_CRED,
                        usernameVariable: 'HARBOR_USER',
                        passwordVariable: 'HARBOR_PASS'
                    )
                ]) {
                    sh '''
                        set -e

                        echo "=========================================="
                        echo "Pushing image to Harbor"
                        echo "=========================================="

                        echo "$HARBOR_PASS" | docker login \
                            "${HARBOR_LOGIN_URL}" \
                            --username "$HARBOR_USER" \
                            --password-stdin

                        docker push "${FULL_IMAGE}:${BUILD_NUMBER}"
                        docker push "${FULL_IMAGE}:${DEPLOY_ENV}"
                    '''
                }
            }
        }

        stage('Deploy via Helm') {
            steps {
                withCredentials([
                    file(
                        credentialsId: env.KUBECONFIG_CRED,
                        variable: 'KUBECONFIG'
                    )
                ]) {
                    sh '''
                        set -e

                        echo "=========================================="
                        echo "Deploying SPRMS Regis"
                        echo "=========================================="

                        helm upgrade --install "${HELM_RELEASE}" \
                            "${HELM_CHART_PATH}" \
                            --namespace "${NAMESPACE}" \
                            --create-namespace \
                            -f "${HELM_CHART_PATH}/values.yaml" \
                            --set image.repository="${FULL_IMAGE}" \
                            --set image.tag="${BUILD_NUMBER}" \
                            --kubeconfig "$KUBECONFIG" \
                            --wait \
                            --atomic \
                            --timeout 10m
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                withCredentials([
                    file(
                        credentialsId: env.KUBECONFIG_CRED,
                        variable: 'KUBECONFIG'
                    )
                ]) {
                    sh '''
                        set -e

                        echo "=========================================="
                        echo "Checking rollout status"
                        echo "=========================================="

                        kubectl rollout status \
                            "deployment/${HELM_RELEASE_DEPLOYMENT}" \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            --timeout=5m

                        echo "=========================================="
                        echo "Current deployment"
                        echo "=========================================="

                        kubectl get deployment \
                            "${HELM_RELEASE_DEPLOYMENT}" \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            -o wide

                        echo "=========================================="
                        echo "Current SPRMS Regis pods"
                        echo "=========================================="

                        kubectl get pods \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            -l app=sprms-regis \
                            -o wide

                        echo "=========================================="
                        echo "SPMRS Regis service"
                        echo "=========================================="

                        kubectl get service \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            -l app=sprms-regis \
                            -o wide

                        echo "=========================================="
                        echo "SPMRS Regis endpoints"
                        echo "=========================================="

                        kubectl get endpoints \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            -l app=sprms-regis \
                            -o wide
                    '''
                }
            }
        }

        stage('Application Health Check') {
            steps {
                sh '''
                    set -e

                    echo "=========================================="
                    echo "Checking deployed application"
                    echo "=========================================="

                    for i in $(seq 1 12); do
                        echo "Application check attempt $i/12"

                        HTTP_CODE=$(curl \
                            --silent \
                            --output "${REPORT_DIR}/application-response.txt" \
                            --write-out "%{http_code}" \
                            --connect-timeout 5 \
                            --max-time 15 \
                            "${TARGET_URL}" || true)

                        echo "HTTP status: $HTTP_CODE"

                        if [ "$HTTP_CODE" != "000" ]; then
                            echo "SPMRS Regis is reachable."
                            exit 0
                        fi

                        sleep 5
                    done

                    echo "SPMRS Regis is not reachable after deployment."
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo """
                Deployment to ${params.DEPLOY_ENV} succeeded.
                Image: ${FULL_IMAGE}:${BUILD_NUMBER}
                Target: ${TARGET_URL}
            """
        }

        failure {
            echo 'Deployment failed. Collecting diagnostics.'

            withCredentials([
                file(
                    credentialsId: env.KUBECONFIG_CRED,
                    variable: 'KUBECONFIG'
                )
            ]) {
                sh '''
                    echo "=========================================="
                    echo "Deployment diagnostics"
                    echo "=========================================="

                    kubectl get deployments,pods,services,endpoints \
                        --namespace "${NAMESPACE}" \
                        --kubeconfig "$KUBECONFIG" \
                        -o wide || true

                    echo "=========================================="
                    echo "Deployment description"
                    echo "=========================================="

                    kubectl describe deployment \
                        "${HELM_RELEASE_DEPLOYMENT}" \
                        --namespace "${NAMESPACE}" \
                        --kubeconfig "$KUBECONFIG" || true

                    echo "=========================================="
                    echo "Recent namespace events"
                    echo "=========================================="

                    kubectl get events \
                        --namespace "${NAMESPACE}" \
                        --kubeconfig "$KUBECONFIG" \
                        --sort-by=.metadata.creationTimestamp \
                        | tail -50 || true

                    echo "=========================================="
                    echo "SPMRS Regis pod logs"
                    echo "=========================================="

                    for pod in $(kubectl get pods \
                        --namespace "${NAMESPACE}" \
                        --kubeconfig "$KUBECONFIG" \
                        -l app=sprms-regis \
                        -o jsonpath='{.items[*].metadata.name}' 2>/dev/null); do

                        echo "===== Logs for $pod ====="

                        kubectl logs "$pod" \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            --all-containers=true \
                            --tail=200 || true

                        echo "===== Previous logs for $pod ====="

                        kubectl logs "$pod" \
                            --namespace "${NAMESPACE}" \
                            --kubeconfig "$KUBECONFIG" \
                            --all-containers=true \
                            --previous \
                            --tail=200 || true
                    done
                '''
            }
        }

        always {
            sh '''
                echo "=========================================="
                echo "Cleaning up"
                echo "=========================================="

                docker logout "${HARBOR_LOGIN_URL}" || true

                if [ -f vpn.pid ]; then
                    VPN_PID=$(cat vpn.pid)

                    if [ -n "$VPN_PID" ] && ps -p "$VPN_PID" > /dev/null 2>&1; then
                        echo "Stopping VPN process: $VPN_PID"
                        sudo -n kill "$VPN_PID" || kill "$VPN_PID" || true
                        sleep 3
                    fi

                    rm -f vpn.pid
                fi

                sudo -n pkill -f \
                    "openconnect.*secure.neyduetewa.gov.bt" || true

                echo "Cleanup completed"
            '''

            archiveArtifacts(
                artifacts: 'reports/**/*',
                allowEmptyArchive: true
            )
        }
    }
}
