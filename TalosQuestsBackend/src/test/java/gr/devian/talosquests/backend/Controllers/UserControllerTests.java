package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Nikolas on 5/12/2016.
 */
@Transactional
public class UserControllerTests extends AbstractControllerTest {

    @Test
    public void GetUnauthorizedOnNoTokenSpecified() throws Exception {

        mockMvc.perform(get("/User"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void GetUnauthorizedOnWrongTokenSpecified() throws Exception {

        mockMvc.perform(get("/User")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void GetAuthorizedOnCorrectTokenSpecified() throws Exception {

        mockMvc.perform(get("/User")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void DeleteUnauthorizedOnNoTokenAndNoPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void DeleteUnauthorizedOnNoPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("token","test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void DeleteUnAuthorizedOnNoTokenSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("password","test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void DeleteUnauthorizedOnIncorrectTokenSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("token","test")
                .param("password","test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void DeleteUnauthorizedOnIncorrectPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("token",testSession.getToken())
                .param("password","bla"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void DeleteOkOnCorrectTokenAndPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("password",passWordPassing)
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void PutUnsupportedMediaTypeOnNoTokenAndNoPasswordAndNoModelSpecified() throws Exception {

        mockMvc.perform(put("/User"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();

    }

    @Test
    public void PutUnauthorizedOnNoPasswordSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("token","test")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void PutUnauthorizedOnNoTokenSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("password",passWordPassing)
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void PutUnsupportedMediaTypeOnNoModelSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("password",passWordPassing)
                .param("token","test"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();

    }

    @Test
    public void PutUnauthorizedOnIncorrectTokenSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("token","test")
                .param("password",passWordPassing)
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void PutUnauthorizedOnIncorrectPasswordSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("token",testSession.getToken())
                .param("password","bla")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void PutOkOnCorrectTokenAndPasswordSpecified() throws Exception {

        testAuthRegisterModelCreatedWithSession.setEmail("test@test.test");

        mockMvc.perform(put("/User")
                .param("password",passWordPassing)
                .param("token",testSession.getToken())
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response.email",is("test@test.test")))
                .andReturn();

    }
}
