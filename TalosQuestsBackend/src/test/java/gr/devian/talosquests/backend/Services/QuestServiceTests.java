package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.UserQuest;
import gr.devian.talosquests.backend.Models.Quest;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

/**
 * Created by Nikolas on 11/1/2017.
 */
@Transactional
public class QuestServiceTests extends AbstractServiceTest {

    @Test
    public void testGetQuestByIdWhenIdIsNull() {
        assertNull(questService.getQuestById(null));
    }

    @Test
    public void testGetQuestByIdWhenIdNotExists() {
        assertNull(questService.getQuestById(Long.MAX_VALUE));
    }

    @Test
    public void testGetQuestByIdWhenIdIsCorrect() {
        assertNotNull(questService.getQuestById(((UserQuest) testGameForUserWithSession.getIncompleteUserQuests().toArray()[0]).getQuest().getId()));
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCreateQuestWhenOriginUserIsNull() throws TalosQuestsException {
        questService.create(null, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCreateQuestWhenModelIsNull() throws TalosQuestsException {
        questService.create(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testCreateQuestWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        questService.create(testUserWithSession, testQuestSerres2);
    }

    @Test
    public void testCreateQuestWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        Quest quest = questService.create(testUserWithSession, testQuestSerres2);
        assertNotNull(quest);
    }


    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteQuestByQuestWhenOriginUserIsNull() throws TalosQuestsException {
        questService.delete(null, (Quest)null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteQuestByQuestWhenModelIsNull() throws TalosQuestsException {
        questService.delete(testUserWithSession, (Quest)null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteQuestByIdWhenOriginUserIsNull() throws TalosQuestsException {
        questService.delete(null, (Long)null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteQuestByIdWhenModelIsNull() throws TalosQuestsException {
        questService.delete(testUserWithSession, (Long)null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testDeleteQuestByIdWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        questService.delete(testUserWithSession, testQuestSerres2.getId());
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testDeleteQuestByQuestWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        questService.delete(testUserWithSession, testQuestSerres2);
    }

    @Test
    public void testDeleteQuestByQuestWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        gameService.getNextQuest(testGameForUserWithSession);
        for (UserQuest userQuest : testGameForUserWithSession.getIncompleteUserQuests()) {
            if (userQuest.getQuest().equals(testQuestSerres3))
                testGameForUserWithSession.setActiveUserQuest(userQuest);
        }
        questService.delete(testUserWithSession, testQuestSerres3);

        assertTrue(true);
    }

    @Test
    public void testDeleteQuestByIdWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        gameService.getNextQuest(testGameForUserWithSession);
        for (UserQuest userQuest : testGameForUserWithSession.getIncompleteUserQuests()) {
            if (userQuest.getQuest().equals(testQuestSerres3))
                testGameForUserWithSession.setActiveUserQuest(userQuest);
        }
        questService.delete(testUserWithSession, testQuestSerres3.getId());

        assertTrue(true);
    }


    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateQuestByQuestWhenOriginUserIsNull() throws TalosQuestsException {
        questService.update(null, (Quest)null, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateQuestByQuestWhenOriginModelIsNull() throws TalosQuestsException {
        questService.update(testUserWithSession, (Quest)null, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateQuestByQuestWhenNewModelIsNull() throws TalosQuestsException {
        questService.update(testUserWithSession, testQuestSerres3, null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testUpdateQuestByQuestWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        questService.update(testUserWithSession, testQuestSerres2, testQuestSerres2);
    }

    @Test
    public void testUpdateQuestByQuestWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        Quest questModel = new Quest();
        questModel.setContent("temp");
        questModel.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        questModel.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        questModel.setLocation(testLocationAthens1);
        questModel.setName("temp");
        questModel.setExp(testQuestSerres2.getExp());

        Quest quest = questService.update(testUserWithSession, testQuestSerres2, questModel);

        assertEquals(quest.getLocation(), questModel.getLocation());
        assertEquals(quest.getName(), questModel.getName());
        assertEquals(quest.getContent(), questModel.getContent());
    }



    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateQuestByIdWhenOriginUserIsNull() throws TalosQuestsException {
        questService.update(null, (Long)null, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateQuestByIdWhenOriginModelIsNull() throws TalosQuestsException {
        questService.update(testUserWithSession, (Long)null, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateQuestByIdWhenNewModelIsNull() throws TalosQuestsException {
        questService.update(testUserWithSession, testQuestSerres3.getId(), null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testUpdateQuestByIdWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        questService.update(testUserWithSession, testQuestSerres2.getId(), testQuestSerres2);
    }

    @Test
    public void testUpdateQuestByIdWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        Quest questModel = new Quest();
        questModel.setContent("temp");
        questModel.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        questModel.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        questModel.setLocation(testLocationAthens1);
        questModel.setName("temp");
        questModel.setExp(testQuestSerres2.getExp());

        Quest quest = questService.update(testUserWithSession, testQuestSerres2.getId(), questModel);

        assertEquals(quest.getLocation(), testLocationAthens1);
        assertEquals(quest.getName(), "temp");
        assertEquals(quest.getContent(), "temp");
    }



    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testWipeQuestWhenOriginUserlIsNull() throws TalosQuestsException {
        questService.wipe(null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testWipeQuestWhenOriginUserHasNoPermissionOfWipingQuests() throws TalosQuestsException {
        questService.wipe(testUserWithSession);
    }

    @Test
    public void testWipeQuestWhenOriginUserHasPermissionOfWipingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanWipeQuests(true);
        testUserWithSession.getAccess().setCanManageQuests(true);
        questService.wipe(testUserWithSession);

    }

    @Test
    public void testGetAllQuests() {
        assertThat(questService.getAllQuests(),hasSize(greaterThan(0)));
    }
}
