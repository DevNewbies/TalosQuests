package gr.devian.talosquests.backend.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Utilities.DurationConverter;
import gr.devian.talosquests.backend.Utilities.LatLngConverter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;

/**
 * Created by Nikolas on 2/12/2016.
 */

@Entity
@Component
public class Quest {
    @GeneratedValue
    @Id
    private long id;

    @JsonIgnore
    @Transient
    private Game game = null;

    private Date started = null;
    private Date completed = null;
    @Convert(converter = DurationConverter.class)
    private Duration duration = Duration.ofSeconds(0);
    private Boolean succeed = false;
    private Boolean active = false;
    @Convert(converter = LatLngConverter.class)
    private LatLng location = null;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QuestModel quest;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Quest() {

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void start() {
        this.started = new Date();
        setActive(true);
    }

    public void complete(boolean state) {
        if (getStarted() != null) {
            this.completed = new Date();
            this.duration = Duration.between(started.toInstant(), completed.toInstant());
            setSucceed(state);
            setActive(false);
        }
    }


    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public QuestModel getQuest() {
        return quest;
    }

    public void setQuest(QuestModel quest) {
        this.quest = quest;
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


    public Boolean getSucceed() {
        return succeed;
    }

    public Boolean getActive() {
        return active;
    }

}
