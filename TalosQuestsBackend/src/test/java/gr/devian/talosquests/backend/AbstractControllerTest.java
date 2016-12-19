package gr.devian.talosquests.backend;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.Controllers.BaseController;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * Created by Nikolas on 5/12/2016.
 */
public abstract class AbstractControllerTest extends AbstractTest {
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    /**
     * Prepares the test class for execution of web tests. Builds a MockMvc
     * instance. Call this method from the concrete JUnit test class in the
     * <code>@Before</code> setup method.
     */
    protected void setUp() throws TalosQuestsException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Prepares the test class for execution of web tests. Builds a MockMvc
     * instance using standalone configuration facilitating the injection of
     * Mockito resources into the controller class.
     * @param controller A controller object to be tested.
     */
    protected void setUp(BaseController controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


}
