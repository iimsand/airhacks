package airhacks.apigateway.control;

import java.util.List;

import airhacks.CDKApp;
import airhacks.route53.control.PublicHostedZoneIntegration;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.services.apigatewayv2.alpha.DomainMappingOptions;
import software.amazon.awscdk.services.apigatewayv2.alpha.DomainName;
import software.amazon.awscdk.services.apigatewayv2.alpha.EndpointType;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.IHttpRouteAuthorizer;
import software.amazon.awscdk.services.apigatewayv2.authorizers.alpha.HttpJwtAuthorizer;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.targets.ApiGatewayv2DomainProperties;
import software.constructs.Construct;

public interface HttpApiGatewayIntegration {

     /**
     * https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html
     */
    static void integrate(Construct scope,IHostedZone hostedZone,String zoneName,IFunction function,IHttpRouteAuthorizer authorizer,Certificate certificate) {
        var lambdaIntegration = HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration", function)
        .build();
        var domainNameWithPrefix = getDomainWithPrefix(zoneName);

        var domainName = DomainName.Builder.create(scope, "DomainName")
                                .certificate(certificate)
                                .endpointType(EndpointType.REGIONAL)
                                .domainName(domainNameWithPrefix)
                                .build();
                var domainNameOptions = DomainMappingOptions.builder()
                                .domainName(domainName)
                                .build();

        var httpApiGateway = HttpApi.Builder.create(scope, "HttpApiGatewayIntegration")                
                .defaultIntegration(lambdaIntegration)
                .defaultAuthorizer(authorizer)
                .defaultDomainMapping(domainNameOptions)
                .build();
        var url =  httpApiGateway.getUrl();

        setupDomainPrefix(scope, hostedZone,zoneName, domainName);
        CfnOutput.Builder.create(scope, "HttpApiGatewayUrlOutput").value(url).build();
        CfnOutput.Builder.create(scope, "HttpApiGatewayCurlOutput").value("curl -i " + url + "hello").build();
    }

    static String getDomainWithPrefix(String zoneName){
        return CDKApp.APP_NAME + "."+zoneName;
    }

     static IHttpRouteAuthorizer createAuthorizer(String userPoolClientId,String issuer){
        return HttpJwtAuthorizer.Builder.create("JWTCognitoUserPoolAuthorizer",issuer)
          .jwtAudience(List.of(userPoolClientId))
          .identitySource(List.of("$request.header.Authorization"))
          .build();        
    }

   

    static void setupDomainPrefix(Construct scope,IHostedZone hostedZone, String zoneName, DomainName domainName) {
                var domainPrefix = CDKApp.APP_NAME;
                var recordTarget = RecordTarget
                                .fromAlias(new ApiGatewayv2DomainProperties(domainName.getRegionalDomainName(),
                                                domainName.getRegionalHostedZoneId()));
                PublicHostedZoneIntegration.provisionARecord(scope, hostedZone, zoneName, recordTarget, domainPrefix);
        }



}
