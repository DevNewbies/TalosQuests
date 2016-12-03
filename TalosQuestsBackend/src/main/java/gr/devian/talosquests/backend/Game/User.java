package gr.devian.talosquests.backend.Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.devian.talosquests.backend.Game.Social.Models.FacebookAccount;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
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
    private long id;


    private ArrayList<Game> games;
    private String userName;
    @JsonIgnore
    private String passWord;
    private FacebookAccount faceBook;
    private String email;
    @JsonIgnore
    private String salt;
    @OneToOne
    private UserSession activeSession;
    @OneToOne
    private Game activeGame;
    private String deviceIMEI;
    private boolean isLoggedIn;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UserSession getActiveSession() {
        return activeSession;
    }

    public void setActiveSession(UserSession activeSession) {
        this.activeSession = activeSession;
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

    public static User create() {
        return null;
    }
    public static boolean update(User usr) {
        try {
            //userRepository.save(usr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
