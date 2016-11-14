package gr.devian.talosquests.backend;

/**
 * Created by Nikolas on 14/11/2016.
 */

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Xrysa
 */
public class User {
    private String name, email;
    private final ArrayList<User> friends = new ArrayList<>();
    private final ArrayList<User> groups = new ArrayList<>();

    public User(){
        name = "this.setName(_name)";
        email = "this.setEmail(_email)";
    }


    // <editor-fold defaultstate="collapsed" desc="Methods">
    public boolean areWeFriends(User user){
        return friends.contains(user);
    }

    public void addNewFriend(User _user){
        if (_user.name != name)
            friends.add(_user);
    }

    public void addUserToGroup(User _groups){
        if (!this.groups.contains(_groups))
            groups.add(_groups);
    }

    public ArrayList mutualFriendsList(User _user){
        ArrayList<User> mutual = new ArrayList<>();
        for (User _name : friends)
            if (_user.name.equals(this.name))
                mutual.add(_name);
        return mutual;
    }

    public void mutualFriends(User _user){
        for (User name : friends)
            if (_user.friends.equals(this.name))
                System.out.println("" +name);
    }

    public void userFriends(){
        for (User name : friends)
            System.out.println("" +name);
    }

    public void userGroups(){
        for (User name : groups)
            System.out.println("" +name);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters">
    public void setName(String _name) {
        name = _name;
    }

    public void setEmail(String _email) {
        email = _email;
    }
    // </editor-fold>
}
