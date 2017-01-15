package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Utilities.LatLngConverter;
import gr.devian.talosquests.backend.Utilities.QuestChoiceCollectionConverter;
import gr.devian.talosquests.backend.Utilities.QuestChoiceConverter;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * Created by Nikolas on 15/12/2016.
 */

@Component
@Entity
public class Quest {
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @JsonView(View.Extended.class)
    private Long id;

    @JsonView(View.Simple.class)
    private String name;

    @JsonView(View.Simple.class)
    private String content;

    @JsonView(View.Simple.class)
    private int exp;

    @Convert(converter = LatLngConverter.class)
    @JsonView(View.Simple.class)
    private LatLng location;

    @Convert(converter = QuestChoiceConverter.class)
    @JsonView(View.Internal.class)
    private QuestChoice correctChoice;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = QuestChoiceCollectionConverter.class)
    @JsonView(View.Simple.class)
    private ArrayList<QuestChoice> availableChoices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public QuestChoice getCorrectChoice() {
        return correctChoice;
    }


    public Quest() {
        availableChoices = new ArrayList<>();
    }

    public ArrayList<QuestChoice> getAvailableChoices() {
        return availableChoices;
    }

    public void setAvailableChoices(ArrayList<QuestChoice> availableChoices) {
        this.availableChoices = availableChoices;
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

}
