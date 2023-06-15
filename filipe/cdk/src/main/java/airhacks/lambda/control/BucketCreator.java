package airhacks.lambda.control;

import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;

public interface BucketCreator {

    static Bucket create(Construct scope){
        return 
        Bucket.Builder.create(scope, "FilipeBucket")
        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
        .bucketName("airhacks-filipe-bucket")
        .versioned(true)
        .build();
    }
}
