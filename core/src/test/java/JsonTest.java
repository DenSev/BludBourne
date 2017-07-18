import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.entity.EntityConfig;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created on 16.04.2017.
 */
public class JsonTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonTest.class);
    Json json = new Json();


    @Test
    public void testJsonParser() throws Exception {
        EntityConfig test = MapperProvider.INSTANCE.parse(EntityConfig.class, "town_blacksmith.json");

        System.out.println(MapperProvider.INSTANCE.writeValueAsString(test));
    }
}
