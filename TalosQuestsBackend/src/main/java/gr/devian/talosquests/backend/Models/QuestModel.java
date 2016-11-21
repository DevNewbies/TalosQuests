package gr.devian.talosquests.backend.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Nikolas on 21/11/2016.
 */
public class QuestModel {
    private int questId;
    private String content;
    private List<QuestChoice> choices;
    private QuestChoice correctChoice;

    private Random randomGenerator;

    public QuestModel() {
        choices = new ArrayList<>();
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<QuestChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestChoice> choices) {
        this.choices = choices;
    }

    public QuestChoice getCorrectChoice() {
        return correctChoice;
    }

    public void setCorrectChoice(QuestChoice correctChoice) {
        this.correctChoice = correctChoice;
    }


    public void useFiftyFiftyHint() {
        int index = randomGenerator.nextInt(choices.size());
        QuestChoice randomChoice = choices.get(index);
        choices.clear();
        choices.add(correctChoice);
        choices.add(randomChoice);
        long seed = System.nanoTime();
        Collections.shuffle(choices,new Random(seed));
    }
}
