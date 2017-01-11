package gr.devian.talosquests.backend.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.User;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by Xrysa on 18/12/2016.
 */
@Transactional
public class GameServiceTest extends AbstractServiceTest {


    @Test
    public void testCreateGameOnCorrectLocation() throws TalosQuestsException {

        try {
            userService.setActiveLocation(testUserWithoutSession, testLocationSerres1);
            testGameForUserWithSession = gameService.create(testUserWithoutSession);
            assertNotNull(testGameForUserWithSession);
            assertEquals(testGameForUserWithSession.getIncompleteQuests().size(), 5);

        } catch (TalosQuestsLocationServiceUnavailableException e) {
            assumeTrue(true);
        }
    }
    @Test
    public void testCreateGameWhenNoQuestsAreAvailableOnUsersArea() throws TalosQuestsException, JsonProcessingException {
        try {
            userService.setActiveLocation(testUserWithoutSession, testLocationAthens1);
            testGameForUserWithSession = gameService.create(testUserWithoutSession);
            logger.warn(mapToJson(testGameForUserWithSession));
            fail();
        } catch (TalosQuestsLocationsNotAvailableException e) {
            assertTrue(true);
        }
    }
    @Test
    public void testCreateGameWhenNoQuestsAreAvailableOnDatabase() throws TalosQuestsException {
        gameService.wipe();
        questRepository.deleteAll();
        try {
            userService.setActiveLocation(testUserWithoutSession, testLocationAthens1);
            testGameForUserWithSession = gameService.create(testUserWithoutSession);
            fail();
        } catch (TalosQuestsLocationsNotAvailableException e) {
            assertTrue(true);
        }
    }
    @Test
    public void testCreateGameOnCorrectLocationWhenLocationServiceIsUnavailable() throws TalosQuestsException {
        LocationService.enableService = false;
        try {
            userService.setActiveLocation(testUserWithoutSession, testLocationSerres1);
            testGameForUserWithSession = gameService.create(testUserWithoutSession);
            fail();

        } catch (TalosQuestsLocationServiceUnavailableException e) {
            assertTrue(true);

        }
        LocationService.enableService = true;
    }

    @Test
    public void testCreateGameOnNullLocation() throws TalosQuestsException {
        testUserWithSession.setLastLocation(null);

        Game bla = null;
        try {
            bla = gameService.create(testUserWithSession);
            fail("Shouldn't continue");
        } catch (TalosQuestsLocationNotProvidedException e) {
            assertTrue(true);
        }
        assertNull(bla);
    }

    @Test
    public void testCreateGameOnNullUser() throws TalosQuestsException {
        Game bla = null;
        try {
            bla = gameService.create(null);
            fail("Shouldn't continue");
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
        assertNull(bla);
    }

    @Test
    public void testDeleteGameOnNullGame() throws TalosQuestsException {
        try {
            gameService.delete((User)null);
            fail("Shouldn't continue");
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteGameSuccess() throws TalosQuestsException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.delete(testGameForUserWithSession);
        assertTrue(true);
    }

    @Test
    public void testSetActiveGameOnNullGame() throws TalosQuestsAccessViolationException {
        try {
            gameService.setActiveGame(testUserWithSession, null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetActiveGameOnNullUser() throws TalosQuestsAccessViolationException {
        try {
            gameService.setActiveGame(null, testGameForUserWithSession);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetActiveGameOnWrongUser() throws TalosQuestsNullArgumentException {
        try {
            gameService.setActiveGame(testUserWithoutSession, testGameForUserWithSession);
            fail();
        } catch (TalosQuestsAccessViolationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetActiveGameOnCorrectUser() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        assertEquals(testUserWithSession.getActiveGame(), testGameForUserWithSession);
    }

    @Test
    public void testGetGameByIdOnNullId() {
        assertNull(gameService.getGameById(null));
    }

    @Test
    public void testGetGameByIdOnWrongId() {
        assertNull(gameService.getGameById(-1L));
    }

    @Test
    public void testGetGameByIdOnCorrectId() {
        assertNotNull(gameService.getGameById(testGameForUserWithSession.getId()));
    }

    @Test
    public void testLoadOnNullId() {
        try {
            gameService.load(testUserWithSession, null);
            fail();
        } catch (TalosQuestsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLoadOnCorrectIdAndCorrectUser() throws TalosQuestsException {
        gameService.load(testUserWithSession, testGameForUserWithSession.getId());
        assertEquals(testUserWithSession.getActiveGame(), testGameForUserWithSession);
    }

    @Test
    public void testLoadOnCorrectIdAndIncorrectUser() throws TalosQuestsException {
        try {
            gameService.load(testUserWithoutSession, testGameForUserWithSession.getId());
            fail();
        } catch (TalosQuestsAccessViolationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetActiveGameByUserOnNullUser() {
        assertNull(gameService.getActiveGameByUser(null));
    }

    @Test
    public void testGetActiveGameByUserWhenUserDoNotHaveActiveGame() {
        assertNull(gameService.getActiveGameByUser(testUserWithSession));
    }

    @Test
    public void testGetActiveGameByUserWhenUserHasActiveGame() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        assertEquals(gameService.getActiveGameByUser(testUserWithSession), testGameForUserWithSession);
    }

    @Test
    public void testGetActiveQuestOnNullGame() {
        assertNull(gameService.getActiveQuest(null));
    }

    @Test
    public void testGetActiveQuestOnNotNullGame() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);

        assertNotNull(gameService.getActiveQuest(testGameForUserWithSession));
    }


    @Test
    public void testSubmitQuestAnswerWhenQuestChoiceIsNull() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);

        assertFalse(gameService.submitQuestAnswer(testGameForUserWithSession, null));
    }

    @Test
    public void testSubmitQuestAnswerWhenGameIsNull() {
        assertFalse(gameService.submitQuestAnswer(null, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuestChoiceArePresentButQuestChoiceIsIncorrect() {
        assertFalse(gameService.submitQuestAnswer(testGameForUserWithSession, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuesChoiceArePresentButGameHasNotAnyActiveQuest() {
        testGameForUserWithSession.setActiveQuest(null);
        assertFalse(gameService.submitQuestAnswer(testGameForUserWithSession, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuestChoiceArePresentAndQuestChoiceIsCorrect() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);

        assertTrue(gameService.submitQuestAnswer(testGameForUserWithSession, testGameForUserWithSession.getActiveQuest().getQuest().getCorrectChoice()));
    }

    @Test
    public void testFinishQuestWhenGameIsNull() {
        try {
            gameService.finishQuest(null, true);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testFinishQuestWhenStateIsNull() {
        try {
            gameService.finishQuest(testGameForUserWithSession, null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testFinishQuestSuccess() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {

        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);

        Quest quest = testGameForUserWithSession.getActiveQuest();

        gameService.finishQuest(testGameForUserWithSession, true);

        assertTrue(testGameForUserWithSession.getCompletedQuests().contains(quest));
        assertFalse(testGameForUserWithSession.getIncompleteQuests().contains(quest));
        assertFalse(quest.getActive());
        assertNull(testGameForUserWithSession.getActiveQuest());
        assertTrue(quest.getSucceed());
    }

    @Test
    public void testCheckGameStateWhenGameIsNull() {
        assertNull(gameService.checkGameState(null));
    }

    @Test
    public void testCheckGameStateWhenIncompleteQuestsIsNotEmpty() {
        assertFalse(gameService.checkGameState(testGameForUserWithSession));
    }

    @Test
    public void testCheckGameStateWhenIncompleteQuestsIsEmpty() {
        testGameForUserWithSession.getIncompleteQuests().clear();
        assertTrue(gameService.checkGameState(testGameForUserWithSession));
    }

    @Test
    public void testGetNextQuestWhenGameIsNull() throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        try {
            gameService.getNextQuest(null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetNextQuestWhenActiveQuestIsNull() throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        assertNull(testGameForUserWithSession.getActiveQuest());

        Quest quest = gameService.getNextQuest(testGameForUserWithSession);

        assertNotNull(testGameForUserWithSession.getActiveQuest());
    }

    @Test
    public void testGetNextQuestWhenActiveQuestIsNotNull() throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        gameService.getNextQuest(testGameForUserWithSession);

        Quest quest1 = testGameForUserWithSession.getActiveQuest();
        assertNotNull(quest1);

        gameService.getNextQuest(testGameForUserWithSession);

        assertNotNull(testGameForUserWithSession.getActiveQuest());

        assertNotEquals(testGameForUserWithSession.getActiveQuest(), quest1);

    }

    @Test
    public void testGetNextQuestWhenNoMoreQuestsAreAvailable() throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        testGameForUserWithSession.getIncompleteQuests().clear();

        assertNull(gameService.getNextQuest(testGameForUserWithSession));

        assertNull(testGameForUserWithSession.getActiveQuest());
    }


    @Test
    public void testWipeMustClearDatabase() {
        gameService.wipe();

        assertEquals(gameService.getAllGames().size(), 0);
    }

}
