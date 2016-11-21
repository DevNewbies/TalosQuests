package gr.devian.talosquests.backend.Models;

/**
 * Created by Nikolas on 21/11/2016.
 */
public class QuestChoice {
    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private int choiceId;
    private String body;
}
