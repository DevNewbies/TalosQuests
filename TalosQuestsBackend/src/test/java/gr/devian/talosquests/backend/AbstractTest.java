package gr.devian.talosquests.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Factories.QuestFactory;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.*;
import gr.devian.talosquests.backend.Repositories.*;
import gr.devian.talosquests.backend.Services.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

/**
 * Created by Nikolas on 5/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected SecureRandom random = new SecureRandom();

    @Autowired
    protected UserService userService;

    @Autowired
    protected GameService gameService;

    @Autowired
    protected  AccessService accessService;

    @Autowired
    protected QuestService questService;

    @Autowired
    protected SessionService sessionService;

    @Autowired
    protected AccessRepository accessRepository;

    @Autowired
    protected UserQuestRepository userQuestRepository;

    @Autowired
    protected QuestRepository questRepository;

    @Autowired
    protected GameRepository gameRepository;

    @Autowired
    protected UserRepository userRepository;

    @Mock
    protected GameService mockedGameService;

    @Mock
    protected LocationService locationService;

    @Mock
    protected QuestFactory questFactory;

    protected AuthRegisterModel testAuthRegisterModelCreatedWithSession;
    protected AuthRegisterModel testAuthRegisterModelCreatedWithoutSession;
    protected AuthRegisterModel testAuthRegisterModelNotCreated;
    protected AuthRegisterModel testAuthRegisterModelNotMatchingUsernameRequiredment;
    protected AuthRegisterModel testAuthRegisterModelNotMatchingPasswordRequiredment;
    protected AuthRegisterModel testAuthRegisterModelNotMatchingEmailRequiredment;
    protected AuthRegisterModel testAuthRegisterModelNotMatchingImeiRequiredment;


    protected User testUserWithSession;
    protected User testUserWithoutSession;

    protected LatLng testLocationSerres1;
    protected LatLng testLocationSerres2;
    protected LatLng testLocationSerres3;
    protected LatLng testLocationSerres4;
    protected LatLng testLocationSerres5;
    protected LatLng testLocationSerres6;
    protected LatLng testLocationThessaloniki1;
    protected LatLng testLocationInvalid;
    protected LatLng testLocationAthens1;

    protected Quest testQuestSerres1;
    protected Quest testQuestSerres2;
    protected Quest testQuestSerres3;
    protected Quest testQuestSerres4;
    protected Quest testQuestSerres5;
    protected Quest testQuestThessaloniki1;


    protected Session testSession;

    protected String userNamePassing;
    protected String userNameFailing;
    protected String passWordPassing;
    protected String passWordFailing;
    protected String imeiPassing;
    protected String imeiFailing;

    protected Game testGameForUserWithSession;
    protected Game testGameForUserWithoutSession;



    @Before
    public void setUp() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException, TalosQuestsException, JsonProcessingException {

        MockitoAnnotations.initMocks(this);

        userService.wipe();

        questRepository.deleteAllInBatch();

        passWordPassing = "Test123!@#";
        passWordFailing = "Test";

        imeiPassing = "012345678912345";
        imeiFailing = "asdagadsgadhdaa";

        userNamePassing = "TestUser";
        userNameFailing = "T$#%A/s";

        testAuthRegisterModelCreatedWithSession = new AuthRegisterModel(
                userNamePassing + "_withSession",
                passWordPassing,
                userNamePassing + "_wi@test.gr",
                imeiPassing);
        testAuthRegisterModelCreatedWithoutSession = new AuthRegisterModel(
                userNamePassing + "_withoutSession",
                passWordPassing,
                userNamePassing + "_wo@test.gr",
                imeiPassing);
        testAuthRegisterModelNotCreated = new AuthRegisterModel(
                userNamePassing + "_notRegistered",
                passWordPassing,
                userNamePassing + "_ur@test.gr",
                imeiPassing);
        testAuthRegisterModelNotMatchingUsernameRequiredment = new AuthRegisterModel(
                userNameFailing,
                passWordPassing,
                userNamePassing + "_nu@test.gr",
                imeiPassing);
        testAuthRegisterModelNotMatchingPasswordRequiredment = new AuthRegisterModel(
                userNamePassing + "_notRegistered",
                passWordFailing,
                userNamePassing + "_np@test.gr",
                imeiPassing);
        testAuthRegisterModelNotMatchingEmailRequiredment = new AuthRegisterModel(
                userNamePassing + "_notRegistered",
                passWordPassing,
                userNamePassing + "_ne.test.gr",
                imeiPassing);
        testAuthRegisterModelNotMatchingImeiRequiredment = new AuthRegisterModel(
                userNamePassing + "_notRegistered",
                passWordPassing,
                userNamePassing + "_ni@test.gr",
                imeiFailing);


        testLocationSerres1 = new LatLng(41.089065, 23.548957);
        testLocationSerres2 = new LatLng(41.090251, 23.549512);
        testLocationSerres3 = new LatLng(41.089588, 23.547068);
        testLocationSerres4 = new LatLng(41.089654, 23.549315);
        testLocationSerres5 = new LatLng(41.089276, 23.548158);
        testLocationSerres6 = new LatLng(41.088757, 23.541529);
        testLocationThessaloniki1 = new LatLng(40.633650, 22.948569);
        testLocationAthens1 = new LatLng(37.978637, 23.739960);
        testLocationInvalid = new LatLng();


        testQuestSerres1 = generateQuest(testLocationSerres2);
        testQuestSerres2 = generateQuest(testLocationSerres3);
        testQuestSerres3 = generateQuest(testLocationSerres4);
        testQuestSerres4 = generateQuest(testLocationSerres5);
        testQuestSerres5 = generateQuest(testLocationSerres6);
        testQuestThessaloniki1 = generateQuest(testLocationThessaloniki1);

        testUserWithSession = userService.create(testAuthRegisterModelCreatedWithSession);
        testUserWithoutSession = userService.create(testAuthRegisterModelCreatedWithoutSession);

        testSession = sessionService.create(testUserWithSession);

        testGameForUserWithSession = createMockedGame(testUserWithSession);
        testGameForUserWithoutSession = createMockedGame(testUserWithoutSession);
        when(mockedGameService.create(testUserWithSession)).thenReturn(testGameForUserWithSession);
        when(mockedGameService.create(testUserWithoutSession)).thenReturn(testGameForUserWithoutSession);

        testGameForUserWithSession = mockedGameService.create(testUserWithSession);
        testGameForUserWithoutSession = mockedGameService.create(testUserWithoutSession);
        LocationService.enableService = true;
    }

    private Game createMockedGame(User user) throws TalosQuestsLocationServiceUnavailableException, TalosQuestsNullArgumentException, TalosQuestsLocationsNotAvailableException {
        Game game;
        LatLng userLatLng = testLocationSerres1;

        userService.setActiveLocation(user,userLatLng);

        ArrayList<UserQuest> userQuests = new ArrayList<>();

        for (Quest q : questRepository.findAll()) {
            UserQuest t = new UserQuest();
            t.setLocation(q.getLocation());
            t.setQuest(q);
            userQuests.add(t);
        }

        ArrayList<UserQuest> quests2 = new ArrayList<>();

        quests2.add(userQuests.get(0));
        quests2.add(userQuests.get(1));
        quests2.add(userQuests.get(2));
        quests2.add(userQuests.get(3));
        userQuestRepository.save(userQuests.get(4));

        when(questFactory.fetchQuestsFromDatabase()).thenReturn(userQuests);
        when(locationService.getQuestsInRadius(userLatLng, userQuests, 10000)).thenReturn(quests2);

        game = new Game();
        game.setUser(user);
        game.setCurrentUserLocation(testLocationSerres2);
        game.setCurrentUserLocation(testLocationSerres1);

        questFactory.setLocation(userLatLng);

        game.setIncompleteUserQuests(locationService.getQuestsInRadius(userLatLng, userQuests, 10000));
        game.getCompletedUserQuests().add(userQuests.get(2));
        for (UserQuest userQuest : game.getIncompleteUserQuests()) {
            userQuestRepository.save(userQuest);
        }

        gameRepository.save(game);

        user.addGame(game);
        userRepository.save(user);
        return game;
    }

    protected Quest generateQuest(LatLng location) {
        Quest q = new Quest();
        q.setLocation(location);
        q.setContent(new BigInteger(130, random).toString(50));
        q.setName(new BigInteger(130, random).toString(5));
        q.setExp(random.nextInt(100) + 50);
        QuestChoice c;
        for (int i = 0; i <= 10; i++) {
            if (i % 2 == 0) {
                c = new QuestChoice();
                c.setContent(new BigInteger(130, random).toString(10));
            } else
                c = new QuestChoice(new BigInteger(130, random).toString(10));
            q.getAvailableChoices().add(c);
            q.setCorrectChoice(c);
        }

        return questRepository.save(q);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
}
