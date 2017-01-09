package gr.devian.talosquests.backend.Models;

import gr.devian.talosquests.backend.Utilities.Tuple;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by Nikolas on 9/1/2017.
 */

public class AccessLevel {
    private Long id;

    private String name;
    private Boolean canCreateOwnGame;
    private Boolean canDeleteOwnGame;
    private Boolean canEditOwnGame;
    private Boolean canEditOwnData;
    private Boolean canDeleteOwnData;
    private Boolean canManageOtherUsers;
    private Boolean canDeleteOtherUsers;
    private Boolean canAddQuests;
    private Boolean canRemoveQuests;
    private Boolean canEditQuests;
    private Boolean canWipeUsers;
    private Boolean canWipeGames;
    private Boolean canBanUsers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanCreateOwnGame() {
        return canCreateOwnGame;
    }

    public void setCanCreateOwnGame(Boolean canCreateOwnGame) {
        this.canCreateOwnGame = canCreateOwnGame;
    }

    public Boolean getCanDeleteOwnGame() {
        return canDeleteOwnGame;
    }

    public void setCanDeleteOwnGame(Boolean canDeleteOwnGame) {
        this.canDeleteOwnGame = canDeleteOwnGame;
    }

    public Boolean getCanEditOwnGame() {
        return canEditOwnGame;
    }

    public void setCanEditOwnGame(Boolean canEditOwnGame) {
        this.canEditOwnGame = canEditOwnGame;
    }

    public Boolean getCanEditOwnData() {
        return canEditOwnData;
    }

    public void setCanEditOwnData(Boolean canEditOwnData) {
        this.canEditOwnData = canEditOwnData;
    }

    public Boolean getCanDeleteOwnData() {
        return canDeleteOwnData;
    }

    public void setCanDeleteOwnData(Boolean canDeleteOwnData) {
        this.canDeleteOwnData = canDeleteOwnData;
    }

    public Boolean getCanManageOtherUsers() {
        return canManageOtherUsers;
    }

    public void setCanManageOtherUsers(Boolean canManageOtherUsers) {
        this.canManageOtherUsers = canManageOtherUsers;
    }

    public Boolean getCanDeleteOtherUsers() {
        return canDeleteOtherUsers;
    }

    public void setCanDeleteOtherUsers(Boolean canDeleteOtherUsers) {
        this.canDeleteOtherUsers = canDeleteOtherUsers;
    }

    public Boolean getCanAddQuests() {
        return canAddQuests;
    }

    public void setCanAddQuests(Boolean canAddQuests) {
        this.canAddQuests = canAddQuests;
    }

    public Boolean getCanRemoveQuests() {
        return canRemoveQuests;
    }

    public void setCanRemoveQuests(Boolean canRemoveQuests) {
        this.canRemoveQuests = canRemoveQuests;
    }

    public Boolean getCanEditQuests() {
        return canEditQuests;
    }

    public void setCanEditQuests(Boolean canEditQuests) {
        this.canEditQuests = canEditQuests;
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

    static {
        AccessLevel level = new AccessLevel();
        level.setCanAddQuests(true);
        level.setCanBanUsers(true);
        level.setCanCreateOwnGame(true);
        level.setCanDeleteOtherUsers(true);
        level.setCanDeleteOwnData(true);
        level.setName("Root");
        level.setCanWipeUsers(true);
        level.setCanWipeGames(true);
        level.setCanRemoveQuests(true);
        level.setCanManageOtherUsers(true);
        level.setCanEditQuests(true);
        level.setCanEditOwnGame(true);
        level.setCanDeleteOwnGame(true);
        level.setCanEditOwnData(true);

        Root = level;

        level = new AccessLevel();
        level.setCanAddQuests(true);
        level.setCanBanUsers(true);
        level.setCanCreateOwnGame(true);
        level.setCanDeleteOtherUsers(true);
        level.setCanDeleteOwnData(true);
        level.setName("Admin");
        level.setCanWipeUsers(false);
        level.setCanWipeGames(false);
        level.setCanRemoveQuests(true);
        level.setCanManageOtherUsers(true);
        level.setCanEditQuests(true);
        level.setCanEditOwnGame(true);
        level.setCanDeleteOwnGame(true);
        level.setCanEditOwnData(true);
        Admin = level;

        level = new AccessLevel();
        level.setCanAddQuests(false);
        level.setCanBanUsers(false);
        level.setCanCreateOwnGame(true);
        level.setCanDeleteOtherUsers(false);
        level.setCanDeleteOwnData(true);
        level.setName("User");
        level.setCanWipeUsers(false);
        level.setCanWipeGames(false);
        level.setCanRemoveQuests(false);
        level.setCanManageOtherUsers(false);
        level.setCanEditQuests(false);
        level.setCanEditOwnGame(true);
        level.setCanDeleteOwnGame(true);
        level.setCanEditOwnData(true);
        User = level;
    }

    public static AccessLevel Root;
    public static AccessLevel Admin;
    public static AccessLevel User;

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof AccessLevel)) {
            return false;
        }
        AccessLevel obj = (AccessLevel) other;
        return Objects.equals(other, obj);
    }
}
