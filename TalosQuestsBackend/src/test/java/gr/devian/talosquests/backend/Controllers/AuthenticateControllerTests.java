package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by Nikolas on 4/12/2016.
 */
@Transactional
public class AuthenticateControllerTests extends AbstractControllerTest {


    @Test
    public void BadRequestOnEmptyJsonPost() throws Exception {
        mockMvc.perform(post("/Auth")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void UnsupportMediaTypeOnEmptyTextPlainPost() throws Exception {

        mockMvc.perform(post("/Auth"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
    }

    @Test
    public void UnauthorizedOnNonExistentUser() throws Exception {

        mockMvc.perform(post("/Auth")
                .content(mapToJson(testAuthRegisterModelNotCreated))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void UnauthorizedOnWrongCredentials() throws Exception {

        testAuthRegisterModelCreatedWithSession.setPassWord("bla");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void AuthorizedOnCorrectCredentialsWithUsernamePassword() throws Exception {
        testAuthRegisterModelCreatedWithSession.setEmail("");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(200)))
                .andExpect(jsonPath("$.response.token",is(testSession.getToken())))
                .andReturn();

    }
    @Test
    public void AuthorizedOnCorrectCredentialsWithEmailPassword() throws Exception {
        testAuthRegisterModelCreatedWithSession.setUserName("");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(200)))
                .andExpect(jsonPath("$.response.token",is(testSession.getToken())))
                .andReturn();

    }

    @Test
    public void AuthorizedOnCorrectCredentialsWithEmailPasswordWithExpiredSession() throws Exception {
        Session session = sessionService.getByUser(testUserWithSession);
        session.expire();
        testAuthRegisterModelCreatedWithSession.setUserName("");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(200)))
                .andExpect(jsonPath("$.response.token",not(session.getToken())))
                .andReturn();

    }

    @Test
    public void ForbiddenOnCorrectCredentialsWithEmailPasswordWithExpiredSessionButBanned() throws Exception {

        testAuthRegisterModelCreatedWithSession.setUserName("");
        testUserWithSession.setBanned(true);
        mockMvc.perform(post("/Auth")
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    public void BadRequestOnInsufficientCredentials() throws Exception {

        mockMvc.perform(post("/Auth")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.state",is(400)))
                .andReturn();

    }

}
