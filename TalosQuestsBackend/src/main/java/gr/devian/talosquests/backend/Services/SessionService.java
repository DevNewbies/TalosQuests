package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Nikolas on 15/1/2017.
 */

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getByUser(User user) throws TalosQuestsNullArgumentException {
        if (user == null)
            return null;

        Session session = sessionRepository.findSessionByUser(user);

        if (session == null)
            return null;

        return checkState(session);
    }

    public Session getByToken(String token) throws TalosQuestsNullArgumentException {
        Session session = sessionRepository.findSessionByToken(token);

        if (session == null)
            return null;

        return checkState(session);

    }

    public Session getById(Long id) throws TalosQuestsNullArgumentException {
        if (id == null)
            return null;

        Session session = sessionRepository.findOne(id);

        if (session == null)
            return null;

        return checkState(session);
    }

    public void delete(User user) throws TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");

        Session session = getByUser(user);

        if (session != null) {
            sessionRepository.delete(session);
        }

    }

    public void delete(Session session) throws TalosQuestsNullArgumentException {
        if (session == null)
            throw new TalosQuestsNullArgumentException("session");

        sessionRepository.delete(session);


    }

    public Session create(User user) throws TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("User");

        Session session = getByUser(user);

        if (session != null)
            delete(session);

        session = new Session(user);
        sessionRepository.save(session);
        return session;
    }

    public Session checkState(Session session) throws TalosQuestsNullArgumentException {
        if (session == null)
            throw new TalosQuestsNullArgumentException("session");

        if (session.getExpires().before(new Date()) || session.getUser().getBanned()) {
            delete(session);
            return null;
        }

        session.updateExpirationDate();
        sessionRepository.save(session);
        return session;

    }

    public void expire(Session session) {
        session.expire();
        sessionRepository.save(session);
    }

    public void wipe(User user) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");

        if (!user.getAccess().getCanWipeSessions())
            throw new TalosQuestsAccessViolationException();

        wipe();
    }

    public void wipe() {
        sessionRepository.deleteAllInBatch();
    }

}
