package airhacks;

import java.util.List;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class VpcStack extends Stack {

    public VpcStack(Construct scope, String name) {
        super(scope,"airhacks-vpc");
        var vpc = Vpc.Builder.create(this, "Vpc")
                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .subnetConfiguration(List.of(SubnetConfiguration.builder()
                        .subnetType(SubnetType.PUBLIC)
                        .name(name+"-subnet")
                        .build()))
                .vpcName(name)
                .maxAzs(2)
                .build();
        var publicSubnets = vpc.getPublicSubnets();
        for (var subnet : publicSubnets) {
            CfnOutput.Builder.create(this, "PublicSubnetOutput" + subnet.getNode().getId())
                    .value(subnet.getSubnetId())
                    .build();
        }

        var privateSubnets = vpc.getPrivateSubnets();
        for (var subnet : privateSubnets) {
            CfnOutput.Builder.create(this, "PrivateSubnetOutput" + subnet.getNode().getId())
                    .value(vpc.getVpcId())
                    .build();
        }

        CfnOutput.Builder.create(this, "VpcIdOutput").value(vpc.getVpcId()).build();
    }
}
