package airhacks;

import airhacks.apigateway.boundary.LambdaApiGatewayStack;
import airhacks.cognito.boundary.CognitoStack;
import airhacks.route53.boundary.DomainCertificateStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Tags;

public class CDKApp {

    public final static String APP_NAME = "lambda-quarkus-cognito";

    public static void main(final String[] args) {

        var app = new App();
        

        Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda with Cognito");
        Tags.of(app).add("environment", "development");
        Tags.of(app).add("application", APP_NAME);

        var hostedZoneId = "Z1009949319RWPYJKIHU0";
        var domainName = "airhacks.net";
        var certificateStack = new DomainCertificateStack(app, domainName);
        var certificate = certificateStack.getCertificate();
        var cognito = new CognitoStack(app);
        var poolClient = cognito.getPoolClient();
        var poolClientId = poolClient.getUserPoolClientId();

        var userPool = cognito.getPool();
        var userPoolId = userPool.getUserPoolId();
        new LambdaApiGatewayStack(app, APP_NAME, hostedZoneId,domainName,userPoolId,poolClientId,certificate);
        app.synth();
    }
}
