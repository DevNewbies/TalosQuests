package gr.devian.talosquests.backend;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import gr.devian.talosquests.backend.Services.GameService;
import gr.devian.talosquests.backend.Services.UserService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

/**
 * Created by Xrysa on 18/12/2016.
 */
public abstract class AbstractServiceTest extends AbstractTest {
    @Autowired
    protected UserService userService;

    @Autowired
    protected GameService gameService;

    @Autowired
    protected UserQuestRepository userQuestRepository;

    @Autowired
    protected QuestRepository questRepository;


    protected User testUserWithSession;
    protected Session testSession;
    protected User testUserWithoutSession;

    protected Game game;

    protected String userName;
    protected String userNameWithSession;
    protected String userNameWithoutSession;
    protected String token;

    protected SecureRandom random = new SecureRandom();

    @Before
    public void setUp() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserData, TalosQuestsException {



        //userQuestRepository.deleteAllInBatch();
        //questRepository.deleteAllInBatch();

        //gameService.wipe();

        //userService.wipe();
        userService.evictCache();

        //Let userName = test_92jf923jf923jg923
        userName = "TestUser";

        //Let userNameWithSession = test_92jf923jf923jg923_withSession
        userNameWithSession = userName + "withSession";

        // AuthRegisterModel
        // -> Username: test_92jf923jf923jg923_withSession
        // -> Password: test
        // -> Email   : test_92jf923jf923jg923_wi@test.gr
        // -> IMEI    : test


        testUserWithSession = userService.createUser(
                new AuthRegisterModel(
                        userNameWithSession,
                        "Test123!!",
                        userName + "_wi@test.gr",
                        "012345678912345"));


        //Let userNameWithSession = test_92jf923jf923jg923_withoutSession
        userNameWithoutSession = userName + "withoutSession";

        // AuthRegisterModel
        // -> Username: test_92jf923jf923jg923_withoutSesison
        // -> Password: test
        // -> Email   : test_92jf923jf923jg923_wo@test.gr
        // -> IMEI    : test

        testUserWithoutSession = userService.createUser(
                new AuthRegisterModel(
                        userNameWithoutSession,
                        "Test123!!",
                        userName + "_wo@test.gr",
                        "012345678912345"));


        testSession = userService.createSession(testUserWithSession);

    }

}
