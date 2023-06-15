package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {
    public static void main(final String[] args) {

            var app = new App();
            var appName = "airhacks-vpc-l1";
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");

        
        new VpcStack(app, appName);
        app.synth();
    }
}
