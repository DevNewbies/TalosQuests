package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Nikolas on 5/12/2016.
 */

@Transactional
public class UserServiceTest extends AbstractServiceTest {


    @Test
    public void testFindAllNotNull() {

        Collection<User> list = userService.findAllUsers();

        assertNotNull("Failure - Expected not null", list);

        assertThat("Failure - Expected List Size Greater than 0", list.size(), greaterThan(0));

    }

    @Test
    public void testFindAllEmpty() throws TalosQuestsException {
        userService.wipe();
        Collection<User> list = userService.findAllUsers();

        assertTrue("Failure - Expected list to be empty", list.isEmpty());
    }


    @Test
    public void testFindByIdWhenExists() {
        User user = userService.getUserById(testUserWithSession.getId());

        assertNotNull("Failure - Expected not null", user);

        assertEquals("Failure - Expected user.Username = testUserWithSession.username", user.getUserName(), testUserWithSession.getUserName());

        assertNotNull("Failure - ID cannot be null", user.getId());
    }


    @Test
    public void testFindByIdWhenNotExists() {
        User user = userService.getUserById(Long.MAX_VALUE);

        assertNull("Failure - Expected null", user);

    }

    @Test
    public void testFindByUsernameWhenExists() {
        User user = userService.getUserByUsername(userNamePassing + "_withSession");

        assertNotNull("Failure - Expected not null", user);

        assertEquals("Failure - Expected user.Username = testUserWithSession.username", user.getUserName(), testUserWithSession.getUserName());

        assertNotNull("Failure - ID cannot be null", user.getId());
    }

    @Test
    public void testFindByUsernameWhenNotExists() {
        String temp = SecurityTools.MD5(new BigInteger(130, random).toString(32));
        User user = userService.getUserByUsername(temp);

        assertNull("Failure - Expected null", user);

    }

    @Test
    public void testGetSessionByUserWhenUserIsNull() throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByUser(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByUserWhenUserDoesntHaveSession() throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByUser(testUserWithoutSession);
        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByUserWhenUserHasSession() throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByUser(testUserWithSession);
        assertNotNull("Failure - Expected not null", session);

        assertEquals("Failure - Session Ids not Equals", session.getSessionId(), testSession.getSessionId());

    }

    @Test
    public void testGetSessionByUserWhenExpired() throws TalosQuestsNullSessionException {
        userService.expireSession(testSession);
        Session session = userService.getSessionByUser(testUserWithSession);

        assertNull(session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsNull() throws TalosQuestsNullSessionException {

        Session session = userService.getSessionByToken(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsNotValid() throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken("testToken");
        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsValid() throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(testSession.getToken());
        assertNotNull("Failure - Expected Not null", session);
    }

    @Test(expected = TalosQuestsNullSessionException.class)
    public void testRemoveSessionWhenNullGiven() throws TalosQuestsNullSessionException {
        userService.removeSession(null);
    }

    @Test
    public void testGetUserByEmailWhenEmailIsValid() {
        String email = testUserWithSession.getEmail();
        User user = userService.getUserByEmail(email);
        assertNotNull("Failure - User cannot be null", user);
    }

    @Test
    public void testGetUserByEmailWhenEmailIsNotValid() {
        String email = "asdagadh";
        User user = userService.getUserByEmail(email);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByEmailWhenEmailIsNull() {
        User user = userService.getUserByEmail(null);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByUsenameWhenUsernameIsNull() {
        User user = userService.getUserByUsername(null);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByIdWhenIdIsNull() {
        User user = userService.getUserById(null);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByIdWhenIdIsNegative() {
        User user = userService.getUserById((long) -7);
        assertNull("Failure - Expected null", user);
    }

    @Test(expected = TalosQuestsInsufficientUserDataException.class)
    public void testCreateUserOnInsufficientUserData() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {
        AuthRegisterModel model = new AuthRegisterModel();
        userService.create(model);
    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testCreateUserOnCredentialNotMetRequirementsImei() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("TestTestTest");
        model.setImei("test15");
        model.setPassWord("Asd123!!");
        model.setEmail("test@test.gr");

        User user = userService.create(model);


    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testCreateUserOnCredentialNotMetRequirementsEmail() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("TestTestTest");
        model.setImei("012345678912345");
        model.setPassWord("Asd123!!");
        model.setEmail("test");

        User user = userService.create(model);


    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testCreateUserOnCredentialNotMetRequirementsPassword() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("TestTestTest");
        model.setImei("012345678912345");
        model.setPassWord("asdsda");
        model.setEmail("test@test.gr");

        User user = userService.create(model);


    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testCreateUserOnCredentialNotMetRequirementsUsername() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("asd$ADSga");
        model.setImei("012345678912345");
        model.setPassWord("Asd123!!");
        model.setEmail("test@test.gr");

        User user = userService.create(model);


    }

    public void testCreateUserOnCorretUserDataShouldCreate() throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setUserName("TestTestTest");
        model.setImei("012345678912345");
        model.setPassWord("Asd123!!");
        model.setEmail("test@test.gr");

        User user = userService.create(model);

        assertNotNull(user);

    }

    /*@Test
    public void testRemoveUserWhenUserIsNull() {
        userService.delete(null);
        verify(userService,times(1)).delete(null);
    }*/

    @Test(expected = TalosQuestsInsufficientUserDataException.class)
    public void testUpdateUserOnNullModel() throws TalosQuestsException {
        userService.update(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testUpdateUserOnCredentialNotMetRequirementsImei() throws TalosQuestsException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setImei("test15");

        userService.update(testUserWithSession, model);

    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testUpdateUserOnCredentialNotMetRequirementsEmail() throws TalosQuestsException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setEmail("test");

        userService.update(testUserWithSession, model);

    }

    @Test(expected = TalosQuestsCredentialsNotMetRequirementsException.class)
    public void testUpdateUserOnCredentialNotMetRequirementsPassword() throws TalosQuestsException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setPassWord("123");

        userService.update(testUserWithSession, model);

    }

    @Test
    public void testUpdateUserShouldPass() throws TalosQuestsException {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setPassWord("Test123!123");
        model.setImei("012345678912345");
        model.setEmail("test@test.gr");

        User user = userService.update(testUserWithSession, model);

        assertNotNull(user);

    }

    @Test
    public void testCheckSessionStateOnNullSession() {
        try {
            userService.checkSessionState(null);
            fail();
        } catch (TalosQuestsNullSessionException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckSessionStateOnExpiredSession() throws TalosQuestsNullSessionException {
        testSession.expire();
        assertNull(userService.checkSessionState(testSession));
    }

    @Test
    public void testCheckSessionStateOnValidSession() throws TalosQuestsNullSessionException {
        assertNotNull(userService.checkSessionState(testSession));
    }

    @Test
    public void testSetActiveLocationOnNullUser() {
        try {
            userService.setActiveLocation(null,null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetActiveLocationOnNullLocation() {
        try {
            userService.setActiveLocation(testUserWithSession,null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRemoveUserWhenUserIsNull() throws TalosQuestsException {
        try {
            userService.delete(null);
            fail();
        } catch (TalosQuestsNullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateSessionWhenSessionAlreadyExists() throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByUser(testUserWithSession);
        assertNotEquals(userService.createSession(testUserWithSession),session);
    }
}
