package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
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


    public Long getId() {
        return id;
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
        this.passWord = SecurityTools.MD5(passWord + "_saltedPass:" + salt + "_hashedByUsername:" + userName);
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

    }

    public User(String userName, String passWord, String email, String deviceIMEI) {
        salt = SecurityTools.GenerateRandomToken();
        this.userName = userName;
        this.passWord = SecurityTools.MD5(passWord + "_saltedPass:" + salt + "_hashedByUsername:" + userName);
        this.email = email;
        this.deviceIMEI = deviceIMEI;
    }
}
