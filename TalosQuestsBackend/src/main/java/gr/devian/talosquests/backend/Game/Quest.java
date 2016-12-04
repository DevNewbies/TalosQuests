package gr.devian.talosquests.backend.Game;

import gr.devian.talosquests.backend.Game.Location.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nikolas on 2/12/2016.
 */

public class Quest implements Serializable {

    private String name;
    private QuestDifficulty difficulty;
    private QuestChoice choice;
    private ArrayList<QuestChoice> availableChoices;
    private String content;
    private LatLng location;

    public Quest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuestDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuestDifficulty difficulty) {
        this.difficulty = difficulty;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
