package gr.devian.talosquests.backend.Models;


import gr.devian.talosquests.backend.LocationProvider.*;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nikolas on 2/12/2016.
 */

public class Quest implements Serializable {

    private String name;
    private QuestChoice choice;
    private ArrayList<QuestChoice> availableChoices;
    private String content;
    private Date started;
    private Date completed;
    private Duration duration;
    private Boolean succeed;
    private Boolean active;
    private LatLng location;
    private Distance distance;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Quest(Location location) {
        started = new Date();
        active = true;
        succeed = false;
        duration = Duration.ofSeconds(0);
        completed = null;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuestChoice getChoice() {
        return choice;
    }

    public void setChoice(QuestChoice choice) {
        this.choice = choice;
    }

    public ArrayList<QuestChoice> getAvailableChoices() {
        return availableChoices;
    }

    public void setAvailableChoices(ArrayList<QuestChoice> availableChoices) {
        this.availableChoices = availableChoices;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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


    public void finish(Boolean succeed) {
        this.succeed = succeed;
        completed = new Date();
    }
}
