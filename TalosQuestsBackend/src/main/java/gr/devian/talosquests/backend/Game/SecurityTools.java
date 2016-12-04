package gr.devian.talosquests.backend.Game;

import gr.devian.talosquests.backend.Models.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Nikolas on 4/12/2016.
 */
public class SecurityTools {
    private static final SecureRandom random = new SecureRandom();
    public static String MD5(String value) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(value.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                sb.append(String.format("%02x", array[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String GenerateRandomToken() {
        return new BigInteger(130, random).toString(32);
    }
}
