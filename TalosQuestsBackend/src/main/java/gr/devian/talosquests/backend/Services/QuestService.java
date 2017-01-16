package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.UserQuest;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikolas on 10/1/2017.
 */
@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private UserQuestRepository userQuestRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private GameService gameService;

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public Quest getQuestById(Long id) {
        if (id == null)
            return null;
        return questRepository.findOne(id);
    }

    public Quest create(User originUser, Quest model) throws TalosQuestsException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (model == null)
            throw new TalosQuestsNullArgumentException("model");

        if (!originUser.getAccess().getCanManageQuests())
            throw new TalosQuestsAccessViolationException("User has no permission adding quests.");

        model.setId(null);

        if (locationService.verifyLatLng(model.getLocation()))
            return questRepository.save(model);
        else
            throw new TalosQuestsInvalidLocationException();

    }

    public void delete(User originUser, Long id) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (id == null)
            throw new TalosQuestsNullArgumentException("id");

        delete(originUser, getQuestById(id));
    }

    public void delete(User originUser, Quest quest) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (quest == null)
            throw new TalosQuestsNullArgumentException("quest");

        if (!originUser.getAccess().getCanManageQuests())
            throw new TalosQuestsAccessViolationException("User has no permission deleting quests.");

        for (Game game : gameService.getAllGames()) {
            ArrayList<UserQuest> userQuests = new ArrayList<>(game.getCompletedUserQuests());

            for (UserQuest userQuest : userQuests) {
                if (userQuest.getQuest().equals(quest)) {
                    game.getCompletedUserQuests().remove(userQuest);
                }
            }

            userQuests = new ArrayList<>(game.getIncompleteUserQuests());
            for (UserQuest userQuest : userQuests) {
                if (userQuest.getQuest().equals(quest)) {
                    game.getIncompleteUserQuests().remove(userQuest);

                }
            }

            if (game.getActiveUserQuest() != null && game.getActiveUserQuest().getQuest().equals(quest))
                game.setActiveUserQuest(null);

            gameService.save(game);


        }

        questRepository.delete(quest);

    }

    public Quest update(User originUser, Long questId, Quest newQuest) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (questId == null)
            throw new TalosQuestsNullArgumentException("questId");

        return update(originUser, getQuestById(questId), newQuest);
    }

    public Quest update(User originUser, Quest originalQuest, Quest newQuest) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (originalQuest == null)
            throw new TalosQuestsNullArgumentException("originalQuest");
        if (newQuest == null)
            throw new TalosQuestsNullArgumentException("newQuest");
        if (!originUser.getAccess().getCanManageQuests())
            throw new TalosQuestsAccessViolationException("User has no permission editing quests.");

        originalQuest.setExp(newQuest.getExp());
        originalQuest.setLocation(newQuest.getLocation());
        originalQuest.setName(newQuest.getName());
        originalQuest.setContent(newQuest.getContent());
        originalQuest.setAvailableChoices(newQuest.getAvailableChoices());
        originalQuest.setCorrectChoice(newQuest.getCorrectChoice());

        questRepository.save(originalQuest);

        return originalQuest;
    }

    public void wipe(User originUser) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (!originUser.getAccess().getCanWipeQuests())
            throw new TalosQuestsAccessViolationException("User has no permission wiping quests.");

        wipe();
    }

    public void wipe() throws TalosQuestsNullArgumentException {
        for (Game game : gameService.getAllGames()) {
            game.getCompletedUserQuests().clear();
            game.getIncompleteUserQuests().clear();
            game.setActiveUserQuest(null);
            gameService.save(game);
        }
        userQuestRepository.deleteAll();
        questRepository.deleteAll();
    }

}
