package airhacks.greetings.configuration.boundary;

import java.util.Map;

public interface TaskConfiguration {
    static final Map<String,String> ENVIRONMENT = Map.of("message","hello from the clouds",
                                                        "configuration","in the cloud");
}
