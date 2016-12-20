package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 20/12/2016.
 */
@Transactional
public class ExceptionHandlerControllerTests extends AbstractControllerTest {
    @Before
    @Override
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testMethodNotSupportedRequest() throws Exception {
        mockMvc.perform(post("/Echo/Test")).andExpect(status().isBadRequest()).andDo(print()).andReturn();
    }


    @Test
    public void testMethodMissingServletRequestParameter() throws Exception {
        mockMvc.perform(get("/Echo/Test")).andExpect(status().isBadRequest()).andDo(print()).andReturn();
    }

    @Test
    public void testMethodEchoOnSuccess() throws Exception {
        mockMvc.perform(get("/Echo/Test").param("echoParam","test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.param",is("test")))
                .andExpect(jsonPath("$.response.path",is("Test")))
                .andDo(print())
                .andReturn();
    }

}
