package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by Nikolas on 2/12/2016.
 */

public class QuestChoice implements Serializable {

    @JsonView(View.Simple.class)
    private String content;

    public QuestChoice(String cont) {
        content = cont;
    }

    public QuestChoice() {
        content = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String cont) {
        content = cont;
    }

    @Transient
    public boolean equals(QuestChoice c) {
        return c.getContent().equals(content);
    }
}
