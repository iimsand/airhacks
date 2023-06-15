package airhacks;


import java.util.List;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.CfnSecurityGroup;
import software.amazon.awscdk.services.ec2.CfnSecurityGroup.IngressProperty;
import software.amazon.awscdk.services.ec2.CfnVPCEndpoint;
import software.constructs.Construct;

public class EndpointsStack extends Stack {
    
    public EndpointsStack(final Construct scope, final String appName) {
        super(scope, appName + "-endpoint");
    }
    
    public void setup(String vpcId, String routeTableId,String subnetOneId,String subnetTwoId) {
        var subnets = List.of(subnetOneId, subnetTwoId);

        var endpointSecurityGroup = this.endpointSecurityGroup(vpcId);
        var s3Endpoint = this.createVPCGatewayEndpoint("com.amazonaws.eu-central-1.s3", vpcId,routeTableId);
        var ecrDKREndpoint = this.createVPCInterfaceEndpoint("com.amazonaws.eu-central-1.ecr.dkr", vpcId, subnets, endpointSecurityGroup);
        var ecrAPIEndpoint = this.createVPCInterfaceEndpoint("com.amazonaws.eu-central-1.ecr.api", vpcId, subnets, endpointSecurityGroup);
        var cloudWatchEndpoint = this.createVPCInterfaceEndpoint("com.amazonaws.eu-central-1.logs", vpcId, subnets, endpointSecurityGroup);
        var secretManagerEndpoint = this.createVPCInterfaceEndpoint("com.amazonaws.eu-central-1.secretsmanager", vpcId, subnets, endpointSecurityGroup);
        var systemManagerEndpoint = this.createVPCInterfaceEndpoint("com.amazonaws.eu-central-1.ssm", vpcId,subnets, endpointSecurityGroup);
        CfnOutput.Builder.create(this, "s3-output").value(s3Endpoint.getRef()).build();
        CfnOutput.Builder.create(this, "ecr-dkr-output").value(ecrDKREndpoint.getRef()).build();
        CfnOutput.Builder.create(this, "ecr-api-output").value(ecrAPIEndpoint.getRef()).build();
        CfnOutput.Builder.create(this, "cloud-watch-output").value(cloudWatchEndpoint.getRef()).build();
        CfnOutput.Builder.create(this, "secret-manager-output").value(secretManagerEndpoint.getRef()).build();
        CfnOutput.Builder.create(this, "system-manager-output").value(systemManagerEndpoint.getRef()).build();
        CfnOutput.Builder.create(this, "endpoint-security-group-output").value(endpointSecurityGroup.getRef()).build();
        
    }
    
    CfnVPCEndpoint createVPCInterfaceEndpoint(String serviceName, String vpcId, List<String> subnetIds,CfnSecurityGroup endpointSecurityGroup) {
        
        return CfnVPCEndpoint.Builder.create(this, serviceName).privateDnsEnabled(true)
                .vpcId(vpcId)
                .serviceName(serviceName)
                .subnetIds(subnetIds)
                .securityGroupIds(List.of(endpointSecurityGroup.getRef()))
                .vpcEndpointType("Interface").build();
    }

    CfnVPCEndpoint createVPCGatewayEndpoint(String serviceName, String vpc,String routeTableId) {
        return CfnVPCEndpoint.Builder.create(this, serviceName)
                .vpcId(vpc)
                .routeTableIds(List.of(routeTableId))
                .serviceName(serviceName)
                .vpcEndpointType("Gateway")
                .build();
    }



    CfnSecurityGroup endpointSecurityGroup(String vpc) {

        var ingressProperty = IngressProperty.builder()
                .toPort(443)
                .fromPort(443)
                .ipProtocol("tcp")
                .cidrIp("0.0.0.0/0")
                 .build();
        var securityGroup =  CfnSecurityGroup.Builder.create(this, "endpoint-security-group")
                 .groupName("endpoint-security-group")
                 .groupDescription("permissive group for VPC endpoints")
                 .vpcId(vpc)
                 .securityGroupIngress(List.of(ingressProperty))
                .build();
        return securityGroup;
        } 
}
