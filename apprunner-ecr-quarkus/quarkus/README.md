# quarkus Project
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 867134250547.dkr.ecr.eu-central-1.amazonaws.com
docker build -f src/main/docker/Dockerfile.jvm --platform linux/amd64 -t airhacks/quarkus-apprunner .
docker tag airhacks/quarkus-apprunner:latest 867134250547.dkr.ecr.eu-central-1.amazonaws.com/airhacks/quarkus-apprunner:latest
docker push 867134250547.dkr.ecr.eu-central-1.amazonaws.com/airhacks/quarkus-apprunner:latest

aws apprunner start-deployment --service-arn arn:aws:apprunner:eu-central-1:867134250547:service/AppRunnerServiceA852BA10-3AEvIUlrTdnF/7f36c69de17f43c59b88bbfc9c5063f9