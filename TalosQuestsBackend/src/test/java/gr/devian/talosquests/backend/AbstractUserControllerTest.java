package gr.devian.talosquests.backend;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.*;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Services.GameService;
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

    @Autowired
    protected GameService gameService;

    @Autowired
    protected QuestRepository questRepository;

    private SecureRandom random = new SecureRandom();

    protected AuthRegisterModel registeredUserModel;
    protected AuthRegisterModel registeredUserModel2;
    protected AuthRegisterModel usernameNotMatchesRequirementsModel;
    protected AuthRegisterModel passwordNotMatchesRequirementsModel;
    protected AuthRegisterModel emailNotMatchesRequirementsModel;
    protected AuthRegisterModel imeiNotMatchesRequirementsModel;
    protected AuthRegisterModel nonExistentUserModel;
    protected AuthRegisterModel insufficientDataModel;

    protected User user;
    protected User user2;
    protected Session session;
    protected Game game;
    protected Game game2;


    @Before
    public void setUp() throws TalosQuestsException {
        super.setUp();

        //userService.wipe();


        registeredUserModel = new AuthRegisterModel("TestUser", "Test1*2^3%1#2@3!", "test@test.gr", "012345678912345");
        registeredUserModel2 = new AuthRegisterModel("TestUser2", "Test1*2^3%1#2@3!", "testa@tesat.gr", "012345278912345");


        usernameNotMatchesRequirementsModel = new AuthRegisterModel("T3#$/s", "Test123123!", "test@test.test", "111111111111111");
        passwordNotMatchesRequirementsModel = new AuthRegisterModel("Test1" + new BigInteger(130, random).toString(10), "test", "test@test.test", "111111111111111");
        emailNotMatchesRequirementsModel = new AuthRegisterModel("Test2" + new BigInteger(130, random).toString(10), "Test123123!", "test", "111111111111111");
        imeiNotMatchesRequirementsModel = new AuthRegisterModel("Test3" + new BigInteger(130, random).toString(10), "Test123123!", "test@test.test", "111111");
        nonExistentUserModel = new AuthRegisterModel("TestUserDot", "Test_#12545#$^", "test2@test.test", "012345678912345");

        insufficientDataModel = new AuthRegisterModel();
        LatLng userLatLng = new LatLng(41.089798, 23.551346);

        LatLng latLng1 = new LatLng(41.089794, 23.551346);
        LatLng latLng2 = new LatLng(41.091177, 23.551174);
        LatLng latLng3 = new LatLng(40.633650, 22.948569);

        QuestModel questModel1 = generateQuest(latLng1);
        questRepository.save(questModel1);

        QuestModel questModel2 = generateQuest(latLng2);
        questRepository.save(questModel2);

        QuestModel questModel3 = generateQuest(latLng3);
        questRepository.save(questModel3);
        user = userService.createUser(registeredUserModel);
        user.setLastLocation(new LatLng(41.089798, 23.551346));

        user2 = userService.createUser(registeredUserModel2);
        user2.setLastLocation(new LatLng(41.089798, 23.551346));

        game = gameService.create(user);
        game2 = gameService.create(user2);

        session = userService.createSession(user);

    }
}
