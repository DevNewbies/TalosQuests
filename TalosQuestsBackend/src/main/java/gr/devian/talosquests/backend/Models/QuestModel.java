package gr.devian.talosquests.backend.Models;

import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Utilities.QuestChoiceConverter;
import gr.devian.talosquests.backend.Utilities.LatLngConverter;
import gr.devian.talosquests.backend.Utilities.QuestChoiceCollectionConverter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;

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

    @Convert(converter = LatLngConverter.class)
    private LatLng location;

    @Convert(converter = QuestChoiceConverter.class)
    private QuestChoice correctChoice;

    @Convert(converter = QuestChoiceCollectionConverter.class)
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
