package gr.devian.talosquests.backend;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Nikolas on 19/11/2016.
 */
public class UserModel {
    static private final AtomicLong counter = new AtomicLong();
    private long id;
    private String name;
    private String surname;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String _surname) {
        surname = _surname;
    }


    public UserModel(String u, String p) {
        setName(u);
        setSurname(p);
        setId(counter.incrementAndGet());
    }
    public UserModel() {
        setName("");
        setSurname("");
        setId(0);
    }
    public static UserModel GetEmptyUser() {
        UserModel p = new UserModel();
        return p;
    }
}
