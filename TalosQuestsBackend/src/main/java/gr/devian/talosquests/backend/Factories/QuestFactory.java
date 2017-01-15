package gr.devian.talosquests.backend.Factories;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationsNotAvailableException;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.UserQuest;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
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



    public ArrayList<UserQuest> fetchQuestsFromDatabase() {
        ArrayList<UserQuest> userQuests = new ArrayList<>();

       for (Quest q : questRepository.findAll()) {
            UserQuest userQuest = new UserQuest();
            userQuest.setQuest(q);
            userQuest.setLocation(q.getLocation());
            userQuests.add(userQuest);
        }
        return userQuests;
    }

    public ArrayList<UserQuest> getQuestsInArea() throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
         return locationService.getQuestsInRadius(userLocation, fetchQuestsFromDatabase(), 10000);
    }
}
