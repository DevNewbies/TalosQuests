package gr.devian.talosquests.backend;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.Factories.QuestFactory;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.*;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Services.GameService;
import gr.devian.talosquests.backend.Services.LocationService;
import gr.devian.talosquests.backend.Services.UserService;
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
    protected LatLng testLocationThessaloniki1;
    protected LatLng testLocationInvalid;

    protected QuestModel testQuestModelSerres1;
    protected QuestModel testQuestModelSerres2;
    protected QuestModel testQuestModelThessaloniki1;

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
    public void setUp() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserData, TalosQuestsException, JsonProcessingException {

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


        testLocationSerres1 = new LatLng(41.089798, 23.551346);
        testLocationSerres2 = new LatLng(41.089794, 23.551346);
        testLocationSerres3 = new LatLng(41.091177, 23.551174);
        testLocationThessaloniki1 = new LatLng(40.633650, 22.948569);
        testLocationInvalid = new LatLng();


        testQuestModelSerres1 = generateQuest(testLocationSerres2);
        testQuestModelSerres2 = generateQuest(testLocationSerres3);
        testQuestModelThessaloniki1 = generateQuest(testLocationThessaloniki1);

        questRepository.save(testQuestModelSerres1);
        questRepository.save(testQuestModelSerres2);
        questRepository.save(testQuestModelThessaloniki1);


        testUserWithSession = userService.createUser(testAuthRegisterModelCreatedWithSession);
        testUserWithoutSession = userService.createUser(testAuthRegisterModelCreatedWithoutSession);

        testSession = userService.createSession(testUserWithSession);

        testGameForUserWithSession = createMockedGame(testUserWithSession);
        testGameForUserWithoutSession = createMockedGame(testUserWithoutSession);
        when(mockedGameService.create(testUserWithSession)).thenReturn(testGameForUserWithSession);
        when(mockedGameService.create(testUserWithoutSession)).thenReturn(testGameForUserWithoutSession);

        testGameForUserWithSession = mockedGameService.create(testUserWithSession);
        testGameForUserWithoutSession = mockedGameService.create(testUserWithoutSession);

    }

    private Game createMockedGame(User user) throws TalosQuestsLocationServiceUnavailableException {
        Game game;
        LatLng userLatLng = testLocationSerres1;

        user.setLastLocation(userLatLng);

        ArrayList<Quest> quests = new ArrayList<>();

        for (QuestModel q : questRepository.findAll()) {
            Quest t = new Quest();
            t.setLocation(q.getLocation());
            t.setQuest(q);
            quests.add(t);
        }

        ArrayList<Quest> quests2 = new ArrayList<>();

        quests2.add(quests.get(0));
        quests2.add(quests.get(1));

        when(questFactory.fetchQuestsFromDatabase()).thenReturn(quests);
        when(locationService.getQuestsInRadius(userLatLng, quests, 10000)).thenReturn(quests2);

        game = new Game();
        game.setUser(user);
        game.setCurrentUserLocation(userLatLng);

        questFactory.setLocation(userLatLng);

        game.setIncompleteQuests(locationService.getQuestsInRadius(userLatLng, quests, 10000));
        for (Quest quest : game.getIncompleteQuests()) {
            userQuestRepository.save(quest);
        }

        gameRepository.save(game);

        user.addGame(game);
        userRepository.save(user);
        return game;
    }

    private QuestModel generateQuest(LatLng location) {
        QuestModel q = new QuestModel();
        q.setLocation(location);
        q.setContent(new BigInteger(130, random).toString(50));
        q.setName(new BigInteger(130, random).toString(5));
        QuestChoice c;
        for (int i = 0; i <= 5; i++) {
            c = new QuestChoice();
            c.setContent(new BigInteger(130, random).toString(10));
            q.getAvailableChoices().add(c);
            q.setCorrectChoice(c);
        }
        return q;
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
}
