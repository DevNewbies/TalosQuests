package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
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
}
