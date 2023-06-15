package airhacks.apigateway.boundary;

import airhacks.apigateway.control.HttpApiGatewayIntegration;
import airhacks.cognito.boundary.CognitoStack;
import airhacks.lambda.control.QuarkusLambda;
import airhacks.route53.control.PublicHostedZoneIntegration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {

    static String FUNCTION_NAME  = "airhacks_CognitorLambdaGreetings";

    public LambdaApiGatewayStack(Construct scope, String id,String zoneId,String zoneName,String userPoolId,String userPoolClientId,Certificate cert) {
        super(scope, id+"-apigateway-stack");
        var quarkuLambda = new QuarkusLambda(this,FUNCTION_NAME);
        var issuer = CognitoStack.issuer(userPoolId);
        var authorizer = HttpApiGatewayIntegration.createAuthorizer(userPoolClientId, issuer);
        var hostedZone = PublicHostedZoneIntegration.lookupHostedZone(this, zoneId, zoneName);
        HttpApiGatewayIntegration.integrate(this, hostedZone,zoneName, quarkuLambda.getFunction(),authorizer,cert);
    }

}
