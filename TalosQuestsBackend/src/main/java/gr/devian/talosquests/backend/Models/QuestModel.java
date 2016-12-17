package gr.devian.talosquests.backend.Models;

import gr.devian.talosquests.backend.LocationProvider.LatLng;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Nikolas on 15/12/2016.
 */

@Component
@Entity
public class QuestModel {
    @GeneratedValue
    @Id
    private long id;

    private String name;
    private String content;

    private LatLng location;


    private QuestChoice correctChoice;


    private ArrayList<QuestChoice> availableChoices;

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public QuestChoice getCorrectChoice() {
        return correctChoice;
    }


    public QuestModel() {
        availableChoices = new ArrayList<>();
    }

    public ArrayList<QuestChoice> getAvailableChoices() {
        return availableChoices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setCorrectChoice(QuestChoice correctChoice) {
        this.correctChoice = correctChoice;
    }

    public void setAvailableChoices(ArrayList<QuestChoice> availableChoices) {
        this.availableChoices = availableChoices;
    }
}
