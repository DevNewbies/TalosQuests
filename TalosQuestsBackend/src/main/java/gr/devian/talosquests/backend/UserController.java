package gr.devian.talosquests.backend;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by Nikolas on 19/11/2016.
 */

@RestController
@RequestMapping("/User")
public class UserController {


    static ArrayList<UserModel> users = new ArrayList<UserModel>();
    static {
        users.add(new UserModel("Test","test"));
        users.add(new UserModel("Chrysa","Tsinou"));
        users.add(new UserModel("Nikolas","Mavropoylos"));
        users.add(new UserModel("Nikos","Alumaras"));
        users.add(new UserModel("Iordanis","Kostelidis"));
        users.add(new UserModel("Antonis","Misirgis"));
        users.add(new UserModel("Kwstas","Giatsios"));
        users.add(new UserModel("Fillipos","Theoxaridis"));

    }

    @RequestMapping(value="" , method = RequestMethod.GET)
    public ArrayList<UserModel> ServiceInfo() {
        return users;
    }

    @RequestMapping("/{name}")
    public UserModel GetUserById(@PathVariable("name") long id) {
        for (UserModel u : users) {
            if (id == u.getId()) {
                return u;
            }
        }
        return UserModel.GetEmptyUser();
    }


    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    public void DeleteUserById(@PathVariable("name") long id) {
        for (UserModel u : users) {
            if (id == u.getId()) {
                users.remove(u);
                return;
            }
        }
    }
    @RequestMapping(value="/{name}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void UpdateUserById(@PathVariable("name") long id,@RequestBody UserModel usr) {
        System.out.println(usr.getName());
        for (UserModel u : users) {
            if (id == u.getId()) {
                u.setName(usr.getName());
                u.setSurname(usr.getSurname());
                return;
            }
        }
    }
}
