package gr.devian.talosquests.backend;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Services.UserService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Nikolas on 5/12/2016.
 */
public abstract class AbstractUserControllerTest extends AbstractControllerTest {

    @Autowired
    protected UserService userService;

    private SecureRandom random = new SecureRandom();

    protected AuthRegisterModel registeredUserModel;
    protected AuthRegisterModel usernameNotMatchesRequirementsModel;
    protected AuthRegisterModel passwordNotMatchesRequirementsModel;
    protected AuthRegisterModel emailNotMatchesRequirementsModel;
    protected AuthRegisterModel imeiNotMatchesRequirementsModel;
    protected AuthRegisterModel nonExistentUserModel;
    protected AuthRegisterModel insufficientDataModel;

    protected User user;
    protected Session session;

    @Before
    public void setUp() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserData {
        super.setUp();

        userService.wipe();


        registeredUserModel = new AuthRegisterModel("TestUser", "Test1*2^3%1#2@3!", "test@test.gr", "012345678912345");

        usernameNotMatchesRequirementsModel = new AuthRegisterModel("T3#$/s", "Test123123!", "test@test.test", "111111111111111");
        passwordNotMatchesRequirementsModel = new AuthRegisterModel("Test1" + new BigInteger(130, random).toString(10), "test", "test@test.test", "111111111111111");
        emailNotMatchesRequirementsModel = new AuthRegisterModel("Test2" + new BigInteger(130, random).toString(10), "Test123123!", "test", "111111111111111");
        imeiNotMatchesRequirementsModel = new AuthRegisterModel("Test3" + new BigInteger(130, random).toString(10), "Test123123!", "test@test.test", "111111");
        nonExistentUserModel = new AuthRegisterModel("TestUserDot", "Test_#12545#$^", "test2@test.test", "012345678912345");

        insufficientDataModel = new AuthRegisterModel();

        user = userService.createUser(registeredUserModel);

        session = userService.createSession(user);

    }
}
