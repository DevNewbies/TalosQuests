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

    public Long getId() {
        return id;
    }
    
    public Boolean getCanWipeQuests() {
        return canWipeQuests;
    }

    public void setCanWipeQuests(Boolean canWipeQuests) {
        this.canWipeQuests = canWipeQuests;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanManageOwnData() {
        return canManageOwnData;
    }

    public void setCanManageOwnData(Boolean canManageOwnData) {
        this.canManageOwnData = canManageOwnData;
    }

    public Boolean getCanManageService() {
        return canManageService;
    }


    public void setCanManageService(Boolean canManageService) {
        this.canManageService = canManageService;
    }

    public Boolean getCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(Boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }

    public Boolean getCanManageQuests() {
        return canManageQuests;
    }

    public void setCanManageQuests(Boolean canManageQuests) {
        this.canManageQuests = canManageQuests;
    }

    public Boolean getCanWipeUsers() {
        return canWipeUsers;
    }

    public void setCanWipeUsers(Boolean canWipeUsers) {
        this.canWipeUsers = canWipeUsers;
    }

    public Boolean getCanWipeGames() {
        return canWipeGames;
    }

    public void setCanWipeGames(Boolean canWipeGames) {
        this.canWipeGames = canWipeGames;
    }

    public Boolean getCanBanUsers() {
        return canBanUsers;
    }

    public void setCanBanUsers(Boolean canBanUsers) {
        this.canBanUsers = canBanUsers;
    }

}
