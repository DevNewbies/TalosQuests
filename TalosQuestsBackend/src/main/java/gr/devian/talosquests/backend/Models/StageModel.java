package gr.devian.talosquests.backend.Models;

import com.google.maps.model.LatLng;

import java.util.List;

/**
 * Created by Nikolas on 21/11/2016.
 */
public class StageModel {
    private int modelId;
    private LatLng location;
    private int difficulty;
    private List<QuestModel> quests;
    private int exp;
    private int availableHints;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<QuestModel> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestModel> quests) {
        this.quests = quests;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getAvailableHints() {
        return availableHints;
    }

    public void setAvailableHints(int availableHints) {
        this.availableHints = availableHints;
    }
}
