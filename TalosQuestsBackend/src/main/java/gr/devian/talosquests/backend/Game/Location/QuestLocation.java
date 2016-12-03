package gr.devian.talosquests.backend.Game.Location;

import gr.devian.talosquests.backend.Game.Quest;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Nikolas on 3/12/2016.
 */
@Component
@Entity
public class QuestLocation {
    @Id
    @GeneratedValue
    private long id;
    private LatLng location;
    @OneToOne
    private Quest quest;

    public QuestLocation() {
    }

    public QuestLocation(LatLng location, Quest quest) {
        this.location = location;
        this.quest = quest;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
