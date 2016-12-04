package gr.devian.talosquests.backend.Game;


import gr.devian.talosquests.backend.Game.Location.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

/**
 * Created by Nikolas on 2/12/2016.
 */

public class UserQuest implements Serializable {

    private long id;
    private Quest quest;
    private Date started;
    private Date completed;
    private Duration duration;
    private Boolean succeed;
    private Boolean active;
    private QuestDistance questDistance;

    public UserQuest(Quest quest, QuestDistance questDistance) {
        this.quest = quest;
        this.questDistance = questDistance;
        started = new Date();
        active = true;
        succeed = false;
        duration = Duration.ofSeconds(0);
        completed = null;
    }

    public Quest getQuest() {
        return quest;
    }

    public Date getStarted() {
        return started;
    }

    public Date getCompleted() {
        return completed;
    }

    public Duration getDuration() {
        return duration;
    }

    public QuestDistance getQuestDistance() {
        return questDistance;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public Boolean getActive() {
        return active;
    }

    public LatLng getLocation() {
        return questDistance.getQuest().getLocation();
    }

    public void finish(Boolean succeed) {
        this.succeed = succeed;
        completed = new Date();
    }
}
