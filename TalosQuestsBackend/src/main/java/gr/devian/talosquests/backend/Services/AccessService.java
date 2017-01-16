package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsDuplicateEntryException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.AccessLevel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.AccessRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Nikolas on 15/1/2017.
 */
@Service
public class AccessService {

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        if (getByName("User") == null) {
            AccessLevel accessLevel = new AccessLevel();
            accessLevel.setName("User");
            accessRepository.save(accessLevel);

            accessLevel = new AccessLevel();
            accessLevel.setName("Root");
            accessLevel.setCanWipeQuests(true);
            accessLevel.setCanWipeUsers(true);
            accessLevel.setCanWipeGames(true);
            accessLevel.setCanWipeSessions(true);
            accessLevel.setCanManageQuests(true);
            accessLevel.setCanManageService(true);
            accessLevel.setCanManageUsers(true);
            accessLevel.setCanManageGames(true);
            accessLevel.setCanBanUsers(true);
            accessRepository.save(accessLevel);

            accessLevel = new AccessLevel();
            accessLevel.setName("Admin");
            accessLevel.setCanManageQuests(true);
            accessLevel.setCanManageService(true);
            accessLevel.setCanManageUsers(true);
            accessLevel.setCanManageGames(true);
            accessLevel.setCanBanUsers(true);
            accessLevel.setCanWipeSessions(true);
            accessRepository.save(accessLevel);
        }
    }

    public AccessLevel create(User originUser, AccessLevel level) throws TalosQuestsDuplicateEntryException, TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");

        if (!originUser.getAccess().getCanManageService())
            throw new TalosQuestsAccessViolationException();

        return create(level);
    }

    public AccessLevel create(AccessLevel level) throws TalosQuestsDuplicateEntryException, TalosQuestsNullArgumentException {
        if (level == null)
            throw new TalosQuestsNullArgumentException("level");

        if (getByName(level.getName()) != null)
            throw new TalosQuestsDuplicateEntryException("name");

        level.setId(null);

        return accessRepository.save(level);
    }

    public void delete(User originUser, String name) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (name == null)
            throw new TalosQuestsNullArgumentException("name");

        delete(originUser,getByName(name));
    }

    public void delete(User originUser, AccessLevel level) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");

        if (!originUser.getAccess().getCanManageService())
            throw new TalosQuestsAccessViolationException();

        delete(level);
    }

    public void delete(AccessLevel level) throws TalosQuestsNullArgumentException {
        if (level == null)
            throw new TalosQuestsNullArgumentException("level");

        for (User user : userService.getAllUsers()) {
            if (user.getAccess().equals(level)) {
                user.setAccess(null);
            }
            userService.save(user);
        }

        accessRepository.delete(level);
    }

    public AccessLevel getByName(String name) {
        if (name == null)
            return null;

        return accessRepository.findAccessLevelByName(name);
    }

    public void setUserAccessLevel(User user, String accessLevelName) throws TalosQuestsNullArgumentException {
        if (accessLevelName == null)
            throw new TalosQuestsNullArgumentException("accessLevelName");

        setUserAccessLevel(user, getByName(accessLevelName));
    }

    public void setUserAccessLevel(User user, AccessLevel accessLevel) throws TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");
        if (accessLevel == null)
            throw new TalosQuestsNullArgumentException("accessLevel");

        user.setAccess(accessLevel);

        userService.save(user);
    }
}
