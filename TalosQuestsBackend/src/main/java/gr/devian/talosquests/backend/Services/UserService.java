package gr.devian.talosquests.backend.Services;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullSessionException;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    GameRepository gameRepository;

    @Autowired
    GameService gameService;

    @Autowired
    SessionRepository sessionRepository;


    public final String userNameValidationPattern = "^[a-zA-Z0-9_\\-]{4,32}$";
    public final String passWordValidationPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$";
    public final String emailValidationPattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public final String imeiValidationPattern = "^\\d{15}$";

    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        if (id == null)
            return null;
        if (id < 0)
            return null;

        return userRepository.findOne(id);
    }

    public User getUserByUsername(String userName) {
        if (Strings.isNullOrEmpty(userName))
            return null;

        return userRepository.findUserByUserName(userName);
    }

    public User getUserByEmail(String email) {
        if (Strings.isNullOrEmpty(email))
            return null;

        return userRepository.findUserByEmail(email);
    }

    public User createUser(AuthRegisterModel model) throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserData {

        if (Strings.isNullOrEmpty(model.getUserName())
                || Strings.isNullOrEmpty(model.getEmail())
                || Strings.isNullOrEmpty(model.getPassWord())
                || Strings.isNullOrEmpty(model.getImei()))
            throw new TalosQuestsInsufficientUserData();

        if (!model.getEmail().matches(emailValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("email", emailValidationPattern);
        if (!model.getImei().matches(imeiValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("imei", imeiValidationPattern);
        if (!model.getPassWord().matches(passWordValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("passWord", passWordValidationPattern);
        if (!model.getUserName().matches(userNameValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("userName", userNameValidationPattern);

        User user = new User(model.getUserName(), model.getPassWord(), model.getEmail(), model.getImei());

        userRepository.save(user);

        return user;
    }

    public User updateUser(User user, AuthRegisterModel model) throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserData {
        if (model == null)
            throw new TalosQuestsInsufficientUserData();

        if (!Strings.isNullOrEmpty(model.getEmail())) {
            if (!model.getEmail().matches(emailValidationPattern))
                throw new TalosQuestsCredentialsNotMetRequirementsException("email", emailValidationPattern);
            user.setEmail(model.getEmail());
        }
        if (!Strings.isNullOrEmpty(model.getImei())) {
            if (!model.getImei().matches(imeiValidationPattern))
                throw new TalosQuestsCredentialsNotMetRequirementsException("imei", imeiValidationPattern);
            user.setDeviceIMEI(model.getImei());
        }
        if (!Strings.isNullOrEmpty(model.getPassWord())) {
            if (!model.getPassWord().matches(passWordValidationPattern))
                throw new TalosQuestsCredentialsNotMetRequirementsException("passWord", passWordValidationPattern);
            user.setPassWord(model.getPassWord());
        }

        userRepository.save(user);

        return user;
    }

    public User removeUser(User user) throws TalosQuestsNullArgumentException, TalosQuestsNullSessionException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");

        if (getSessionByUser(user) != null) {
            sessionRepository.deleteSessionByUser(user);
        }

        ArrayList<Game> games = new ArrayList<Game>(user.getGames());
        for (Game game : games) {
            gameService.delete(game);
        }
        user.getGames().clear();
        user.setActiveGame(null);
        userRepository.save(user);
        userRepository.delete(user);

        return user;
    }

    public Session getSessionByUser(User user) throws TalosQuestsNullSessionException {
        if (user == null)
            return null;

        Session session = sessionRepository.findSessionByUser(user);

        if (session == null)
            return null;

        return checkSessionState(session);
    }

    public Session getSessionByToken(String token) throws TalosQuestsNullSessionException {
        Session session = sessionRepository.findSessionByToken(token);

        if (session == null)
            return null;

        return checkSessionState(session);

    }

    public Session createSession(User user) throws TalosQuestsNullSessionException {
        Session session = getSessionByUser(user);

        if (session != null)
            removeSession(session);

        session = new Session(user);
        sessionRepository.save(session);
        return session;
    }

    public Session checkSessionState(Session session) throws TalosQuestsNullSessionException {
        if (session == null)
            throw new TalosQuestsNullSessionException();

        if (session.getExpireDate().before(new Date())) {
            removeSession(session);
            return null;
        }
        session.updateExpirationDate();
        sessionRepository.save(session);
        return session;

    }

    public void expireSession(Session session) {
        session.expire();
        sessionRepository.save(session);
    }

    public void removeSession(Session session) throws TalosQuestsNullSessionException {
        if (session == null)
            throw new TalosQuestsNullSessionException();

        sessionRepository.delete(session);
    }

    public void wipe() throws TalosQuestsNullArgumentException {
        sessionRepository.deleteAllInBatch();
        gameService.wipe();
        userRepository.deleteAllInBatch();
    }
}
