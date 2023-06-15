# quarkus Project
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 867134250547.dkr.ecr.eu-central-1.amazonaws.com
docker build -f src/main/docker/Dockerfile.jvm -t airhacks/ecs-fargate-quarkus .
docker tag airhacks/ecs-fargate-quarkus:latest 867134250547.dkr.ecr.eu-central-1.amazonaws.com/airhacks/ecs-fargate-quarkus:latest
docker push 867134250547.dkr.ecr.eu-central-1.amazonaws.com/airhacks/ecs-fargate-quarkus:latest
aws ecs update-service --force-new-deployment --cluster ecs-fargate-quarkus-cluster  --service ecs-fargate-quarkus-service