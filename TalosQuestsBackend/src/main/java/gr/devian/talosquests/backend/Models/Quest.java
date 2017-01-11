package gr.devian.talosquests.backend.Models;


import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Utilities.DurationConverter;
import gr.devian.talosquests.backend.Utilities.LatLngConverter;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Nikolas on 2/12/2016.
 */

@Entity
@Component
public class Quest {
    @GeneratedValue
    @Id
    @JsonView(View.Extended.class)
    private long id;

    @JsonView(View.Simple.class)
    private Date started = null;

    @JsonView(View.Simple.class)
    private Date completed = null;

    @Convert(converter = DurationConverter.class)
    @Column(columnDefinition = "TEXT")
    private Duration duration = new Duration(0);

    @JsonView(View.Simple.class)
    private Boolean succeed = false;

    @JsonView(View.Simple.class)
    private Boolean active = false;

    @JsonView(View.Simple.class)
    @Convert(converter = LatLngConverter.class)
    private LatLng location = null;

    @JsonView(View.Simple.class)
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QuestModel quest;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void start() {
        this.started = new Date();
        setActive(true);
    }

    public void complete(boolean state) {
        if (getStarted() != null) {
            this.completed = new Date();
            this.duration.inSeconds = java.time.Duration.between(started.toInstant(), completed.toInstant()).getSeconds();
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
