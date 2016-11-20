package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 19/11/2016.
 */

@RestController
@RequestMapping("/User")
public class UserController {


    private UserRepository userRepository;

    @Autowired
    public void initRepo(UserRepository _userRepository) {
        userRepository = _userRepository;
    }

    @RequestMapping(value="" , method = RequestMethod.GET)
    public Iterable<User> ListUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/{name}")
    public User GetUserById(@PathVariable("name") long id) {
        User usr;
        try {
            usr = userRepository.findOne(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return User.GetEmptyUser();

        }
        if (usr == null) {
            return User.GetEmptyUser();
        }
        else {
            return usr;
        }
    }


    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    public void DeleteUserById(@PathVariable("name") long id) {
        try {
            userRepository.delete(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @RequestMapping(value="/{name}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void UpdateUserById(@PathVariable("name") long id,@RequestBody User user) {
        User usr;
        try {
            usr = userRepository.findOne(id);
            usr.setDisplayName(user.getDisplayName());
            usr.setDeviceImei(user.getDeviceImei());
            usr.setFacebookId(user.getFacebookId());
            userRepository.save(usr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
