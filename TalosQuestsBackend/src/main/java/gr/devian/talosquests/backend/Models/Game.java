package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;
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
    @JsonView(View.Extended.class)
    private long id;

    @JsonIgnore
    @OneToOne
    private User user;

    @JsonView(View.Simple.class)
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private UserQuest activeUserQuest;

    @JsonView(View.Simple.class)
    private int experiencePoints;

    @JsonView(View.Simple.class)
    private boolean active;

    @JsonView(View.Extended.class)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<UserQuest> completedUserQuests;

    @JsonView(View.Extended.class)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<UserQuest> incompleteUserQuests;

    public Game() {
        user = null;
        activeUserQuest = null;
        experiencePoints = 0;
        active = false;
        completedUserQuests = new ArrayList<>();
        incompleteUserQuests = new ArrayList<>();
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

    public Collection<UserQuest> getCompletedUserQuests() {
        return completedUserQuests;
    }

    public Collection<UserQuest> getIncompleteUserQuests() {
        return incompleteUserQuests;
    }

    public LatLng getCurrentUserLocation() {
        return getUser().getLastLocation();
    }

    public void setCurrentUserLocation(LatLng currentUserLocation) {
        if (getCurrentUserLocation() == null || !getCurrentUserLocation().equals(currentUserLocation))
            this.getUser().setLastLocation(currentUserLocation);
    }

    public void setIncompleteUserQuests(ArrayList<UserQuest> incompleteUserQuests) {
        this.incompleteUserQuests = incompleteUserQuests;
    }

    public UserQuest getActiveUserQuest() {
        return activeUserQuest;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setActiveUserQuest(UserQuest activeUserQuest) {
        this.activeUserQuest = activeUserQuest;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

}
