package airhacks.apprunner.health.boundary;
import java.lang.System.Logger.Level;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Liveness
@Readiness
@ApplicationScoped
public class Probes implements HealthCheck{

    @Inject
    @ConfigProperty(defaultValue = "0",name="block.health.in.seconds")
    long blockHealthInSeconds;

    System.Logger LOG = System.getLogger("health");

    @Override
    public HealthCheckResponse call() {
        LOG.log(Level.INFO,".");
        try {
            Thread.sleep(blockHealthInSeconds*1000);
        } catch (InterruptedException e) { }
        return HealthCheckResponse.up("generic");
    }
    
}
