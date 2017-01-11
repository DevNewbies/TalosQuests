package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Nikolas on 20/12/2016.
 */
@Transactional
public class ServiceInfoControllerTests extends AbstractControllerTest {

    @Test
    public void OKRequestOnServiceInfoController() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response.remoteAddr",is("127.0.0.1")))
                .andReturn();
    }
    @Test
    public void OKRequestOnServiceInfoControllerWithForwardedHeader() throws Exception {
        mockMvc.perform(get("/").header("X-Forwarded-For","0.0.0.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response.remoteAddr",is("0.0.0.0")))
                .andReturn();
    }


}
