package gr.devian.talosquests.backend;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.QuestModel;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Nikolas on 5/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
     * @param obj The Object to map.
     * @return A String of JSON.
     * @throws JsonProcessingException Thrown if an error occurs while mapping.
     */
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * Maps a String of JSON into an instance of a Class of type T. Uses a
     * Jackson ObjectMapper.
     * @param json A String of JSON.
     * @param clazz A Class of type T. The mapper will attempt to convert the
     *        JSON into an Object of this Class type.
     * @return An Object of type T.
     * @throws JsonParseException Thrown if an error occurs while mapping.
     * @throws JsonMappingException Thrown if an error occurs while mapping.
     * @throws IOException Thrown if an error occurs while mapping.
     */
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
    protected SecureRandom random = new SecureRandom();
    protected QuestModel generateQuest(LatLng location) {
        QuestModel q = new QuestModel();
        q.setLocation(location);
        q.setContent(new BigInteger(130, random).toString(50));
        q.setName(new BigInteger(130, random).toString(5));
        QuestChoice c;
        for (int i = 0; i <= 5; i++) {
            c = new QuestChoice();
            c.setContent(new BigInteger(130, random).toString(10));
            q.getAvailableChoices().add(c);
            q.setCorrectChoice(c);
        }
        return q;
    }
}
