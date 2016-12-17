package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractUserControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by Nikolas on 4/12/2016.
 */
@Transactional
public class AuthControllerTests extends AbstractUserControllerTest {


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
                .content(mapToJson(nonExistentUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void UnauthorizedOnWrongCredentials() throws Exception {

        registeredUserModel.setPassWord("bla");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(registeredUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void AuthorizedOnCorrectCredentialsWithUsernamePassword() throws Exception {
        registeredUserModel.setEmail("");
        registeredUserModel.setImei("");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(registeredUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(200)))
                .andReturn();

    }
    @Test
    public void AuthorizedOnCorrectCredentialsWithEmailPassword() throws Exception {
        registeredUserModel.setUserName("");
        registeredUserModel.setImei("");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(registeredUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(200)))
                .andReturn();

    }


    @Test
    public void BadRequestOnInsufficientCredentials() throws Exception {

        mockMvc.perform(post("/Auth")
                .content(mapToJson(insufficientDataModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.state",is(400)))
                .andReturn();

    }


}
