package airhacks;

import airhacks.s3.boundary.S3ReadRoleStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {
    public static void main(final String[] args) {

            var app = new App();
            var appName = "iam-s3read-role";
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");
            Tags.of(app).add("application", appName);

            var stackProps = StackProps.builder()
                    .build();
        
        new S3ReadRoleStack(app, appName, stackProps);
        app.synth();
    }
}
