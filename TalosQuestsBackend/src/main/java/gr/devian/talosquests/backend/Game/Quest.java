package gr.devian.talosquests.backend.Game;

import gr.devian.talosquests.backend.Game.Location.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

/**
 * Created by Nikolas on 2/12/2016.
 */
@Component
@Entity
public class Quest {
    @Id
    @GeneratedValue
    private long id;

    public Quest getNextQuest() {
        return null;
    }

    public String getName() {
        return null;
    }
    public IQuestDifficulty getQuestDifficulty() {
        return null;
    }
    public IQuestChoice getCorrectChoice() {
        return null;
    }
    public ArrayList<IQuestChoice> getAvailableChoices(){
        return null;
    }
    public String getQuestContent() {
        return null;
    }
    public LatLng getLocation() {
        return null;
    }

}
