package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {
    public static void main(final String[] args) {

            var app = new App();
            var appName = "vpc-cfn";
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");
            Tags.of(app).add("application", appName);

            var stackProps = StackProps.builder()
                    .build();
            var hostedZoneName = "airhacks-live.eu-central-1.aws.cloud.airhacks";
            var vpcStack = new VPCStack(app, appName);
            var endpointStack = new EndpointsStack(app, appName);
            var subnets = vpcStack.getSubnets();
            var vpc =   vpcStack.getVpc();
            var subnetOne = subnets.get(0).getRef();
            var subnetTwo = subnets.get(1).getRef();
            //var routeTableId = "rtb-0bd2a5e66820d35ff";
            //endpointStack.setup(vpc.getRef(), routeTableId, subnetOne, subnetTwo);
            
            app.synth();
    }
}
