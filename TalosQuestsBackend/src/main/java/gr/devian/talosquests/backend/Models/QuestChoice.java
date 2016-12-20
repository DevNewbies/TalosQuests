package gr.devian.talosquests.backend.Models;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by Nikolas on 2/12/2016.
 */

public class QuestChoice implements Serializable {
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
