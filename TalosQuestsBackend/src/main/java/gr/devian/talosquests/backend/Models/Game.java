package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.LocationProvider.*;
import gr.devian.talosquests.backend.Services.LocationService;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.Tuple;
import jdk.nashorn.internal.ir.annotations.Ignore;
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

    @Transient
    @JsonIgnore
    @Ignore
    private LatLng currentUserLocation;


    @OneToOne
    @JsonIgnore
    private User user;

    @OneToOne
    private Quest activeQuest;

    private int experiencePoints;

    private boolean active;

    private ArrayList<Quest> completedQuests;
    private ArrayList<Quest> incompleteQuests;


    public Game() {
        currentUserLocation = null;
        user = null;
        activeQuest = null;
        completedQuests = new ArrayList<>();
        incompleteQuests = new ArrayList<>();
    }


    public User getUser() {
        return user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<Quest> getCompletedQuests() {
        return completedQuests;
    }

    public ArrayList<Quest> getIncompleteQuests() {
        return incompleteQuests;
    }

    public LatLng getCurrentUserLocation() {
        return currentUserLocation;
    }

    public void setCurrentUserLocation(LatLng currentUserLocation) {
        this.currentUserLocation = currentUserLocation;
    }

    public void setIncompleteQuests(ArrayList<Quest> incompleteQuests) {
        this.incompleteQuests = incompleteQuests;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

}
