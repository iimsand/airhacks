package airhacks.bucket.boundary;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;

public class BucketStack extends Stack {

    private Bucket bucket;

    public BucketStack(Construct scope) {
        super(scope, "BucketStack");
        this.bucket = Bucket.Builder.create(this, "MartinBucket")
        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
        .bucketName("airhacks-martin-bucket")
        .versioned(true)
        .eventBridgeEnabled(false)
        .build();
        CfnOutput.Builder.create(this, "BucketArn").value(this.bucket.getBucketArn()).build();
    }
}
