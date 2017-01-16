package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by Nikolas on 9/1/2017.
 */
@Component
@Entity
public class AccessLevel {
    @GeneratedValue
    @Id
    private Long id;

    @JsonView(View.Simple.class)
    private String name = "User";
    @JsonView(View.Simple.class)
    private Boolean canManageOwnData = true;
    @JsonView(View.Simple.class)
    private Boolean canManageUsers = false;
    @JsonView(View.Simple.class)
    private Boolean canManageService = false;
    @JsonView(View.Simple.class)
    private Boolean canManageQuests = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeUsers = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeGames = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeQuests = false;
    @JsonView(View.Simple.class)
    private Boolean canBanUsers = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeSessions = false;
    @JsonView(View.Simple.class)
    private Boolean canManageGames = false;


    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }

    public Boolean getCanManageOwnData() {
        return canManageOwnData;
    }
    public Boolean getCanManageService() {
        return canManageService;
    }
    public Boolean getCanManageUsers() {
        return canManageUsers;
    }
    public Boolean getCanManageQuests() {
        return canManageQuests;
    }
    public Boolean getCanWipeUsers() { return canWipeUsers; }
    public Boolean getCanWipeGames() {
        return canWipeGames;
    }
    public Boolean getCanBanUsers() {
        return canBanUsers;
    }
    public Boolean getCanWipeSessions() {
        return canWipeSessions;
    }
    public Boolean getCanManageGames() {
        return canManageGames;
    }
    public Boolean getCanWipeQuests() {
        return canWipeQuests;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setId(Long id) { this.id = id; }

    public void setCanManageOwnData(Boolean canManageOwnData) {
        this.canManageOwnData = canManageOwnData;
    }
    public void setCanManageService(Boolean canManageService) {
        this.canManageService = canManageService;
    }
    public void setCanManageUsers(Boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }
    public void setCanManageQuests(Boolean canManageQuests) {
        this.canManageQuests = canManageQuests;
    }
    public void setCanWipeUsers(Boolean canWipeUsers) {
        this.canWipeUsers = canWipeUsers;
    }
    public void setCanWipeGames(Boolean canWipeGames) {
        this.canWipeGames = canWipeGames;
    }
    public void setCanBanUsers(Boolean canBanUsers) {
        this.canBanUsers = canBanUsers;
    }
    public void setCanWipeSessions(Boolean canWipeSessions) {
        this.canWipeSessions = canWipeSessions;
    }
    public void setCanManageGames(Boolean canManageGames) {
        this.canManageGames = canManageGames;
    }
    public void setCanWipeQuests(Boolean canWipeQuests) {
        this.canWipeQuests = canWipeQuests;
    }
}
