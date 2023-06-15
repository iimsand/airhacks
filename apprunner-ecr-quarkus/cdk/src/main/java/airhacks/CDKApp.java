package airhacks;

import airhacks.apprunner.ecr.boundary.ECRStack;
import airhacks.apprunner.service.boundary.AppRunnerStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {
    
    public static void main(final String[] args) {

            var app = new App();
            var appName = "apprunner-ecr-quarkus";
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");
            Tags.of(app).add("application", appName);

            var stackProps = StackProps.builder()
                    .build();
        var ecrStack = new ECRStack(app, "quarkus-apprunner");
        var repository = ecrStack.getRepository();
        new AppRunnerStack(app, appName, repository, stackProps);
        app.synth();
    }
}
