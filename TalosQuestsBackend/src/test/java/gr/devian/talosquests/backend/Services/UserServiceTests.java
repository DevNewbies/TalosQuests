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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Nikolas on 5/12/2016.
 */

@Transactional
public class UserServiceTests extends AbstractServiceTest {


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
    public void testFindUsersByUsersnameWhenSearchIsNull() {
        List<User> users = userService.findUsersByName(null);
        assertNull(users);
    }

    @Test
    public void testFindUsersByUsersnameWhenSearchIsEmpty() {
        List<User> users = userService.findUsersByName("");
        assertEquals(users.size(), 0);
    }

    @Test
    public void testFindUsersByUsersnameWhenSearchIsNotNullOrEmptyWithWildcard() {
        List<User> users = userService.findUsersByName("%test%");
        assertEquals(users.size(), 2);
    }

    @Test
    public void testFindUsersByEmailWhenSearchIsNull() {
        List<User> users = userService.findUsersByEmail(null);
        assertNull(users);
    }

    @Test
    public void testFindUsersByEmailWhenSearchIsEmpty() {
        List<User> users = userService.findUsersByEmail("");
        assertEquals(users.size(), 0);
    }

    @Test
    public void testFindUsersByEmailWhenSearchIsNotNullOrEmptyWithWildcard() {
        List<User> users = userService.findUsersByEmail("%test%");
        assertEquals(users.size(), 2);
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

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateUserFromOtherUserWhenOriginIsNull() throws TalosQuestsException {
        userService.update(null, testUserWithSession, new AuthRegisterModel());
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testUpdateUserFromOtherUserWhenTargetIsNull() throws TalosQuestsException {
        userService.update(testUserWithSession, null, new AuthRegisterModel());
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testUpdateUserFromOtherUserWhenOriginIsTargetButHasNoAccessToChangeData() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageOwnData(false);
        userService.update(testUserWithSession, testUserWithSession, new AuthRegisterModel());
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testUpdateUserFromOtherUserWhenOriginIsNotTargetAndHasNoAccessToChangeData() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanManageUsers(false);
        userService.update(testUserWithSession, testUserWithoutSession, new AuthRegisterModel());
    }

    @Test
    public void testUpdateUserFromOtherUserWhenOriginIsNotTargetAndHasAccessToChangeData() throws TalosQuestsException {

        testUserWithSession.getAccess().setCanManageUsers(true);
        AuthRegisterModel model = new AuthRegisterModel();
        model.setPassWord("Test123!123");
        model.setImei("012345678912345");
        model.setEmail("test@test.gr");
        User user = userService.update(testUserWithSession, testUserWithoutSession, model);
        assertEquals(user.getEmail(), "test@test.gr");

    }


    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetActiveLocationOnNullUser() throws TalosQuestsNullArgumentException {
        userService.setActiveLocation(null, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetActiveLocationOnNullLocation() throws TalosQuestsNullArgumentException {
        userService.setActiveLocation(testUserWithSession, null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testRemoveUserWhenUserIsNull() throws TalosQuestsException {
        userService.delete(null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testRemoveUserWhenOriginIsNull() throws TalosQuestsException {
        userService.delete(null, testUserWithoutSession);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testRemoveUserWhenTargetIsNull() throws TalosQuestsException {
        userService.delete(testUserWithoutSession, null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testRemoveUserWhenOriginIsTargetButHasNoPermissionToManageOwnData() throws TalosQuestsException {
        testUserWithoutSession.getAccess().setCanManageOwnData(false);
        userService.delete(testUserWithoutSession, testUserWithoutSession);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testRemoveUserWhenOriginIsNotTargetAndHasNoPermissionToManageOtherUsersData() throws TalosQuestsException {
        testUserWithoutSession.getAccess().setCanManageUsers(false);
        userService.delete(testUserWithoutSession, testUserWithSession);
    }

    @Test
    public void testRemoveUserWhenOriginIsNotTargetAndHasPermissionToManageOtherUsers() throws TalosQuestsException {
        sessionService.create(testUserWithoutSession);
        testUserWithSession.getAccess().setCanManageUsers(true);
        userService.delete(testUserWithSession, testUserWithoutSession);
    }

    @Test
    public void testCreateSessionWhenSessionAlreadyExists() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByUser(testUserWithSession);
        assertNotEquals(sessionService.create(testUserWithSession), session);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetBannedStateWhenOriginIsNull() throws TalosQuestsException {
        userService.setBannedState(null, testUserWithoutSession, true);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetBannedStateWhenTargetIsNull() throws TalosQuestsException {
        userService.setBannedState(testUserWithSession, null, true);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSetBannedStateWhenBanIsNull() throws TalosQuestsException {
        userService.setBannedState(testUserWithSession, testUserWithoutSession, null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testSetBannedStateWhenOriginHasNoPermissionBanningUsers() throws TalosQuestsException {
        userService.setBannedState(testUserWithSession, testUserWithoutSession, true);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testSetBannedStateWhenOriginIsTarget() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanBanUsers(true);
        userService.setBannedState(testUserWithSession, testUserWithSession, true);
    }

    @Test
    public void testSetBannedStateWhenUserHasPermission() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanBanUsers(true);
        userService.setBannedState(testUserWithSession, testUserWithoutSession, true);
        assertTrue(testUserWithoutSession.getBanned());
    }


    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testWipeUsersWhenUserIsNull() throws TalosQuestsException {
        userService.wipe(null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testWipeUsersWhenUserHasNoAccess() throws TalosQuestsException {
        userService.wipe(testUserWithSession);
    }

    @Test
    public void testWipeUsersWhenUserHasAccess() throws TalosQuestsException {
        testUserWithSession.getAccess().setCanWipeUsers(true);
        userService.wipe(testUserWithSession);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testSaveUserWhenUserIsNull() throws TalosQuestsNullArgumentException {
        userService.save(null);
    }

    @Test
    public void testSaveUserWhenUserIsValid() throws TalosQuestsNullArgumentException {
        userService.save(testUserWithSession);
    }
}
