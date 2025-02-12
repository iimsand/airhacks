package airhacks.functionurl.boundary;

import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

    static String FUNCTION_NAME = "airhacks_Filipe";

    public FunctionURLStack(Construct construct,String id,boolean snapStart) {
        super(construct,id+ "-function-url-stack");
        var quarkusLambda = new QuarkusLambda(this, FUNCTION_NAME,snapStart);
        var function = quarkusLambda.getFunction();
        var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .build());
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();

    }
}
