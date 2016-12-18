package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.QuestModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Xrysa on 18/12/2016.
 */
@Transactional
public class GameServiceTest extends AbstractServiceTest {

    public QuestModel generateQuest(LatLng location) {
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

    @Before
    @Override
    public void setUp() throws TalosQuestsException {
        super.setUp();

        QuestModel model = generateQuest(new LatLng(41.089794, 23.551346));
        questRepository.save(model);
        model = generateQuest(new LatLng(41.091177, 23.551174));
        questRepository.save(model);
        model = generateQuest(new LatLng(40.633650, 22.948569));
        questRepository.save(model);

        testUserWithSession.setLastLocation(new LatLng(41.089798, 23.551346));

        game = gameService.create(testUserWithSession);

    }

    @Test
    public void testCreateGameOnCorrectLocation() throws TalosQuestsException {

        assertEquals(game.getIncompleteQuests().size(), 2);
        assertNotNull(game);

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
    public void testDeleteGameOnNullGame() {
        try {
            gameService.delete(null);
            fail("Shouldn't continue");
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteGameSuccess() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, game);
        gameService.delete(game);
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
            gameService.setActiveGame(null, game);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetActiveGameOnWrongUser() throws TalosQuestsNullArgumentException {
        try {
            gameService.setActiveGame(testUserWithoutSession, game);
            fail();
        } catch (TalosQuestsAccessViolationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetActiveGameOnCorrectUser() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, game);
        assertEquals(testUserWithSession.getActiveGame(), game);
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
        assertNotNull(gameService.getGameById(game.getId()));
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
        gameService.load(testUserWithSession, game.getId());
        assertEquals(testUserWithSession.getActiveGame(), game);
    }

    @Test
    public void testLoadOnCorrectIdAndIncorrectUser() throws TalosQuestsException {
        try {
            gameService.load(testUserWithoutSession, game.getId());
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
        gameService.setActiveGame(testUserWithSession, game);
        assertEquals(gameService.getActiveGameByUser(testUserWithSession), game);
    }

    @Test
    public void testGetActiveQuestOnNullGame() {
        assertNull(gameService.getActiveQuest(null));
    }

    @Test
    public void testGetActiveQuestOnNotNullGame() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, game);
        gameService.getNextQuest(game);

        assertNotNull(gameService.getActiveQuest(game));
    }


    @Test
    public void testSubmitQuestAnswerWhenQuestChoiceIsNull() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, game);
        gameService.getNextQuest(game);

        assertFalse(gameService.submitQuestAnswer(game, null));
    }

    @Test
    public void testSubmitQuestAnswerWhenGameIsNull() {
        assertFalse(gameService.submitQuestAnswer(null, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuestChoiceArePresentButQuestChoiceIsIncorrect() {
        assertFalse(gameService.submitQuestAnswer(game, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuesChoiceArePresentButGameHasNotAnyActiveQuest() {
        game.setActiveQuest(null);
        assertFalse(gameService.submitQuestAnswer(game, new QuestChoice()));
    }

    @Test
    public void testSubmitQuestWhenGameAndQuestChoiceArePresentAndQuestChoiceIsCorrect() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        gameService.setActiveGame(testUserWithSession, game);
        gameService.getNextQuest(game);

        assertTrue(gameService.submitQuestAnswer(game, game.getActiveQuest().getQuest().getCorrectChoice()));
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
            gameService.finishQuest(game, null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testFinishQuestSuccess() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {

        gameService.setActiveGame(testUserWithSession, game);
        gameService.getNextQuest(game);

        Quest quest = game.getActiveQuest();

        gameService.finishQuest(game, true);

        assertTrue(game.getCompletedQuests().contains(quest));
        assertFalse(game.getIncompleteQuests().contains(quest));
        assertFalse(quest.getActive());
        assertNull(game.getActiveQuest());
        assertTrue(quest.getSucceed());
    }

    @Test
    public void testCheckGameStateWhenGameIsNull() {
        assertNull(gameService.checkGameState(null));
    }

    @Test
    public void testCheckGameStateWhenIncompleteQuestsIsNotEmpty() {
        assertFalse(gameService.checkGameState(game));
    }

    @Test
    public void testCheckGameStateWhenIncompleteQuestsIsEmpty() {
        game.getIncompleteQuests().clear();
        assertTrue(gameService.checkGameState(game));
    }

    @Test
    public void testGetNextQuestWhenGameIsNull() {
        try {
            gameService.getNextQuest(null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetNextQuestWhenActiveQuestIsNull() throws TalosQuestsNullArgumentException {
        assertNull(game.getActiveQuest());

        Quest quest = gameService.getNextQuest(game);

        assertNotNull(game.getActiveQuest());
    }

    @Test
    public void testGetNextQuestWhenActiveQuestIsNotNull() throws TalosQuestsNullArgumentException {
        gameService.getNextQuest(game);

        Quest quest1 = game.getActiveQuest();
        assertNotNull(quest1);

        gameService.getNextQuest(game);

        assertNotNull(game.getActiveQuest());

        assertNotEquals(game.getActiveQuest(), quest1);

    }

    @Test
    public void testGetNextQuestWhenNoMoreQuestsAreAvailable() throws TalosQuestsNullArgumentException {
        game.getIncompleteQuests().clear();

        assertNull(gameService.getNextQuest(game));

        assertNull(game.getActiveQuest());
    }

    @Test
    public void testWipeMustClearDatabse() {
        gameService.wipe();

        assertEquals(gameService.getAllGames().size(), 0);
    }

}
