package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 19/11/2016.
 */

@RestController
@RequestMapping("/Admin/User")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public void initRepo(UserRepository _userRepository) {

        userRepository = _userRepository;
    }

    @RequestMapping(value="" , method = RequestMethod.GET)
    public ResponseModel<Iterable<User>> ListUsers() {
        ResponseModel<Iterable<User>> resp;
        try {
            resp = ResponseModel.CreateSuccessModel(userRepository.findAll());
        } catch (Exception e) {
            resp = ResponseModel.CreateFailModel(e.getMessage(),500);
        }
        return resp;
    }

    @RequestMapping("/{name}")
    public ResponseModel<User> GetUserById(@PathVariable("name") long id) {
        User usr;
        ResponseModel<User> resp;
        try {
            usr = userRepository.findOne(id);
            if (usr == null) {
                resp = ResponseModel.CreateFailModel("User not found",404);
            }
            else
                resp = ResponseModel.CreateSuccessModel(usr);

        } catch (Exception e) {
            resp = ResponseModel.CreateFailModel(e.getMessage(),500);
        }
        return resp;
    }


    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    public ResponseModel<User> DeleteUserById(@PathVariable("name") long id) {
        ResponseModel<User> resp;
        try {
            userRepository.delete(id);
            resp = ResponseModel.CreateSuccessModel(null);
        } catch (Exception e) {
            resp = ResponseModel.CreateFailModel(e.getMessage(),500);
        }
        return resp;
    }
    @RequestMapping(value="/{name}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel<User> UpdateUserById(@PathVariable("name") long id, @RequestBody User user) {
        System.out.println(user.toString());
        ResponseModel<User> resp;
        User usr;
        try {

            usr = userRepository.findOne(id);
            System.out.println(usr.toString());

            if (usr != null) {
                if (!Strings.isNullOrEmpty(user.getDisplayName()))
                    usr.setDisplayName(user.getDisplayName());
                if (!Strings.isNullOrEmpty(user.getDeviceImei()))
                    usr.setDeviceImei(user.getDeviceImei());
                if (user.getFacebookId() != 0)
                    usr.setFacebookId(user.getFacebookId());
                if (!Strings.isNullOrEmpty(user.getPassword()))
                    usr.setPassword(user.getPassword());
                userRepository.save(usr);
                resp = ResponseModel.CreateSuccessModel(usr);
            }
            else {
                resp = ResponseModel.CreateFailModel("User not found",404);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            resp = ResponseModel.CreateFailModel(e.getMessage(),500);
        }
        return resp;

    }
}
