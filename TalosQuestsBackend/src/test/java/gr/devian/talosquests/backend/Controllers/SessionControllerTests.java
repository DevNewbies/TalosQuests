package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 19/12/2016.
 */
@Transactional
public class SessionControllerTests extends AbstractControllerTest {
    @Test
    public void testGetSessionWhenTokenIsValid() throws Exception {
        mockMvc.perform(get("/Session")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetSessionWhenTokenIsInvalid() throws Exception {
        mockMvc.perform(get("/Session")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

}
