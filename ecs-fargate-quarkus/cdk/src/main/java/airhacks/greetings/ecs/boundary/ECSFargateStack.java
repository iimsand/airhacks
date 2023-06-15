package airhacks.greetings.ecs.boundary;

import java.util.List;
import java.util.Map;

import airhacks.greetings.configuration.boundary.TaskConfiguration;
import airhacks.greetings.monitoring.control.Monitoring;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.CfnService;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.CpuArchitecture;
import software.amazon.awscdk.services.ecs.FargateTaskDefinition;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.PortMapping;
import software.amazon.awscdk.services.ecs.RuntimePlatform;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationTargetGroup;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.Protocol;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

public class ECSFargateStack extends Stack {

        static final int PORT = 8080;
        LogGroup logGroup;

        public ECSFargateStack(Construct scope, String appName, Repository repository) {
                super(scope, appName);
                this.logGroup = createLogGroup(appName);
                new Monitoring(this);
                var vpc = Vpc.Builder.create(this, appName + "-vpc")
                                .maxAzs(2)
                                .enableDnsHostnames(true)
                                .enableDnsSupport(true)
                                .vpcName("airhacks-ecs-fargate")
                                .subnetConfiguration(List.of(
                                                SubnetConfiguration.builder()
                                                                .cidrMask(28)
                                                                .name("public-")
                                                                .subnetType(SubnetType.PUBLIC)
                                                                .build()))

                                .build();

                var image = ContainerImage.fromEcrRepository(repository);

                var cluster = Cluster.Builder
                                .create(this, appName + "-cluster")
                                .clusterName(appName + "-cluster")
                                .containerInsights(true)
                                .vpc(vpc)
                                .build();
                var taskDefinition = this.createTaskDefinition(image,appName);
                var service = ApplicationLoadBalancedFargateService.Builder.create(this, appName + "-service")
                                .cluster(cluster) // Required
                                .assignPublicIp(true)
                                .serviceName(appName + "-service")
                                .loadBalancerName(appName + "-lb")
                                .cpu(512) // Default is 256
                                .desiredCount(1) // minimum value is 1
                                .healthCheckGracePeriod(Duration.minutes(2))
                                .taskDefinition(taskDefinition)
                                .memoryLimitMiB(1024) // Default is 512
                                .publicLoadBalancer(true) // Default is false
                                .build();
                configureLoadBalancerHealthCheck(service.getTargetGroup());
                /**
                 * Overridden. Workaround from: https://github.com/aws/aws-cdk/issues/3646
                 */
                var child = service.getService().getNode();
                var cfnService = (CfnService) child.findChild("Service");
                cfnService.setDesiredCount(0);
                var clusterName = cluster.getClusterName();
                var serviceName = service.getService().getServiceName();
                CfnOutput.Builder.create(this, "ECSClusterNameOutput").value(clusterName).build();
                CfnOutput.Builder.create(this, "ECSserviceNameOutput").value(serviceName).build();

                CfnOutput.Builder.create(this, "ECSForceDeploymentCMDOutput")
                                .value(String.format(
                                                "aws ecs update-service --force-new-deployment --cluster %s  --service %s",
                                                clusterName, serviceName))
                                .build();

                CfnOutput.Builder.create(this, "ECSDesiredTaskCountCMDOutput")
                                .value(String.format(
                                                "aws ecs update-service --cluster %s  --service %s --desired-count 1",
                                                clusterName, serviceName))
                                .build();
        }

        FargateTaskDefinition createTaskDefinition(ContainerImage image, String appName) {
                var taskDefinition = FargateTaskDefinition.Builder.create(this, "FargateTaskDefinition")
                                .cpu(512)
                                .runtimePlatform(RuntimePlatform.builder()
                                                .cpuArchitecture(CpuArchitecture.ARM64)
                                                .build())
                                .family(appName)
                                .memoryLimitMiB(1024)
                                .build();
                var containerName = appName + "-container";
                var serviceName = appName + "-service";
                var configuration = TaskConfiguration.ENVIRONMENT;
                var port = 8080;                
                var healthCheck = software.amazon.awscdk.services.ecs.HealthCheck.builder()
                                .command(List.of("curl -k -f http://localhost:" + port + "/q/health/live || exit 1"))
                                .interval(Duration.minutes(1))
                                .timeout(Duration.seconds(10))
                                .retries(3)
                                .startPeriod(Duration.minutes(3))
                                .build();
                
                taskDefinition.addContainer("default-container", createContainerDefinitionOptions(containerName,
                                serviceName, image, configuration, healthCheck, port));
                return taskDefinition;
        }

        ContainerDefinitionOptions createContainerDefinitionOptions(String containerName, String serviceName,
                        ContainerImage image, Map<String, String> configuration,
                        software.amazon.awscdk.services.ecs.HealthCheck healthCheck, int port) {
                                var loggingProperties = AwsLogDriverProps.builder()
                                .streamPrefix(serviceName)
                                .logGroup(this.logGroup)
                                .build();
                return ContainerDefinitionOptions.builder()
                                .containerName(containerName)
                                .image(image)
                                .essential(true)
                                .environment(configuration)
                                .healthCheck(healthCheck)
                                .portMappings(List.of(PortMapping.builder().containerPort(port).build()))
                                .logging(LogDriver.awsLogs(loggingProperties))
                                .build();
        }

        LogGroup createLogGroup(String appName) {
                var logGroupName = "/ecs/" + appName;
                return LogGroup.Builder
                        .create(this, "LogGroup")
                        .logGroupName(logGroupName)
                        .retention(RetentionDays.FIVE_DAYS)
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .build();
            }
        void configureLoadBalancerHealthCheck(ApplicationTargetGroup targetGroup) {
                var healthCheck = HealthCheck.builder()
                                .path("/q/health/ready")
                                .interval(Duration.seconds(30))
                                .protocol(Protocol.HTTP)
                                .unhealthyThresholdCount(3)
                                .healthyThresholdCount(2)
                                .timeout(Duration.seconds(5))
                                .build();
                targetGroup.setHealthCheck(healthCheck);
        }
}
