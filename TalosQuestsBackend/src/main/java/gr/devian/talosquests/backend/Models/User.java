package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * Created by Nikolas on 3/12/2016.
 */
@Component
@Entity
@Access(AccessType.FIELD)
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;


    private ArrayList<Game> games;

    @Column(unique=true)
    private String userName;
    @JsonIgnore
    private String passWord;
    private FacebookAccount faceBook;
    @Column(unique=true)
    private String email;
    @JsonIgnore
    private String salt;
    @OneToOne
    @JsonIgnore
    private Game activeGame;
    private String deviceIMEI;

    private LatLng lastLocation;

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
    public ArrayList<Game> getGames() {
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

    public FacebookAccount getFaceBook() {
        return faceBook;
    }

    public void setFaceBook(FacebookAccount faceBook) {
        this.faceBook = faceBook;
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
    }
    public String hashStr(String str) {
        return SecurityTools.MD5(str + "_saltedPass:" + salt + "_hashedByUsername:" + userName);
    }
    public User(String userName, String passWord, String email, String deviceIMEI) {
        games = new ArrayList<>();
        salt = SecurityTools.GenerateRandomToken();
        this.userName = userName;
        this.passWord = hashStr(passWord);
        this.email = email;
        this.deviceIMEI = deviceIMEI;
    }
}
