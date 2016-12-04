package gr.devian.talosquests.backend.Game.Location;

import gr.devian.talosquests.backend.Game.Quest;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Created by Nikolas on 2/12/2016.
 */

public class QuestDistance implements Serializable {

    private long id;
    private Quest quest;
    private Distance distance;
    private Duration duration;
    private LatLng originCoordinates;
    private LatLng destinationCoordinates;

    public QuestDistance() {
    }

    public QuestDistance(Distance distance, Duration duration, LatLng originCoordinates, LatLng destinationCoordinates, Quest quest) {
        this.distance = distance;
        this.duration = duration;
        this.originCoordinates = originCoordinates;
        this.destinationCoordinates = destinationCoordinates;
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }

    public Duration getDuration() {
        return duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public LatLng getOriginCoordinates() {
        return originCoordinates;
    }

    public LatLng getDestinationCoordinates() {
        return destinationCoordinates;
    }
}
