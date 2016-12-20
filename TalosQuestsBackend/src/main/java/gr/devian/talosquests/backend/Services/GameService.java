package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Factories.GameFactory;
import gr.devian.talosquests.backend.LocationProvider.Location;
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
import java.util.Collection;
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

    public void delete(Game game) throws TalosQuestsNullArgumentException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("Game");


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

    public Quest getNextQuest(Game game) throws TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException {
        if (game == null)
            throw new TalosQuestsNullArgumentException("Game");

        Quest c = null;
        if (game.getIncompleteQuests().size() > 0) {
            if (game.getActiveQuest() != null)
                finishQuest(game, false);

            ArrayList<Quest> quests = new ArrayList<>(game.getIncompleteQuests());

            c = locationService.getClosestQuestDistance(game.getCurrentUserLocation(),quests).left;
            c.start();
            game.setActiveQuest(c);
        } else {
            game.setActiveQuest(null);

        }
        gameRepository.save(game);

        return c;
    }

    public Boolean checkGameState(Game game) {
        if (game == null)
            return null;
        return game.getIncompleteQuests().size() <= 0;
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
