package airhacks.lambda.greetings.boundary;

import static java.lang.System.Logger.Level.*;

import java.security.Principal;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class Greeter {

    static System.Logger LOG = System.getLogger(Greeter.class.getName()); 

    @Inject
    JsonWebToken token;
    
    @Inject
    Principal principal;


    @Inject
    @ConfigProperty(defaultValue = "hello, quarkus on AWS", name="message")
    String message;
    
    public String greetings() {
        LOG.log(INFO, "returning: " + this.message);
        LOG.log(INFO, "token: " + this.token.getRawToken());
        LOG.log(INFO, "principal: " + this.principal.getName());
        return this.message;
    }

    public void greetings(String userMessage) {
        LOG.log(INFO, "received: " + userMessage);
    }
}
