package airhacks;

import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.CfnSubnet;
import software.amazon.awscdk.services.ec2.CfnVPC;
import software.amazon.awscdk.services.route53.CfnHostedZone;
import software.amazon.awscdk.services.route53.CfnHostedZoneProps;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.CfnHostedZone.HostedZoneConfigProperty;

public class VPCStack extends Stack {
        CfnVPC vpc;
        List<CfnSubnet> subnets;

    public VPCStack(Construct scope,String appName) {
            super(scope, appName+"-stack");
            this.subnets = new ArrayList<>();
        //https://docs.aws.amazon.com/vpc/latest/userguide/vpc-dns.html
        //https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/dedicated-instance.html
        var nameTag = CfnTag.builder().key("Name").value(appName).build();
        this.vpc = CfnVPC.Builder.create(this, "vpc")
                        .cidrBlock("10.0.0.0/24")
                        .tags(List.of(nameTag))
                        .enableDnsHostnames(true)
                .enableDnsSupport(true)
                        .instanceTenancy("default")
                .build();
        
        var subnetOneName = "subnet-one";
        var subnetOne = CfnSubnet.Builder.create(this, subnetOneName)
                .availabilityZone("eu-central-1a")
                        .cidrBlock("10.0.0.0/25")
                        .vpcId(vpc.getRef())
                        .tags(List.of(name(appName,subnetOneName)))
                .build();

        var subnetTwoName = "subnet-two";
        var subnetTwo = CfnSubnet.Builder.create(this, subnetTwoName)
                        .availabilityZone("eu-central-1b")
                        .cidrBlock("10.0.0.128/25")
                        .vpcId(vpc.getRef())
                        .tags(List.of(name(appName,subnetTwoName)))
                        .build();

                
        this.subnets.add(subnetOne);
        this.subnets.add(subnetTwo);
        CfnOutput.Builder.create(this,"vpc-output").exportName("vpc").value(vpc.getRef()).build();
        CfnOutput.Builder.create(this,"subnet-one-output").exportName("subnet-one").value(subnetOne.getRef()).build();
        CfnOutput.Builder.create(this, "subnet-two-output").exportName("subnet-two").value(subnetTwo.getRef()).build();

        var subnetOutput = String.format("[\"%s\",\"%s\"]",subnetOne.getRef(),subnetTwo.getRef());
        CfnOutput.Builder.create(this,"subnets-output").value(subnetOutput).build();

        }

        CfnTag name(String appName, String name) {
                var tagName = appName + "-" + name;
                return CfnTag.builder().key("Name").value(tagName).build();
        }

        public CfnVPC getVpc() {
                return vpc;
        }


        public List<CfnSubnet> getSubnets() {
                return subnets;
        }


 

}
