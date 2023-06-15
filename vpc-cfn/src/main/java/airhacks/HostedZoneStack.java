package airhacks;

import java.util.List;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.CfnVPC;
import software.amazon.awscdk.services.route53.CfnHostedZone;
import software.amazon.awscdk.services.route53.CfnHostedZone.HostedZoneConfigProperty;
import software.constructs.Construct;

public class HostedZoneStack extends Stack {

    public HostedZoneStack(Construct scope, String appName,CfnVPC vpc,String zoneName) {
        super(scope, appName + "-hosted-zone");
        var hostedZone = this.hostedZone(vpc, zoneName, "private hosted name for custom DNS names");
        CfnOutput.Builder.create(this, "hosted-zone-output").value(hostedZone.getRef()).build();
    }


    CfnHostedZone hostedZone(CfnVPC vpc,String name,String comment) {
        return CfnHostedZone.Builder.create(this, "hosted-zone")
                        .vpcs(List.of(vpc))
                        .name(name)
                        .hostedZoneConfig(HostedZoneConfigProperty.builder()
                                        .comment(comment).build())
                        .build();
}
}
