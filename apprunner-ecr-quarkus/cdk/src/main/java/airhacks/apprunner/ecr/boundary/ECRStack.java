package airhacks.apprunner.ecr.boundary;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ecr.Repository;
import software.constructs.Construct;

public class ECRStack extends Stack {
    

    Repository repository;

    public ECRStack(Construct scope,String repositoryName) {
        super(scope, "airhacks-ecr-apprunner");
        this.repository = Repository
                .Builder.create(this, "AirhacksECRRepository")
                        .imageScanOnPush(false)
                        .repositoryName("airhacks/" + repositoryName)
                        .removalPolicy(RemovalPolicy.DESTROY)
        .build();
    }

    public Repository getRepository() {
        return repository;
    }
    
    
}
