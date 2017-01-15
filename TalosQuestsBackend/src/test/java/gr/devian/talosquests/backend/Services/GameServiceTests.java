package gr.devian.talosquests.backend.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.UserQuest;
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
public class GameServiceTests extends AbstractServiceTest {


    @Test
    public void testCreateGameOnCorrectLocation() throws TalosQuestsException {

        try {
            userService.setActiveLocation(testUserWithoutSession, testLocationSerres1);
            testGameForUserWithSession = gameService.create(testUserWithoutSession);
            assertNotNull(testGameForUserWithSession);
            assertEquals(testGameForUserWithSession.getIncompleteUserQuests().size(), 5);

        } catch (TalosQuestsLocationServiceUnavailableException e) {
            assumeTrue(true);
        }
    }



    @Test(expected = TalosQuestsLocationsNotAvailableException.class)
    public void testCreateGameWhenNoQuestsAreAvailableOnUsersArea() throws TalosQuestsException, JsonProcessingException {
        userService.setActiveLocation(testUserWithoutSession, testLocationAthens1);
        testGameForUserWithSession = gameService.create(testUserWithoutSession);
    }

    @Test(expected = TalosQuestsLocationsNotAvailableException.class)
    public void testCreateGameWhenNoQuestsAreAvailableOnDatabase() throws TalosQuestsException {
        gameService.wipe();
        questRepository.deleteAll();

        userService.setActiveLocation(testUserWithoutSession, testLocationAthens1);
        testGameForUserWithSession = gameService.create(testUserWithoutSession);
    }

    @Test(expected = TalosQuestsLocationServiceUnavailableException.class)
    public void testCreateGameOnCorrectLocationWhenLocationServiceIsUnavailable() throws TalosQuestsException {
        LocationService.enableService = false;
        userService.setActiveLocation(testUserWithoutSession, testLocationSerres1);
        testGameForUserWithSession = gameService.create(testUserWithoutSession);
    }

    @Test(expected = TalosQuestsLocationNotProvidedException.class)
    public void testCreateGameOnNullLocation() throws TalosQuestsException {
        testUserWithSession.setLastLocation(null);
        gameService.create(testUserWithSession);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCreateGameOnNullUser() throws TalosQuestsException {
        gameService.create(null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteGameOnNullGame() throws TalosQuestsException {
        gameService.delete((User) null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteWhenOriginIsNull() throws TalosQuestsException {
        gameService.delete(null, testUserWithSession);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteWhenTargetIsNull() throws TalosQuestsException {
        gameService.delete(testUserWithSession, (User) null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteWhenGameIsNull() throws TalosQuestsException {
        gameService.delete(testUserWithSession, (Game) null);
    }

    @Test
    public void testDeleteSuccessWhenOriginAndTargetUserProvided() throws TalosQuestsException {
        gameService.delete(testUserWithSession, testUserWithSession);
        assertEquals(testUserWithSession.getGames().size(), 0);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testDeleteSuccessWhenOriginHasNoPremissionDeleteOtherUsersData() throws TalosQuestsException {
        gameService.delete(testUserWithSession, testUserWithoutSession);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testDeleteSuccessWhenOriginHasNoPremissionDeleteOwnData() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageOwnData(false);
        gameService.delete(testUserWithSession, testUserWithSession);
    }

    @Test
    public void testDeleteGameSuccessUsingOriginAndTarget() throws TalosQuestsException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.delete(testGameForUserWithSession);
        assertTrue(true);
    }

    @Test
    public void testDeleteGameSuccessUsingTarget() throws TalosQuestsException {
        gameService.delete(testUserWithoutSession);
        assertTrue(true);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetActiveGameOnNullGame() throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        gameService.setActiveGame(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetActiveGameOnNullUser() throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        gameService.setActiveGame(null, testGameForUserWithSession);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testSetActiveGameOnWrongUser() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithoutSession, testGameForUserWithSession);
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

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testLoadOnNullId() throws TalosQuestsException {
        gameService.load(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testLoadOnNullUser() throws TalosQuestsException {
        gameService.load(null, null);
    }

    @Test(expected = TalosQuestsException.class)
    public void testLoadOnInvalidId() throws TalosQuestsException {
        gameService.load(testUserWithSession, Long.MAX_VALUE);
    }

    @Test
    public void testLoadOnCorrectIdAndCorrectUser() throws TalosQuestsException {
        gameService.load(testUserWithSession, testGameForUserWithSession.getId());
        assertEquals(testUserWithSession.getActiveGame(), testGameForUserWithSession);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testLoadOnCorrectIdAndIncorrectUser() throws TalosQuestsException {
        gameService.load(testUserWithoutSession, testGameForUserWithSession.getId());
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
        testGameForUserWithSession.setActiveUserQuest(null);
        assertFalse(gameService.submitQuestAnswer(testGameForUserWithSession, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuestChoiceArePresentAndQuestChoiceIsCorrect() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);

        assertTrue(gameService.submitQuestAnswer(testGameForUserWithSession, testGameForUserWithSession.getActiveUserQuest().getQuest().getCorrectChoice()));
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testFinishQuestWhenGameIsNull() throws TalosQuestsNullArgumentException {
        gameService.finishQuest(null, true);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testFinishQuestWhenStateIsNull() throws TalosQuestsNullArgumentException {
        gameService.finishQuest(testGameForUserWithSession, null);
    }

    @Test
    public void testFinishQuestSuccess() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {

        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);

        UserQuest userQuest = testGameForUserWithSession.getActiveUserQuest();

        gameService.finishQuest(testGameForUserWithSession, true);

        assertTrue(testGameForUserWithSession.getCompletedUserQuests().contains(userQuest));
        assertFalse(testGameForUserWithSession.getIncompleteUserQuests().contains(userQuest));
        assertFalse(userQuest.getActive());
        assertNull(testGameForUserWithSession.getActiveUserQuest());
        assertTrue(userQuest.getSucceed());
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
        testGameForUserWithSession.getIncompleteUserQuests().clear();
        assertTrue(gameService.checkGameState(testGameForUserWithSession));
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testGetNextQuestWhenGameIsNull() throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException, TalosQuestsNullArgumentException {
        gameService.getNextQuest(null);
    }

    @Test
    public void testGetNextQuestWhenActiveQuestIsNull() throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        assertNull(testGameForUserWithSession.getActiveUserQuest());

        UserQuest userQuest = gameService.getNextQuest(testGameForUserWithSession);

        assertNotNull(testGameForUserWithSession.getActiveUserQuest());
    }

    @Test
    public void testGetNextQuestWhenActiveQuestIsNotNull() throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        gameService.getNextQuest(testGameForUserWithSession);

        UserQuest userQuest1 = testGameForUserWithSession.getActiveUserQuest();
        assertNotNull(userQuest1);

        gameService.getNextQuest(testGameForUserWithSession);

        assertNotNull(testGameForUserWithSession.getActiveUserQuest());

        assertNotEquals(testGameForUserWithSession.getActiveUserQuest(), userQuest1);

    }

    @Test(expected = TalosQuestsLocationsNotAvailableException.class)
    public void testGetNextQuestWhenNoMoreQuestsAreAvailable() throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        testGameForUserWithSession.getIncompleteUserQuests().clear();
        gameService.getNextQuest(testGameForUserWithSession);
    }


    @Test
    public void testWipeMustClearDatabase() throws TalosQuestsNullArgumentException {
        gameService.wipe();
        assertEquals(gameService.getAllGames().size(), 0);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testWipeWhenUserHasNoAccess() throws TalosQuestsException {
        gameService.wipe(testUserWithSession);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testWipeWhenUserIsNull() throws TalosQuestsException {
        gameService.wipe(null);
    }

    @Test
    public void testWipeWhenUserHasPermission() throws TalosQuestsException {

        testUserWithSession.getAccess().setCanWipeGames(true);
        gameService.wipe(testUserWithSession);
        assertTrue(true);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSaveWhenGameIsNull() throws TalosQuestsNullArgumentException {
        gameService.save(null);
    }

}
