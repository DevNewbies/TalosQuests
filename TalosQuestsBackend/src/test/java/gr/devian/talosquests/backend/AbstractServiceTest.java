package gr.devian.talosquests.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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

    protected User testUserWithSession;
    protected Session testSession;
    protected User testUserWithoutSession;


    protected String userName;
    protected String userNameWithSession;
    protected String userNameWithoutSession;
    protected String token;



    protected Game game;



    @Before
    public void setUp() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserData, TalosQuestsException, JsonProcessingException {

        MockitoAnnotations.initMocks(this);
        //userQuestRepository.deleteAllInBatch();
        //questRepository.deleteAllInBatch();

        gameService.wipe();

        userService.wipe();
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

        testUserWithSession.setLastLocation(userLatLng);

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
        game.setUser(testUserWithSession);
        game.setCurrentUserLocation(userLatLng);

        questFactory.setLocation(userLatLng);

        game.setIncompleteQuests(locationService.getQuestsInRadius(userLatLng, quests, 10000));
        for (Quest quest : game.getIncompleteQuests()) {
            userQuestRepository.save(quest);
        }

        gameRepository.save(game);

        testUserWithSession.addGame(game);
        userRepository.save(testUserWithSession);

        when(mockedGameService.create(testUserWithSession)).thenReturn(game);

        game = mockedGameService.create(testUserWithSession);

    }

}
