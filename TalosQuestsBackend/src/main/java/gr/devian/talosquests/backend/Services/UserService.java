package gr.devian.talosquests.backend.Services;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.*;
import gr.devian.talosquests.backend.Repositories.AccessRepository;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
    GameService gameService;

    @Autowired
    AccessService accessService;

    @Autowired
    SessionService sessionService;


    public final String userNameValidationPattern = "^[a-zA-Z0-9_\\-]{4,32}$";
    public final String passWordValidationPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$";
    public final String emailValidationPattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public final String imeiValidationPattern = "^\\d{15}$";


    public List<User> findAllUsers() {
        return getAllUsers();
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

    public User create(AuthRegisterModel model) throws TalosQuestsCredentialsNotMetRequirementsException, TalosQuestsInsufficientUserDataException {

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
        user.setAccess(accessService.getByName("User"));
        userRepository.save(user);

        return user;
    }

    public User update(User user, AuthRegisterModel model) throws TalosQuestsException {
        return update(user, user, model);
    }

    public User update(User origin, User target, AuthRegisterModel model) throws TalosQuestsException {
        if (model == null)
            throw new TalosQuestsInsufficientUserDataException();
        if (target == null)
            throw new TalosQuestsNullArgumentException("target");
        if (origin == null)
            throw new TalosQuestsNullArgumentException("origin");

        if (origin.equals(target) && !origin.getAccess().getCanManageOwnData())
            throw new TalosQuestsAccessViolationException("User has no permissions to edit own data.");

        if (!origin.equals(target) && !origin.getAccess().getCanManageUsers())
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

    public User delete(User user) throws TalosQuestsException {
        return delete(user, user);
    }

    public User delete(User origin, User target) throws TalosQuestsException {
        if (target == null)
            throw new TalosQuestsNullArgumentException("target");
        if (origin == null)
            throw new TalosQuestsNullArgumentException("origin");

        if (origin.equals(target) && !origin.getAccess().getCanManageOwnData())
            throw new TalosQuestsAccessViolationException("User has no permissions to delete own data.");
        if (!origin.equals(target) && !origin.getAccess().getCanManageUsers())
            throw new TalosQuestsAccessViolationException("User has no permissions to delete other user's data.");

        Session session = sessionService.getByUser(target);
        if (session != null) {
            sessionService.delete(session);
        }

        ArrayList<Game> games = new ArrayList<>(target.getGames());
        for (Game game : games) {
            gameService.delete(game);
        }
        target.getGames().clear();

        userRepository.delete(target);

        return target;
    }

    public void setBannedState(User originUser, User targetUser, Boolean ban) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (targetUser == null)
            throw new TalosQuestsNullArgumentException("targetUser");
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (ban == null)
            throw new TalosQuestsNullArgumentException("ban");

        if (!originUser.getAccess().getCanBanUsers())
            throw new TalosQuestsAccessViolationException("User has no permissions to ban or unban other users.");

        if (originUser.equals(targetUser))
            throw new TalosQuestsAccessViolationException("You cannot ban/unban your self.");

        targetUser.setBanned(ban);
        userRepository.save(targetUser);
    }

    public void save(User user) throws TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");

        userRepository.save(user);
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
        sessionService.wipe();
        gameService.wipe();
        userRepository.deleteAllInBatch();

    }
}