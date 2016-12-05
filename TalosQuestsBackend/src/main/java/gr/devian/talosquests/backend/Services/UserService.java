package gr.devian.talosquests.backend.Services;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Nikolas on 5/12/2016.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        if (id < 0) {
            return null;
        }
        return userRepository.findOne(id);
    }

    public User getUserByUsername(String userName) {
        if (Strings.isNullOrEmpty(userName)) {
            return null;
        }
        return userRepository.findUserByUserName(userName);
    }

    public User getUserByEmail(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return null;
        }
        return userRepository.findUserByEmail(email);
    }

    public User createUser(AuthRegisterModel model) {

        if (Strings.isNullOrEmpty(model.getEmail()) || Strings.isNullOrEmpty(model.getPassWord()))
            return null;

        User user = new User(model.getUserName(), model.getPassWord(), model.getEmail(), model.getImei());
        userRepository.save(user);

        return user;
    }

    public User updateUser(User user, AuthRegisterModel model) {
        if (model == null)
            return null;

        if (!Strings.isNullOrEmpty(model.getEmail()))
            user.setEmail(model.getEmail());
        if (!Strings.isNullOrEmpty(model.getImei()))
            user.setDeviceIMEI(model.getImei());
        if (!Strings.isNullOrEmpty(model.getPassWord()))
            user.setPassWord(model.getPassWord());

        userRepository.save(user);

        return user;
    }

    public void removeUser(User user) {
        if (user == null)
            return;
        sessionRepository.deleteSessionByUser(user);
        userRepository.delete(user);
    }

    public Session getSessionByUser(User user) {
        if (user == null)
            return null;

        Session session = sessionRepository.findSessionByUser(user);

        if (session == null)
            return null;

        return checkSessionState(session);
    }

    public Session getSessionByToken(String token) {
        Session session = sessionRepository.findSessionByToken(token);

        if (session == null)
            return null;

        return checkSessionState(session);

    }

    public Session createSession(User user) {
        Session session = getSessionByUser(user);

        if (session != null) {
            removeSession(session);
        }

        session = new Session(user);
        sessionRepository.save(session);
        return session;
    }

    private Session checkSessionState(Session session) {
        if (session.getExpireDate().before(new Date())) {
            sessionRepository.delete(session);
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_WEEK, 7);
            session.setExpireDate(cal.getTime());
            sessionRepository.save(session);
            return session;
        }
    }


    public void removeSession(Session session) {
        if (session == null)
            return;
        sessionRepository.delete(session);
    }
}
