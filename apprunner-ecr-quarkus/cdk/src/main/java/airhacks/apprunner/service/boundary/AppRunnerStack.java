package airhacks.apprunner.service.boundary;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apprunner.alpha.Cpu;
import software.amazon.awscdk.services.apprunner.alpha.EcrProps;
import software.amazon.awscdk.services.apprunner.alpha.ImageConfiguration;
import software.amazon.awscdk.services.apprunner.alpha.Memory;
import software.amazon.awscdk.services.apprunner.alpha.Service;
import software.amazon.awscdk.services.apprunner.alpha.Source;
import software.amazon.awscdk.services.ecr.IRepository;

public class AppRunnerStack extends Stack {

    Service apprunnerService;

    public AppRunnerStack(Construct scope, String id, IRepository repository, StackProps props) {
        super(scope, id, props);

        this.apprunnerService = Service.Builder.create(this, "AppRunnerService")
                .cpu(Cpu.ONE_VCPU)
                .memory(Memory.FOUR_GB)
                .autoDeploymentsEnabled(true)
                .source(Source.fromEcr(EcrProps.builder()
                        .repository(repository)
                        .tagOrDigest("latest")
                        .imageConfiguration(ImageConfiguration
                                .builder()
                                .port(8080)
                                .build())
                        .build()))
                .build();
        CfnOutput.Builder.create(this, "AppRunnerServiceArn").value(this.apprunnerService.getServiceArn()).build();
        CfnOutput.Builder.create(this, "AppRunnerServiceURL").value(this.apprunnerService.getServiceUrl()).build();
    }
}
