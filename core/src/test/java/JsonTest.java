import com.badlogic.gdx.utils.Json;
import com.google.common.base.Stopwatch;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created on 16.04.2017.
 */
public class JsonTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonTest.class);
    Json json = new Json();


    @Test
    public void testJsonParser() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.debug("Json: {}", sendMessage("This is a message", 125, true));
        LOGGER.debug("Elapsed: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        /*public void sendMessage(Component.MESSAGE messageType, String ... args){
        String fullMessage = messageType.toString();

		for (String string : args) {
			fullMessage += Component.MESSAGE_TOKEN + string;
		}

		for(Component component: _components){
			component.receiveMessage(fullMessage);
		}
	}*/
    }

    public String sendMessage(String message, Object... args) throws Exception {

        return message + MapperProvider.INSTANCE.mapper().writeValueAsString(args);
    }
}
