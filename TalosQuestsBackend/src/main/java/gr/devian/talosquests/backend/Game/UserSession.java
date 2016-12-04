package gr.devian.talosquests.backend.Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Nikolas on 3/12/2016.
 */
@Component
@Entity
public class UserSession {
    private static HashMap<String, UserSession> List = new HashMap<>();
    private static final SecureRandom random = new SecureRandom();
    @GeneratedValue
    @Id
    private long sessionId;
    @OneToOne
    private User user;
    private String token;
    private Date expires;

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Date getExpireDate() {
        return expires;
    }

    public UserSession() {
        token = new BigInteger(130, random).toString(32);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        expires = cal.getTime();
    }

    public static UserSession Get(String token) {
        if (List.containsKey(token)) {
            UserSession usrSes = List.get(token);
            if (usrSes.expires.before(new Date())) {
                List.remove(usrSes);
                return null;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_WEEK, 7);
                usrSes.expires = cal.getTime();
                return usrSes;
            }
        }
        return null;
    }
    public static UserSession Get(User usr) {
        for (UserSession userSession : List.values()) {
            if (userSession.getUser().getId() == usr.getId()) {
                if (userSession.expires.before(new Date())) {
                    List.remove(userSession);
                    return null;
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_WEEK, 7);
                    userSession.expires = cal.getTime();
                    return userSession;
                }
            }
        }
        return null;
    }
    public static UserSession Create(User usr) {
        UserSession sess = new UserSession();
        sess.user = usr;
        List.put(sess.getToken(),sess);
        return sess;
    }
}
