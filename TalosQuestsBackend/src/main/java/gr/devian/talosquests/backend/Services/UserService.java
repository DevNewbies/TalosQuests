package gr.devian.talosquests.backend.Services;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.*;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersByName(String name) {
        if (name == null)
            return null;
        return userRepository.findUserByUserNameLike(name);
    }

    public List<User> findUsersByEmail(String email) {
        if (email == null)
            return null;
        return userRepository.findUserByEmailLike(email);
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

    public User createUser(AuthRegisterModel model) throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {

        if (Strings.isNullOrEmpty(model.getUserName())
                || Strings.isNullOrEmpty(model.getEmail())
                || Strings.isNullOrEmpty(model.getPassWord())
                || Strings.isNullOrEmpty(model.getImei()))
            throw new TalosQuestsInsufficientUserDataException();

        if (!model.getEmail().matches(emailValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("email", emailValidationPattern);
        if (!model.getImei().matches(imeiValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("imei", imeiValidationPattern);
        if (!model.getPassWord().matches(passWordValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("passWord", passWordValidationPattern);
        if (!model.getUserName().matches(userNameValidationPattern))
            throw new TalosQuestsCredentialsNotMetRequirementsException("userName", userNameValidationPattern);

        User user = new User(model.getUserName(), model.getPassWord(), model.getEmail(), model.getImei());
        user.setAccess(AccessLevel.User);
        userRepository.save(user);

        return user;
    }

    public User updateUser(User user, AuthRegisterModel model) throws TalosQuestsException {
        return updateUser(user, user, model);
    }

    public User updateUser(User origin, User target, AuthRegisterModel model) throws TalosQuestsException {
        if (model == null)
            throw new TalosQuestsInsufficientUserDataException();
        if (target == null)
            throw new TalosQuestsNullArgumentException("target");
        if (origin == null)
            throw new TalosQuestsNullArgumentException("origin");


        if (origin.equals(target) && !origin.getAccess().getCanEditOwnData())
            throw new TalosQuestsAccessViolationException("User has no permissions to edit own data.");
        if (!origin.equals(target) && !origin.getAccess().getCanManageOtherUsers())
            throw new TalosQuestsAccessViolationException("User has no permissions to edit other user's data.");

        if (!Strings.isNullOrEmpty(model.getEmail())) {
            if (!model.getEmail().matches(emailValidationPattern))
                throw new TalosQuestsCredentialsNotMetRequirementsException("email", emailValidationPattern);
            target.setEmail(model.getEmail());
        }
        if (!Strings.isNullOrEmpty(model.getImei())) {
            if (!model.getImei().matches(imeiValidationPattern))
                throw new TalosQuestsCredentialsNotMetRequirementsException("imei", imeiValidationPattern);
            target.setDeviceIMEI(model.getImei());
        }
        if (!Strings.isNullOrEmpty(model.getPassWord())) {
            if (!model.getPassWord().matches(passWordValidationPattern))
                throw new TalosQuestsCredentialsNotMetRequirementsException("passWord", passWordValidationPattern);
            target.setPassWord(model.getPassWord());
        }

        userRepository.save(target);

        return target;
    }

    public User removeUser(User user) throws TalosQuestsException {
        return removeUser(user, user);
    }

    public User removeUser(User origin, User target) throws TalosQuestsException {
        if (target == null)
            throw new TalosQuestsNullArgumentException("target");
        if (origin == null)
            throw new TalosQuestsNullArgumentException("origin");

        if (origin.equals(target) && !origin.getAccess().getCanDeleteOwnData())
            throw new TalosQuestsAccessViolationException("User has no permissions to delete own data.");
        if (!origin.equals(target) && !origin.getAccess().getCanManageOtherUsers())
            throw new TalosQuestsAccessViolationException("User has no permissions to delete other user's data.");


        if (getSessionByUser(target) != null) {
            sessionRepository.deleteSessionByUser(target);
        }

        ArrayList<Game> games = new ArrayList<Game>(target.getGames());
        for (Game game : games) {
            gameService.delete(game);
        }
        target.getGames().clear();
        target.setActiveGame(null);
        userRepository.save(target);
        userRepository.delete(target);

        return target;
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

    public void setActiveLocation(User user, LatLng location) throws TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");
        if (location == null)
            throw new TalosQuestsNullArgumentException("location");

        user.setLastLocation(location);

    }

    public void wipe(User user) throws TalosQuestsException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");
        if (!user.getAccess().getCanWipeUsers())
            throw new TalosQuestsAccessViolationException("User has no permission to wipe users");

        wipe();
    }

    public void wipe() throws TalosQuestsException {
        sessionRepository.deleteAllInBatch();
        for (User user : userRepository.findAll()) {
            ArrayList<Game> games = new ArrayList<Game>(user.getGames());
            for (Game game : games) {
                gameService.delete(null, game);
            }
            user.getGames().clear();
            user.setActiveGame(null);
            userRepository.save(user);
            userRepository.delete(user);
        }
    }


}
