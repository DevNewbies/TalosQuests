package gr.devian.talosquests.backend.Game;

import gr.devian.talosquests.backend.Game.Location.LatLng;
import gr.devian.talosquests.backend.Game.Location.LocationProvider;
import gr.devian.talosquests.backend.Game.Location.QuestDistance;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
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
    @OneToOne
    private UserQuest activeQuest;
    private int experiencePoints;
    private ArrayList<UserQuest> history;
    private static ArrayList<Quest> Quests;


    static {
        Reflections reflections = new Reflections("gr.devian.talosquests.backend");
        Set<Class<? extends Quest>> classes = reflections.getSubTypesOf(Quest.class);
        for (Class clazz : classes) {
            if (!Modifier.isInterface(clazz.getModifiers())) {
                System.out.println(clazz.getName());
                try {
                    PrintWriter writer = new PrintWriter("C:/Users/Nikolas/test.txt", "UTF-8");
                    writer.println(clazz.getName());
                    writer.close();
                } catch (IOException e) {
                    // do something
                }
            }
        }
    }


    public ArrayList<UserQuest> getHistory() {
        return history;
    }

    public static ArrayList<Quest> getQuests() {
        return Quests;
    }

    public UserQuest getActiveQuest() {
        return activeQuest;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setActiveQuest(UserQuest activeQuest) {
        this.activeQuest = activeQuest;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public ArrayList<Quest> getAvailableQuests() {
        ArrayList<Quest> availableQuests = new ArrayList<>();

        for (Quest quest : Quests) {
            boolean add = true;
            for (UserQuest uquest : history) {
                if (uquest.getQuest().equals(uquest)) {
                    add = false;
                }
            }
            if (add)
                availableQuests.add(quest);
        }
        return availableQuests;
    }

    public void startQuest() throws Exception {
        if (activeQuest == null) {
            ArrayList<Quest> availableQuests = getAvailableQuests();
            if (availableQuests.size() > 0) {
                try {
                    QuestDistance closestQuestDistance = LocationProvider.getClosestQuestDistance(currentUserLocation, availableQuests);
                    activeQuest = new UserQuest(closestQuestDistance.getQuest(), closestQuestDistance);
                } catch (Exception e) {
                    //throw TalosQuestsLocationProviderServiceOffline
                }
            } else {
                gameFinalize();
            }
        } else {
            //throw TalosQuestsGameInProgressException
        }
    }

    public void finishQuest(boolean succeed) {
        if (activeQuest != null) {
            activeQuest.finish(succeed);

            activeQuest = null;
        } else {
            //throw TalosQuestsGameNotInProgressException
        }
    }

    public void gameFinalize() {

    }
}
