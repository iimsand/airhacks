package airhacks;

import airhacks.greetings.ecr.boundary.ECRStack;
import airhacks.greetings.ecs.boundary.ECSFargateStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;



public class CDKApp {
    public static void main(final String[] args) {
            var appName = "ecs-fargate-quarkus";
            var app = new App();
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");
            Tags.of(app).add("application", "ecs-fargate-quarkus");

        
        
        var ecrStack = new ECRStack(app, appName);
        var repository = ecrStack.getRepository();
        new ECSFargateStack(app, appName ,repository);
        app.synth();
    }
}
