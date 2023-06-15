package airhacks.s3.control;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

public class S3ReadRole extends Construct {

    Role role;

    public S3ReadRole(Construct scope) {
        super(scope,"S3ReadRole");
        var policyStatement = PolicyStatement.Builder
                        .create()
                        .effect(Effect.ALLOW)
                        .actions(List.of("s3:GetObject"))
                        .resources(List.of("*"))
                        .build();

        var policyDocument = create(policyStatement);
 
        this.role = Role.Builder.create(this, "role")
                .assumedBy(ServicePrincipal.Builder.create("lambda.amazonaws.com").build())
                .roleName("airhacks-s3-read")
                .inlinePolicies(Map.of("S3ReadExample",policyDocument))
            .build();

    }

    static PolicyDocument create(PolicyStatement policyStatement) {
        return PolicyDocument.Builder
                .create()
                .statements(List.of(policyStatement))
                .build();
    }


}
