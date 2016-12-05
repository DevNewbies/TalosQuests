package gr.devian.talosquests.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.AbstractTest;
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
public class AuthControllerTesting extends AbstractTest {

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;




    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserService userService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        sessionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();


        AuthRegisterModel authRegModel = new AuthRegisterModel();
        authRegModel.setUserName("test");
        authRegModel.setPassWord("test");
        authRegModel.setEmail("test@test.test");
        authRegModel.setImei("test");

        User user = userService.createUser(authRegModel);

        userService.createSession(user);

    }

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
    public void UnauthorizedOnWrongCredentials() throws Exception {



        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("test");
        model.setPassWord("test2");
        ObjectMapper mapper = new ObjectMapper();

        /*given(authController.Authenticate(model))
                .willReturn(
                        ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ResponseModel.CreateFailModel("Unauthorized",401)));*/


        String obj = mapper.writeValueAsString(model);
        System.out.println(obj);
        mockMvc.perform(post("/Auth")
                .content(obj)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void AuthorizedOnCorrectCredentials() throws Exception {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("test");
        model.setPassWord("test");
        ObjectMapper mapper = new ObjectMapper();
        String obj = mapper.writeValueAsString(model);

        mockMvc.perform(post("/Auth")
                .content(obj)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(200)))
                .andReturn();

    }

    @Test
    public void BadRequestOnInsiffucientCredentials() throws Exception {
        AuthRegisterModel model = new AuthRegisterModel();
        ObjectMapper mapper = new ObjectMapper();
        String obj = mapper.writeValueAsString(model);

        mockMvc.perform(post("/Auth")
                .content(obj)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.state",is(400)))
                .andReturn();

    }


}
