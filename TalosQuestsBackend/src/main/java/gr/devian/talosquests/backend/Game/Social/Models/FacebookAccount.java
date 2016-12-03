package gr.devian.talosquests.backend.Game.Social.Models;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */

public class FacebookAccount implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    public FacebookAccount() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
