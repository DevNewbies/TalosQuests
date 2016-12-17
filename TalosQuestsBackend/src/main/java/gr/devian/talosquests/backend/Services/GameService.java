package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
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

import java.util.Date;

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
    GameFactory gameFactory;

    public Game create(User user) throws TalosQuestsLocationServiceUnavailableException {
        Game game = gameFactory.build(user);




        for (Quest quest : game.getIncompleteQuests()) {
            userQuestRepository.save(quest);
        }

        gameRepository.save(game);

        user.addGame(game);
        userRepository.save(user);

        return game;
    }

    public void delete(Game game) {
        game.getUser().removeGame(game);
        userRepository.save(game.getUser());
        gameRepository.delete(game);
    }

    public void setActiveGame(User user, Game game) {
        if (game != null && user != null) {
            if (game.getUser().equals(user)) {
                user.setActiveGame(game);
                game.setActive(true);
                userRepository.save(user);
                gameRepository.save(game);
            }
        }
    }

    public Game getGameById(Long id) {
        if (id == null)
            return null;
        return gameRepository.findOne(id);
    }

    public void load(Long id) throws TalosQuestsException {
        Game game = getGameById(id);
        if (game == null)
            throw new TalosQuestsException("Game with this id not found");

        game.getUser().setActiveGame(game);

        userRepository.save(game.getUser());
    }

    public Game getActiveGameByUser(User user) {
        if (user == null)
            return null;
        return user.getActiveGame();
    }

    public Quest getQuest(Game game) {
        if (game == null)
            return null;
        return game.getActiveQuest();
    }

    public Boolean submitQuestAnswer(Game game, QuestChoice choice) {
        if (game == null)
            return false;

        Quest quest = getQuest(game);

        if (quest == null)
            return false;

        return choice.equals(quest.getQuest().getCorrectChoice());

    }

    public void finishQuest(Game game, boolean state) {
        Quest quest = game.getActiveQuest();
        if (quest != null) {
            game.getIncompleteQuests().remove(quest);
            game.getCompletedQuests().add(quest);
            quest.complete(state);
            game.setActiveQuest(null);
        }
    }

    public Quest getNextQuest(Game game) {
        Quest c = null;
        if (game.getIncompleteQuests().size() > 0) {
            c = ((Quest[])game.getIncompleteQuests().toArray())[0];
            c.setGame(game);
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
            return false;
        return game.getIncompleteQuests().size() <= 0;
    }

    public void wipe() {
        gameRepository.deleteAllInBatch();
    }


}
