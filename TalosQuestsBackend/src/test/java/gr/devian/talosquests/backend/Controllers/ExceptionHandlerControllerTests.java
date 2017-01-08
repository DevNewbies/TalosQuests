package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import org.apache.catalina.loader.WebappClassLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 20/12/2016.
 */

public class ExceptionHandlerControllerTests extends AbstractControllerTest {
    @Autowired
    public void getWebApplicationContext(WebApplicationContext web) {
        if (!web.getServletContext().setInitParameter("throwExceptionIfNoHandlerFound", "true")) {
            logger.warn("throwExceptionIfNoHandlerFound=" + web.getServletContext().getInitParameter("throwExceptionIfNoHandlerFound"));
        }
        webApplicationContext = web;

    }

    WebApplicationContext webApplicationContext;



    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    }

    @Test
    public void testMethodNotSupportedRequest() throws Exception {
        assertEquals(webApplicationContext.getServletContext().getInitParameter("throwExceptionIfNoHandlerFound"),"true");
        mockMvc.perform(post("/Echo/Test")).andExpect(status().isBadRequest()).andDo(print()).andReturn();
    }


    @Test
    public void testMethodMissingServletRequestParameter() throws Exception {
        mockMvc.perform(get("/Echo/Test")).andExpect(status().isBadRequest()).andDo(print()).andReturn();
    }

    @Test
    public void testMethodEchoOnSuccess() throws Exception {
        mockMvc.perform(get("/Echo/Test").param("echoParam", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.param", is("test")))
                .andExpect(jsonPath("$.response.path", is("Test")))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testMethodNotFound() throws Exception {
        mockMvc.perform(
                get("/something"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testUnhandledException() throws Exception {
        mockMvc.perform(get("/Error/500")).andExpect(status().isInternalServerError()).andReturn();
    }
}
