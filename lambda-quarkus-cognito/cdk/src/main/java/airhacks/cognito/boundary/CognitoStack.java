package airhacks.cognito.boundary;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.cognito.AuthFlow;
import software.amazon.awscdk.services.cognito.AutoVerifiedAttrs;
import software.amazon.awscdk.services.cognito.CognitoDomainOptions;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClient;
import software.amazon.awscdk.services.cognito.UserPoolClientOptions;
import software.amazon.awscdk.services.cognito.UserPoolDomain;
import software.amazon.awscdk.services.cognito.UserPoolDomainOptions;
import software.constructs.Construct;

public class CognitoStack extends Stack {
    static final String DOMAIN_PREFIX = "airhacks-lambda-authorizers";
    private UserPoolClient poolClient;
    private UserPool pool;
    private UserPoolDomain domain;

    public CognitoStack(Construct scope) {
        super(scope, "airhacks-lambda-cognito-stack");
        this.pool = UserPool.Builder.create(this, "AirhacksPool")
                .userPoolName("AirhacksPool")
                .selfSignUpEnabled(true)
                .autoVerify(AutoVerifiedAttrs.builder().email(false).build())
                .build();

        var userPoolClientOptions = UserPoolClientOptions.builder()
                .generateSecret(false)
                .idTokenValidity(Duration.days(1))
                .userPoolClientName("AirhacksLambdaClientPool")
                .authFlows(AuthFlow.builder()
                        .userPassword(true)
                        .build())
                .build();
        this.poolClient = pool.addClient("AirhacksAppClient", userPoolClientOptions);
        this.domain = pool.addDomain("AirhacksPoolDomain", UserPoolDomainOptions
                .builder()
                .cognitoDomain(CognitoDomainOptions.builder()
                        .domainPrefix(DOMAIN_PREFIX)
                        .build())
                .build());
        CfnOutput.Builder.create(this, "UserPoolClientId").value(poolClient.getUserPoolClientId()).build();

    }

    

    static String getDomain() {
        return "https://%s.auth.eu-central-1.amazoncognito.com".formatted(DOMAIN_PREFIX);
    }

 
    // https://cognito-idp.[region].amazonaws.com/[userPoolId]/.well-known/openid-configuration
    public static String issuer(String userPoolClientId) {
            return "https://cognito-idp.eu-central-1.amazonaws.com/%s".formatted(userPoolClientId);
    }

    public UserPoolClient getPoolClient() {
        return poolClient;
    }

    public UserPool getPool() {
        return pool;
    }

}
