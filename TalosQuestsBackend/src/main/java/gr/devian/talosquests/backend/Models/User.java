package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Utilities.LatLngConverter;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Nikolas on 3/12/2016.
 */
@Component
@Entity
@Access(AccessType.FIELD)
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    @JsonView(View.Extended.class)
    private Long id;

    @JsonView(View.Extended.class)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<Game> games;

    @JsonView(View.Simple.class)
    @Column(unique = true)
    private String userName;

    @JsonView(View.Internal.class)
    private String passWord;

    @JsonView(View.Simple.class)
    @Column(unique = true)
    private String email;

    @JsonView(View.Internal.class)
    private String salt;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonView(View.Extended.class)
    private Game activeGame;

    @JsonView(View.Simple.class)
    private Boolean banned;

    @JsonView(View.Extended.class)
    private String deviceIMEI;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = LatLngConverter.class)
    @JsonView(View.Simple.class)
    private LatLng lastLocation;


    @OneToOne
    @JsonView(View.Extended.class)
    private AccessLevel access;

    public LatLng getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LatLng lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Long getId() {
        return id;
    }


    public void addGame(Game g) {
        games.add(g);
    }

    public void removeGame(Game g) {
        if (games.contains(g))
            games.remove(g);
    }

    public Collection<Game> getGames() {
        return games;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = SecurityTools.MD5(passWord + "_saltedPass:" + getSalt() + "_hashedByUsername:" + getUserName());
    }

    public AccessLevel getAccess() {
        return access;
    }

    @JsonView(View.Simple.class)
    public String getAccessLevel() {
        return access.getName();
    }

    public void setAccess(AccessLevel access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public Game getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(Game activeGame) {
        this.activeGame = activeGame;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceImei) {
        this.deviceIMEI = deviceImei;
    }

    public User() {
        games = new ArrayList<>();
        banned = false;
    }

    public String hashStr(String str) {
        return SecurityTools.MD5(str + "_saltedPass:" + salt + "_hashedByUsername:" + userName);
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public User(String userName, String passWord, String email, String deviceIMEI) {
        games = new ArrayList<>();
        salt = SecurityTools.GenerateRandomToken();
        this.userName = userName;
        this.passWord = hashStr(passWord);
        this.email = email;
        this.deviceIMEI = deviceIMEI;
        this.banned = false;
    }
}
