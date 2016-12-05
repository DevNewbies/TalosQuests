package gr.devian.talosquests.backend;

import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Services.UserService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

/**
 * Created by Nikolas on 5/12/2016.
 */
public abstract class AbstractUserControllerTest extends  AbstractControllerTest {

    @Autowired
    protected UserService userService;

    private SecureRandom random = new SecureRandom();

    protected AuthRegisterModel validModel;
    protected AuthRegisterModel invalidModel;
    protected AuthRegisterModel insufficientDataModel;

    protected User user;
    protected Session session;

    @Before
    public void setUp() {
        super.setUp();

        userService.wipe();


        validModel = new AuthRegisterModel("test","test","test@test.test","test");

        invalidModel = new AuthRegisterModel("test2","test2","test2@test.test","test");

        insufficientDataModel = new AuthRegisterModel();

        user = userService.createUser(validModel);

        session = userService.createSession(user);

    }
}
