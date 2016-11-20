package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Nikolas on 19/11/2016.
 */
@Component
@Entity
public class User {
    static private final AtomicLong counter = new AtomicLong();


    @Id
    @GeneratedValue
    private long id;
    private long facebookId;
    private String displayName;
    private String username;
    private String password;
    private String deviceImei;
    private String accessToken;

    public long getId() {
        return id;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public String getAccessToken() {
        return accessToken;
    }



    public void setId(long id) {
        this.id = id;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonIgnore
    public String getPassword() {
        return password;

    }
    public User(String d) {
        displayName=d;
    }

    public User() {

    }
    public static User GetEmptyUser() {
        User p = new User();
        return p;
    }
}
