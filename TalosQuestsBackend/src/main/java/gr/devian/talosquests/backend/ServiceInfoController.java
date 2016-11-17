package gr.devian.talosquests.backend;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Nikolas on 13/11/2016.
 */

@RestController
public class ServiceInfoController {

    static class User {
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


        public User(String u, String p) {
            setName(u);
            setSurname(p);
            setId(counter.incrementAndGet());
        }
        public User() {
            setName("");
            setSurname("");
            setId(0);
        }
        public static User GetEmptyUser() {
            User p = new User("","");
            p.id=-1;
            return p;
        }
    }
    static ArrayList<User> users = new ArrayList<User>();
    static {
        users.add(new User("Test","test"));
        users.add(new User("Chrysa","Tsinou"));
        users.add(new User("Nikolas","Mavropoylos"));
        users.add(new User("Nikos","Alumaras"));
        users.add(new User("Iordanis","Kostelidis"));
        users.add(new User("Antonis","Misirgis"));
        users.add(new User("Kwstas","Giatsios"));
        users.add(new User("Fillipos","Theoxaridis"));

    }
    @Autowired(required = true)
    private HttpServletRequest request;

       @RequestMapping("/ServiceInfo")
    public ArrayList<User> ServiceInfo() {
        return users;
    }

    @RequestMapping("/ServiceInfo/{name}")
    public User ServiceInfoWithId(@PathVariable("name") long id) {
        for (User u : users) {
            if (id == u.id) {
                return u;
            }
        }
        return User.GetEmptyUser();
    }


    @RequestMapping(value="/ServiceInfo/{name}", method = RequestMethod.DELETE)
    public void DeleteUser(@PathVariable("name") long id) {
        for (User u : users) {
            if (id == u.id) {
                users.remove(u);
                return;
            }
        }
    }
    @RequestMapping(value="/ServiceInfo/{name}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void UpdateUser(@PathVariable("name") long id,@RequestBody User usr) {
        System.out.println(usr.getName());
        for (User u : users) {
            if (id == u.id) {
                u.setName(usr.getName());
                u.setSurname(usr.getSurname());
                return;
            }
        }
    }


    public String getUserById(String id) {
        if (id.equals("1")) {
            return "Nikolas";
        }
        else {
            return "Chrysa";
        }
    }
}
