package airhacks.route53.control;

import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.HostedZoneAttributes;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.PublicHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.constructs.Construct;

public interface PublicHostedZoneIntegration {

    static ARecord provisionARecord(Construct scope, IHostedZone hostedZone, String zoneName, RecordTarget recordTarget,
            String domainPrefix) {

        return ARecord.Builder.create(scope, "AliasRecord")
                .zone(hostedZone)
                .target(recordTarget)
                .recordName(domainPrefix)
                .build();
    }

    static IHostedZone lookupHostedZone(Construct scope, String hostedZoneID, String zoneName) {
        return PublicHostedZone.fromHostedZoneAttributes(scope, "PublicHostedZone",
                HostedZoneAttributes.builder()
                        .hostedZoneId(hostedZoneID)
                        .zoneName(zoneName)
                        .build());
    }
}
