package gr.devian.talosquests.backend;

import gr.devian.talosquests.backend.Controllers.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

import gr.devian.talosquests.backend.Controllers.UserController;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GameClassTesting {

    @MockBean
    private UserController userController;

    @Test
    public void contextLoads() {

        assertTrue(true);

    }

}


