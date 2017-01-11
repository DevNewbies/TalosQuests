package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestModel;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

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
        assertNotNull(questService.getQuestById(((Quest) testGameForUserWithSession.getIncompleteQuests().toArray()[0]).getQuest().getId()));
    }

    @Test
    public void testCreateQuestWhenOriginUserIsNull() throws TalosQuestsException {
        try {
            questService.create(null, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateQuestWhenModelIsNull() throws TalosQuestsException {
        try {
            questService.create(testUserWithSession, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateQuestWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        try {
            questService.create(testUserWithSession, testQuestModelSerres2);
            fail();
        } catch (TalosQuestsAccessViolationException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateQuestWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        QuestModel quest = questService.create(testUserWithSession, testQuestModelSerres2);
        assertNotNull(quest);
    }


    @Test
    public void testDeleteQuestWhenOriginUserIsNull() throws TalosQuestsException {
        try {
            questService.delete(null, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteQuestWhenModelIsNull() throws TalosQuestsException {
        try {
            questService.delete(testUserWithSession, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteQuestWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        try {
            questService.delete(testUserWithSession, testQuestModelSerres2);
            fail();
        } catch (TalosQuestsAccessViolationException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteQuestWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        gameService.getNextQuest(testGameForUserWithSession);
        for (Quest quest : testGameForUserWithSession.getIncompleteQuests()) {
            if (quest.getQuest().equals(testQuestModelSerres3))
                testGameForUserWithSession.setActiveQuest(quest);
        }
        questService.delete(testUserWithSession, testQuestModelSerres3);

        assertTrue(true);
    }


    @Test
    public void testUpdateQuestWhenOriginUserIsNull() throws TalosQuestsException {
        try {
            questService.update(null, null, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateQuestWhenOriginModelIsNull() throws TalosQuestsException {
        try {
            questService.update(testUserWithSession, null, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateQuestWhenNewModelIsNull() throws TalosQuestsException {
        try {
            questService.update(testUserWithSession, testQuestModelSerres3, null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateQuestWhenOriginUserHasNoPermissionOfManagingQuests() throws TalosQuestsException {
        try {
            questService.update(testUserWithSession, testQuestModelSerres2, testQuestModelSerres2);
            fail();
        } catch (TalosQuestsAccessViolationException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateQuestWhenOriginUserHasPermissionOfManagingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageQuests(true);
        QuestModel questModel = new QuestModel();
        questModel.setContent("temp");
        questModel.setAvailableChoices(testQuestModelSerres2.getAvailableChoices());
        questModel.setCorrectChoice(testQuestModelSerres2.getCorrectChoice());
        questModel.setLocation(testLocationAthens1);
        questModel.setName("temp");
        questModel.setExp(testQuestModelSerres2.getExp());

        QuestModel quest = questService.update(testUserWithSession, testQuestModelSerres2, questModel);

        assertEquals(quest.getLocation(), testLocationAthens1);
        assertEquals(quest.getName(), "temp");
        assertEquals(quest.getContent(), "temp");
    }

    @Test
    public void testWipeQuestWhenOriginUserlIsNull() throws TalosQuestsException {
        try {
            questService.wipe(null);
            fail();
        } catch (TalosQuestsNullArgumentException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testWipeQuestWhenOriginUserHasNoPermissionOfWipingQuests() throws TalosQuestsException {
        try {
            questService.wipe(testUserWithSession);
            fail();
        } catch (TalosQuestsAccessViolationException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testWipeQuestWhenOriginUserHasPermissionOfWipingQuests() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanWipeQuests(true);
        testUserWithSession.getAccess().setCanManageQuests(true);
        questService.wipe(testUserWithSession);

    }
}
