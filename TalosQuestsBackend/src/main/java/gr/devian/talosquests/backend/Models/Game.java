package gr.devian.talosquests.backend.Models;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.LocationProvider.*;
import gr.devian.talosquests.backend.Repositories.*;
import gr.devian.talosquests.backend.Services.LocationService;
import gr.devian.talosquests.backend.Utilities.Tuple;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Nikolas on 2/12/2016.
 */

@Component
@Entity
public class Game {
    @GeneratedValue
    @Id
    private long id;
    private LatLng currentUserLocation;
    private Quest activeQuest;
    private int experiencePoints;
    private ArrayList<Quest> history;
    private static ArrayList<Quest> Quests;


    static {

    }

    @Autowired
    @Transient
    private UserRepository userRepository;

    public ArrayList<Quest> getHistory() {
        return history;
    }

    public static ArrayList<Quest> getQuests() {
        return Quests;
    }

    public Quest getActiveQuest() {
        return activeQuest;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setActiveQuest(Quest activeQuest) {
        this.activeQuest = activeQuest;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public ArrayList<Quest> getAvailableQuests() {
        ArrayList<Quest> availableQuests = new ArrayList<>();

        for (Quest quest : Quests) {
            boolean add = true;
            for (Quest uquest : history) {
                if (uquest.equals(uquest)) {
                    add = false;
                }
            }
            if (add)
                availableQuests.add(quest);
        }
        return availableQuests;
    }

    public void startQuest() throws TalosQuestsLocationProviderServiceUnavailableException, TalosQuestsGameInProgressException {
        if (activeQuest == null) {
            ArrayList<Quest> availableQuests = getAvailableQuests();
            if (availableQuests.size() > 0) {
                try {
                    Tuple<Quest,Location> closestQuestDistance = LocationService.getClosestQuestDistance(currentUserLocation, availableQuests);
                    activeQuest = closestQuestDistance.left;

                } catch (Exception e) {
                    throw new TalosQuestsLocationProviderServiceUnavailableException();
                }
            } else {
                gameFinalize();
            }
        } else {
            throw new TalosQuestsGameInProgressException();
        }
    }

    public void finishQuest(boolean succeed) throws TalosQuestsGameNotInProgressException {
        if (activeQuest != null) {
            activeQuest.finish(succeed);
            activeQuest = null;
        } else {
            throw new TalosQuestsGameNotInProgressException();
        }
    }

    public void gameFinalize() {

    }
}
