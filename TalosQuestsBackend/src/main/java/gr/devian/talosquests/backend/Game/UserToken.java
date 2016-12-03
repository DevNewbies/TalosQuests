package gr.devian.talosquests.backend.Game;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikolas on 3/12/2016.
 */
@Component
@Entity
public class UserToken {
    @GeneratedValue
    @Id
    private long id;
    private String token;
    private Date expires;

    private static final SecureRandom random = new SecureRandom();

    public UserToken() {
        token = new BigInteger(130, random).toString(32);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        expires = cal.getTime();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}