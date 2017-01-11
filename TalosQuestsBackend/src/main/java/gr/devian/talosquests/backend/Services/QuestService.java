package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.GameRepository;
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
    private GameRepository gameRepository;

    public List<QuestModel> listQuests() {
        return questRepository.findAll();
    }

    public QuestModel getQuestById(Long id) {
        if (id == null)
            return null;
        return questRepository.findOne(id);
    }

    public QuestModel create(User originUser, QuestModel model) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (model == null)
            throw new TalosQuestsNullArgumentException("model");

        if (!originUser.getAccess().getCanManageQuests())
            throw new TalosQuestsAccessViolationException("User ha no permission adding quests.");

        model.setId(null);

        return questRepository.save(model);

    }

    public void delete(User originUser, QuestModel model) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (model == null)
            throw new TalosQuestsNullArgumentException("model");
        if (!originUser.getAccess().getCanManageQuests())
            throw new TalosQuestsAccessViolationException("User has no permission deleting quests.");

        for (Game game : gameRepository.findAll()) {
            ArrayList<Quest> quests = new ArrayList<>(game.getCompletedQuests());

            for (Quest quest : quests) {
                if (quest.getQuest().equals(model)) {
                    game.getCompletedQuests().remove(quest);
                }
            }

            quests = new ArrayList<>(game.getIncompleteQuests());
            for (Quest quest : quests) {
                if (quest.getQuest().equals(model)) {
                    game.getIncompleteQuests().remove(quest);

                }
            }

            if (game.getActiveQuest() != null && game.getActiveQuest().getQuest().equals(model))
                game.setActiveQuest(null);

            gameRepository.save(game);


        }

        questRepository.delete(model);

    }

    public QuestModel update(User originUser, QuestModel originalModel, QuestModel newModel) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (originalModel == null)
            throw new TalosQuestsNullArgumentException("originalModel");
        if (newModel == null)
            throw new TalosQuestsNullArgumentException("newModel");
        if (!originUser.getAccess().getCanManageQuests())
            throw new TalosQuestsAccessViolationException("User has no permission editing quests.");

        originalModel.setExp(newModel.getExp());
        originalModel.setLocation(newModel.getLocation());
        originalModel.setName(newModel.getName());
        originalModel.setContent(newModel.getContent());
        originalModel.setAvailableChoices(newModel.getAvailableChoices());
        originalModel.setCorrectChoice(newModel.getCorrectChoice());

        questRepository.save(originalModel);

        return originalModel;
    }

    public void wipe(User originUser) throws TalosQuestsAccessViolationException, TalosQuestsNullArgumentException {
        if (originUser == null)
            throw new TalosQuestsNullArgumentException("originUser");
        if (!originUser.getAccess().getCanWipeQuests())
            throw new TalosQuestsAccessViolationException("User has no permission wiping quests.");

        for (QuestModel model : listQuests()) {
            delete(originUser, model);
        }
    }
}
