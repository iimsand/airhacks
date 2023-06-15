package airhacks.s3.boundary;

import airhacks.s3.control.S3ReadRole;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class S3ReadRoleStack extends Stack {

    public S3ReadRoleStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        new S3ReadRole(this);
    }
}
