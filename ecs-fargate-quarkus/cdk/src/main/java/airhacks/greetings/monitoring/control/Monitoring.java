package airhacks.greetings.monitoring.control;

import java.util.List;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.events.EventPattern;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.targets.CloudWatchLogGroup;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

public class Monitoring extends Construct {

    Rule rule;

    public Monitoring(Construct scope) {
        super(scope, "FargateMonitoring");
        var logGroup = this.createLogGroup();
        var logGroupTarget = CloudWatchLogGroup.Builder
        .create(logGroup)
        .build();

        this.rule = Rule.Builder.create(this, "rule")
                .eventPattern(EventPattern.builder()
                        .source(List.of("aws.ecs"))
                        .build())
                .targets(List.of(logGroupTarget))
                .build();

    }


    LogGroup createLogGroup() {
        return LogGroup.Builder
                .create(this, "LogGroup")
                .logGroupName("/ecs/airhacks/performance")
                .retention(RetentionDays.FIVE_DAYS)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }


    public Rule getRule(){
        return this.rule;
    }

}
