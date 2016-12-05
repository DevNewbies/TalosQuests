package gr.devian.talosquests.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.AbstractTest;
import gr.devian.talosquests.backend.AbstractUserControllerTest;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void UnsupportMediaTypeOnEmptyTextPlainPost() throws Exception {
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
                .content(mapToJson(invalidModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void UserFoundOnWhenUserNotExists() throws Exception {
        mockMvc.perform(post("/Register")
                .content(mapToJson(validModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }
}
