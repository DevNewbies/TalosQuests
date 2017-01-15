package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Factories.GameFactory;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.UserQuest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Service
public class GameService {


    @Autowired
    UserService userService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserQuestRepository userQuestRepository;

    @Autowired
    LocationService locationService;


    @Autowired
    GameFactory gameFactory;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game create(User user) throws TalosQuestsException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("User");

        Game game = gameFactory.build(user);

        for (UserQuest userQuest : game.getIncompleteUserQuests()) {
            userQuestRepository.save(userQuest);
        }

        gameRepository.save(game);

        user.addGame(game);
        userService.save(user);

        return game;
    }

    public void delete(Game game) throws TalosQuestsException {
        delete(game.getUser(), game);
    }

    public void delete(User originUser, Game game) throws TalosQuestsException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("Game");

        if (originUser != null) {
            if (originUser.equals(game.getUser()) && !originUser.getAccess().getCanManageOwnData())
                throw new TalosQuestsAccessViolationException("User has no access deleting own game");
            if (!originUser.equals(game.getUser()) && !originUser.getAccess().getCanManageUsers())
                throw new TalosQuestsAccessViolationException("User has no access deleting other user's game");
        }

        ArrayList<UserQuest> userQuests = new ArrayList<UserQuest>(game.getCompletedUserQuests());
        game.getCompletedUserQuests().clear();
        for (UserQuest userQuest : userQuests) {
            userQuest.setQuest(null);
            if (game.getIncompleteUserQuests().contains(userQuest))
                game.getIncompleteUserQuests().remove(userQuest);
            userQuestRepository.save(userQuest);
            userQuestRepository.delete(userQuest);
        }

        userQuests = new ArrayList<UserQuest>(game.getIncompleteUserQuests());
        game.getIncompleteUserQuests().clear();
        for (UserQuest userQuest : userQuests) {
            userQuest.setQuest(null);
            userQuestRepository.save(userQuest);
            userQuestRepository.delete(userQuest);
        }


        game.setActiveUserQuest(null);

        User user = game.getUser();
        user.removeGame(game);

        if (user.getActiveGame() != null)
            if (user.getActiveGame().equals(game))
                user.setActiveGame(null);
        game.setUser(null);

        userService.save(user);

        gameRepository.delete(game);

    }


    public void delete(User originUser, User targetUser) throws TalosQuestsException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (targetUser == null)
            throw new TalosQuestsNullArgumentException("targetUser");

        ArrayList<Game> games = new ArrayList<>(targetUser.getGames());
        for (Game game : games) {
            delete(originUser, game);
        }
    }

    public void delete(User user) throws TalosQuestsException {
        delete(user, user);
    }

    public void setActiveGame(User user, Game game) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("game");
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");
        if (!game.getUser().equals(user))
            throw new TalosQuestsAccessViolationException();

        user.setActiveGame(game);
        game.setActive(true);
        userService.save(user);
        gameRepository.save(game);

    }

    public Game getGameById(Long id) {
        if (id == null)
            return null;
        return gameRepository.findOne(id);
    }

    public void load(User user, Long id) throws TalosQuestsException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");
        if (id == null)
            throw new TalosQuestsNullArgumentException("id");

        Game game = getGameById(id);
        if (game == null)
            throw new TalosQuestsException("Game with this id not found");

        setActiveGame(user, game);

    }

    public Game getActiveGameByUser(User user) {
        if (user == null)
            return null;
        return user.getActiveGame();
    }

    public UserQuest getActiveQuest(Game game) {
        if (game == null)
            return null;
        return game.getActiveUserQuest();
    }

    public Boolean submitQuestAnswer(Game game, QuestChoice choice) {
        if (game == null)
            return false;

        UserQuest userQuest = getActiveQuest(game);

        if (userQuest == null)
            return false;
        if (choice == null)
            return false;
        else
            return choice.equals(userQuest.getQuest().getCorrectChoice());

    }

    public void finishQuest(Game game, Boolean state) throws TalosQuestsNullArgumentException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("game");
        if (state == null)
            throw new TalosQuestsNullArgumentException("state");
        UserQuest userQuest = game.getActiveUserQuest();
        if (userQuest != null) {
            game.getIncompleteUserQuests().remove(userQuest);
            game.getCompletedUserQuests().add(userQuest);
            userQuest.complete(state);
            game.setExperiencePoints(game.getExperiencePoints() + (state ? userQuest.getQuest().getExp() : 0));
            game.setActiveUserQuest(null);
        }
    }


    public Boolean checkGameState(Game game) {
        if (game == null)
            return null;
        return game.getIncompleteUserQuests().size() <= 0;
    }

    public UserQuest getNextQuest(Game game) throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("Game");

        if (game.getIncompleteUserQuests().size() <= 0)
            throw new TalosQuestsLocationsNotAvailableException();

        if (game.getActiveUserQuest() != null)
            finishQuest(game, false);

        ArrayList<UserQuest> userQuests = new ArrayList<>(game.getIncompleteUserQuests());

        UserQuest c = locationService.getClosestQuestDistance(game.getCurrentUserLocation(), userQuests).left;
        c.start();
        game.setActiveUserQuest(c);
        gameRepository.save(game);

        return c;
    }

    public void save(Game game) throws TalosQuestsNullArgumentException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("game");

        gameRepository.save(game);
    }

    public void wipe(User user) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");

        if (!user.getAccess().getCanWipeGames())
            throw new TalosQuestsAccessViolationException("User has no permission to wipe games.");

        wipe();
    }

    public void wipe() throws TalosQuestsNullArgumentException {
        for (Game game : gameRepository.findAll()) {
            ArrayList<UserQuest> userQuests = new ArrayList<UserQuest>(game.getCompletedUserQuests());
            game.getCompletedUserQuests().clear();
            for (UserQuest userQuest : userQuests) {
                if (game.getIncompleteUserQuests().contains(userQuest))
                    game.getIncompleteUserQuests().remove(userQuest);
                userQuest.setQuest(null);
                userQuestRepository.save(userQuest);
                userQuestRepository.delete(userQuest);
            }

            userQuests = new ArrayList<UserQuest>(game.getIncompleteUserQuests());
            game.getIncompleteUserQuests().clear();
            for (UserQuest userQuest : userQuests) {
                userQuest.setQuest(null);
                userQuestRepository.save(userQuest);
                userQuestRepository.delete(userQuest);
            }


            game.setActiveUserQuest(null);

            User user = game.getUser();
            user.setActiveGame(null);
            user.getGames().clear();
            userService.save(user);

            game.setUser(null);

            gameRepository.save(game);
        }
        userQuestRepository.deleteAllInBatch();
        gameRepository.deleteAllInBatch();
    }


}
