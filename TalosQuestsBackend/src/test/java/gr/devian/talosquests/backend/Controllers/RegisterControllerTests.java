package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractUserControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 5/12/2016.
 */
@Transactional
public class RegisterControllerTests extends AbstractUserControllerTest {

    @Test
    public void BadRequestOnEmptyJsonPost() throws Exception {
        mockMvc.perform(post("/Register")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void UnsupportedMediaTypeOnEmptyTextPlainPost() throws Exception {
        mockMvc.perform(post("/Register"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
    }

    @Test
    public void BadRequestOnInsufficientUserData() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(insufficientDataModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void SuccessOnWhenUserNotExists() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(nonExistentUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();



    }

    @Test
    public void UserFoundWhenUserExists() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(registeredUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void BadRequestWhenUsernameNotMetRequirements() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(usernameNotMatchesRequirementsModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void BadRequestWhenPasswordNotMetRequirements() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(passwordNotMatchesRequirementsModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void BadRequestWhenEmailNotMetRequirements() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(emailNotMatchesRequirementsModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void BadRequestWhenImeiNotMetRequirements() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(imeiNotMatchesRequirementsModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }
}
