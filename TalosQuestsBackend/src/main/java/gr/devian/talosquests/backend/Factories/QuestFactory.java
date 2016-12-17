package gr.devian.talosquests.backend.Factories;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestModel;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import gr.devian.talosquests.backend.Services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nikolas on 15/12/2016.
 */
@Service
public class QuestFactory {

    @Autowired
    QuestRepository questRepository;

    @Autowired
    LocationService locationService;

    private Random randomGenerator = new Random();

    private LatLng userLocation;


    public void setLocation(LatLng loc) {
        userLocation = loc;
    }



    public ArrayList<Quest> fetchQuestsFromDatabase() {
        ArrayList<Quest> quests = new ArrayList<>();

       for (QuestModel q : questRepository.findAll()) {
            Quest quest = new Quest();
            quest.setQuest(q);
            quest.setLocation(q.getLocation());
            quests.add(quest);
        }
        return quests;
    }

    public ArrayList<Quest> getQuestsInArea() throws TalosQuestsLocationServiceUnavailableException {
        ArrayList<Quest> quests = new ArrayList<>();

        ArrayList<Quest> avail = locationService.getQuestsInRadius(userLocation, fetchQuestsFromDatabase(), 10000);

        Quest q = avail.get(randomGenerator.nextInt(avail.size()));
        if (!quests.contains(q)) {
            quests.add(q);
        }

        return quests;
    }
}
