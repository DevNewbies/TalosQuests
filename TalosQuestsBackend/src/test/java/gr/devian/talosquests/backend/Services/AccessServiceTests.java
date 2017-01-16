package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsDuplicateEntryException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.AccessLevel;
import gr.devian.talosquests.backend.Models.User;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Nikolas on 15/1/2017.
 */
@Transactional
public class AccessServiceTests extends AbstractServiceTest {
    @Test
    public void testGetAccessLevelByNameWhenNameIsNull() {
        assertNull(accessService.getByName(null));
    }

    @Test
    public void testGetAccessLevelByNameWhenNameNotInDatabase() {
        assertNull(accessService.getByName(""));
    }


    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetAccessLevelWhenTargetIsNull() throws TalosQuestsNullArgumentException {
        accessService.setUserAccessLevel(null, (AccessLevel) null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetAccessLevelWhenAccessLevelIsNull() throws TalosQuestsNullArgumentException {
        accessService.setUserAccessLevel(testUserWithSession, (AccessLevel) null);
    }

    @Test
    public void testSetAccessLevelSuccess() throws TalosQuestsException {
        accessService.setUserAccessLevel(testUserWithSession, accessService.getByName("Admin"));
        assertEquals(testUserWithSession.getAccess().getCanBanUsers(), true);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetUserAccessLevelWhenAccessLevelNameIsNull() throws TalosQuestsNullArgumentException {
        accessService.setUserAccessLevel(testUserWithSession, (String) null);
    }

    @Test
    public void testSetUserAccessLevelWhenAccessLevelNameIsNotNull() throws TalosQuestsNullArgumentException {
        accessService.setUserAccessLevel(testUserWithSession, "Root");
        assertEquals(testUserWithSession.getAccess().getName(), "Root");
    }

    @Test
    public void testInitWhenAccessLevelDatabaseIsEmpty() throws TalosQuestsNullArgumentException {
        for (User user : userService.getAllUsers()) {
            user.setAccess(null);
            userService.save(user);
        }
        accessRepository.deleteAll();
        accessService.init();
        assertNotNull(accessService.getByName("User"));
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCreateWhenOriginUserIsNull() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsDuplicateEntryException {
        accessService.create(null, null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testCreateWhenOriginUserHasNoPermissionsToManageService() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsDuplicateEntryException {
        accessService.create(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCreateWhenOriginUserHasPermissionsToManageServiceButAccessLevelIsNull() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsDuplicateEntryException {
        accessService.setUserAccessLevel(testUserWithSession, "Root");
        accessService.create(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsDuplicateEntryException.class)
    public void testCreateWhenOriginUserHasPermissionsToManageServiceButAccessLevelAlreadyExists() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsDuplicateEntryException {
        accessService.setUserAccessLevel(testUserWithSession, "Root");
        accessService.create(testUserWithSession, new AccessLevel());
    }

    @Test
    public void testCreateWhenOriginUserHasPermissionsToManageServiceAndAccessLevelIsValid() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsDuplicateEntryException {
        accessService.setUserAccessLevel(testUserWithSession, "Root");
        AccessLevel level = new AccessLevel();
        level.setName("test");
        AccessLevel newLevel = accessService.create(testUserWithSession, level);
        assertEquals(newLevel.getName(), level.getName());
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteWhenLevelNameIsNull() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        accessService.delete(null,(String)null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteWhenOriginUserIsNull() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        accessService.delete(null,"Root");
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testDeleteWhenOriginUserHasNoPermissionManagingService() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        accessService.delete(testUserWithSession,"Root");
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteWhenLevelIsNull() throws TalosQuestsNullArgumentException {
        accessService.delete(null);
    }

    @Test
    public void testDeleteWhenLevelIsValid() throws TalosQuestsNullArgumentException {
        accessService.delete(accessService.getByName("User"));
        assertNull(testUserWithSession.getAccess());
    }

    @Test
    public void testDeleteWhenLevelIsValidAndOriginUserHasAccess() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        accessService.setUserAccessLevel(testUserWithSession,"Root");
        accessService.delete(testUserWithSession,accessService.getByName("User"));
        assertNull(testUserWithoutSession.getAccess());
    }

    @Test
    public void testDeleteWhenLevelNameIsValidAndOriginUserHasAccess() throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        accessService.setUserAccessLevel(testUserWithSession,"Root");
        accessService.delete(testUserWithSession,"User");
        assertNull(testUserWithoutSession.getAccess());
    }
}
