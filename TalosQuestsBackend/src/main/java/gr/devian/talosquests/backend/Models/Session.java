package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
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
public class Session {
    private static HashMap<String, Session> List = new HashMap<>();
    private static final SecureRandom random = new SecureRandom();
    @GeneratedValue
    @Id
    private long sessionId;
    @OneToOne
    private User user;
    private String token;
    private Date expires;

    @Transient
    @JsonIgnore
    Calendar cal = Calendar.getInstance();

    public long getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Date getExpireDate() {
        return expires;
    }

    public void updateExpirationDate() {
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        expires = cal.getTime();
    }
    public void expire() {
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_WEEK, -7);
        expires = cal.getTime();
    }
    public Session() {
        token = new BigInteger(130, random).toString(32);
        updateExpirationDate();
    }

    public Session(User user) {
        token = new BigInteger(130, random).toString(32);
        updateExpirationDate();
        this.user = user;
    }


}
