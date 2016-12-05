package gr.devian.talosquests.backend.Models;

import javax.persistence.Entity;

/**
 * Created by Xrysa on 19/11/2016.
 */

public class AuthRegisterModel {
    private String userName;
    private String passWord;
    private String email;
    private String imei;

    public AuthRegisterModel(){}

    public AuthRegisterModel(String userName, String passWord, String email, String imei) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.imei = imei;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}