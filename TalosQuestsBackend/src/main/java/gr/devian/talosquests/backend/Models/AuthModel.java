package gr.devian.talosquests.backend.Models;

/**
 * Created by Xrysa on 19/11/2016.
 */
public class AuthModel {
    private long facebookId;
    private String userName;
    private String passWord;
    private String token;
    private String imei;

    public AuthModel(){}

    public long getFacebookId() {
        return facebookId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getToken() {
        return token;
    }

    public String getImei() {
        return imei;
    }



    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

}