package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractServiceTest;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 * Created by Nikolas on 15/1/2017.
 */
@Transactional
public class SessionServiceTests extends AbstractServiceTest {

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCreateSessionWhenUserIsNull() throws TalosQuestsNullArgumentException {
        sessionService.create(null);
    }

    @Test
    public void testGetSessionByUserWhenUserIsNull() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByUser(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByUserWhenUserDoesntHaveSession() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByUser(testUserWithoutSession);
        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByUserWhenUserHasSession() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByUser(testUserWithSession);
        assertNotNull("Failure - Expected not null", session);

        assertEquals("Failure - Session Ids not Equals", session.getSessionId(), testSession.getSessionId());

    }

    @Test
    public void testGetSessionByUserWhenExpired() throws TalosQuestsNullArgumentException {
        sessionService.expire(testSession);
        Session session = sessionService.getByUser(testUserWithSession);

        assertNull(session);
    }


    @Test
    public void testGetSessionByIdWhenIdIsNull() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getById(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByIdWhenIdIsValid() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getById(testSession.getSessionId());
        assertNotNull("Failure - Expected not null", session);

        assertEquals("Failure - Session Ids not Equals", session.getSessionId(), testSession.getSessionId());

    }

    @Test
    public void testGetSessionByIdWhenExpired() throws TalosQuestsNullArgumentException {
        sessionService.expire(testSession);
        Session session = sessionService.getById(testSession.getSessionId());

        assertNull(session);
    }

    @Test
    public void testGetSessionByIdWhenIdIsInvalid() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getById(Long.MAX_VALUE);

        assertNull(session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsNull() throws TalosQuestsNullArgumentException {

        Session session = sessionService.getByToken(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsNotValid() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByToken("testToken");
        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsValid() throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByToken(testSession.getToken());
        assertNotNull("Failure - Expected Not null", session);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testRemoveSessionWhenNullGiven() throws TalosQuestsNullArgumentException, TalosQuestsNullArgumentException {
        sessionService.delete((User) null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testCheckSessionStateOnNullSession() throws TalosQuestsNullArgumentException {
        sessionService.checkState(null);
    }

    @Test
    public void testCheckSessionStateOnExpiredSession() throws TalosQuestsNullArgumentException {
        testSession.expire();
        assertNull(sessionService.checkState(testSession));
    }

    @Test
    public void testCheckSessionStateOnValidSession() throws TalosQuestsNullArgumentException {
        assertNotNull(sessionService.checkState(testSession));
    }


    @Test
    public void testDeleteSessionByUserWhenSessionIsValidAndUserIsNotNull() throws TalosQuestsNullArgumentException {
        sessionService.delete(testUserWithSession);
        assertNull(sessionService.getByUser(testUserWithSession));
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testDeleteSessionWhenSessionIsNull() throws TalosQuestsNullArgumentException {
        sessionService.delete((Session) null);
    }

    @Test(expected = TalosQuestsNullArgumentException.class)
    public void testWipeWhenUserIsNull() throws TalosQuestsException {
        sessionService.wipe(null);
    }

    @Test(expected = TalosQuestsAccessViolationException.class)
    public void testWipeWhenUserIsValidButHasNoPermissionToWipe() throws TalosQuestsException {
        sessionService.wipe(testUserWithSession);
    }

    @Test
    public void testWipeWhenUserIsValidWithPermission() throws TalosQuestsException {
        accessService.setUserAccessLevel(testUserWithSession, "Admin");
        sessionService.wipe(testUserWithSession);
    }

    @Test
    public void testGetAllSessions() {
        assertThat(sessionService.getAllSessions(), hasSize(1));
    }
}
