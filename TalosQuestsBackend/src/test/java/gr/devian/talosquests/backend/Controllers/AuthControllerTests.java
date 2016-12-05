package gr.devian.talosquests.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.AbstractTest;
import gr.devian.talosquests.backend.AbstractUserControllerTest;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
                .content(mapToJson(invalidModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void UnauthorizedOnWrongCredentials() throws Exception {

        validModel.setPassWord("bla");
        mockMvc.perform(post("/Auth")
                .content(mapToJson(validModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void AuthorizedOnCorrectCredentials() throws Exception {

        mockMvc.perform(post("/Auth")
                .content(mapToJson(validModel))
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
