package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Utilities.LatLngConverter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Nikolas on 2/12/2016.
 */

@Component
@Entity
@Access(AccessType.FIELD)
public class Game {
    @GeneratedValue
    @Id
    private long id;

    @JsonIgnore
    @Convert(converter = LatLngConverter.class)
    private LatLng currentUserLocation;

    @JsonIgnore
    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Quest activeQuest;

    private int experiencePoints;

    private boolean active;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<Quest> completedQuests;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<Quest> incompleteQuests;


    public Game() {
        currentUserLocation = null;
        user = null;
        activeQuest = null;
        experiencePoints = 0;
        active = false;
        completedQuests = new ArrayList<>();
        incompleteQuests = new ArrayList<>();
    }

    public long getId() {
        return id;
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

    public Collection<Quest> getCompletedQuests() {
        return completedQuests;
    }

    public Collection<Quest> getIncompleteQuests() {
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
