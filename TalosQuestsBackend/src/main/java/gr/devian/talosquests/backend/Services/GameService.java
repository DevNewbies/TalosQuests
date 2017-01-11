package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Factories.GameFactory;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
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
    UserRepository userRepository;

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

        for (Quest quest : game.getIncompleteQuests()) {
            userQuestRepository.save(quest);
        }

        gameRepository.save(game);

        user.addGame(game);
        userRepository.save(user);

        return game;
    }

    public void delete(Game game) throws TalosQuestsException {
        delete(game.getUser(), game);
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

    public void delete(User originUser, Game game) throws TalosQuestsException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("Game");

        if (originUser != null) {
            if (originUser.equals(game.getUser()) && !originUser.getAccess().getCanManageOwnData())
                throw new TalosQuestsAccessViolationException("User has no access deleting own game");
            if (!originUser.equals(game.getUser()) && !originUser.getAccess().getCanManageUsers())
                throw new TalosQuestsAccessViolationException("User has no access deleting other user's game");
        }

        ArrayList<Quest> quests = new ArrayList<Quest>(game.getCompletedQuests());
        game.getCompletedQuests().clear();
        for (Quest quest : quests) {
            quest.setQuest(null);
            if (game.getIncompleteQuests().contains(quest))
                game.getIncompleteQuests().remove(quest);
            userQuestRepository.save(quest);
            userQuestRepository.delete(quest);
        }

        quests = new ArrayList<Quest>(game.getIncompleteQuests());
        game.getIncompleteQuests().clear();
        for (Quest quest : quests) {
            quest.setQuest(null);
            userQuestRepository.save(quest);
            userQuestRepository.delete(quest);
        }


        game.setActiveQuest(null);

        User user = game.getUser();
        user.removeGame(game);

        if (user.getActiveGame() != null)
            if (user.getActiveGame().equals(game))
                user.setActiveGame(null);
        game.setUser(null);

        userRepository.save(user);

        gameRepository.delete(game);

    }

    public void setActiveGame(User user, Game game) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("game");
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");
        if (!game.getUser().equals(user))
            throw new TalosQuestsAccessViolationException();
        //if (!user.getGames().contains(game))

        user.setActiveGame(game);
        game.setActive(true);
        userRepository.save(user);
        gameRepository.save(game);

    }

    public Game getGameById(Long id) {
        if (id == null)
            return null;
        return gameRepository.findOne(id);
    }

    public void load(User user, Long id) throws TalosQuestsException {
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

    public Quest getActiveQuest(Game game) {
        if (game == null)
            return null;
        return game.getActiveQuest();
    }

    public Boolean submitQuestAnswer(Game game, QuestChoice choice) {
        if (game == null)
            return false;

        Quest quest = getActiveQuest(game);

        if (quest == null)
            return false;
        if (choice == null)
            return false;
        else
            return choice.equals(quest.getQuest().getCorrectChoice());

    }

    public void finishQuest(Game game, Boolean state) throws TalosQuestsNullArgumentException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("game");
        if (state == null)
            throw new TalosQuestsNullArgumentException("state");
        Quest quest = game.getActiveQuest();
        if (quest != null) {
            game.getIncompleteQuests().remove(quest);
            game.getCompletedQuests().add(quest);
            quest.complete(state);
            game.setExperiencePoints(game.getExperiencePoints() + (state ? quest.getQuest().getExp() : 0));
            game.setActiveQuest(null);
        }
    }


    public Boolean checkGameState(Game game) {
        if (game == null)
            return null;
        return game.getIncompleteQuests().size() <= 0;
    }

    public Quest getNextQuest(Game game) throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("Game");

        if (game.getIncompleteQuests().size() <= 0)
            throw new TalosQuestsLocationsNotAvailableException();

        if (game.getActiveQuest() != null)
            finishQuest(game, false);

        ArrayList<Quest> quests = new ArrayList<>(game.getIncompleteQuests());

        Quest c = locationService.getClosestQuestDistance(game.getCurrentUserLocation(), quests).left;
        c.start();
        game.setActiveQuest(c);
        gameRepository.save(game);

        return c;
    }


    public void wipe(User user) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (user == null)
            throw new TalosQuestsNullArgumentException("user");

        if (!user.getAccess().getCanWipeGames())
            throw new TalosQuestsAccessViolationException("User has no permission to wipe games.");

        wipe();
    }

    public void wipe() {
        for (Game game : gameRepository.findAll()) {
            ArrayList<Quest> quests = new ArrayList<Quest>(game.getCompletedQuests());
            game.getCompletedQuests().clear();
            for (Quest quest : quests) {
                if (game.getIncompleteQuests().contains(quest))
                    game.getIncompleteQuests().remove(quest);
                quest.setQuest(null);
                userQuestRepository.save(quest);
                userQuestRepository.delete(quest);
            }

            quests = new ArrayList<Quest>(game.getIncompleteQuests());
            game.getIncompleteQuests().clear();
            for (Quest quest : quests) {
                quest.setQuest(null);
                userQuestRepository.save(quest);
                userQuestRepository.delete(quest);
            }


            game.setActiveQuest(null);

            User user = game.getUser();
            user.setActiveGame(null);
            user.getGames().clear();
            userRepository.save(user);

            game.setUser(null);

            gameRepository.save(game);
        }
        userQuestRepository.deleteAllInBatch();
        gameRepository.deleteAllInBatch();
    }


}
