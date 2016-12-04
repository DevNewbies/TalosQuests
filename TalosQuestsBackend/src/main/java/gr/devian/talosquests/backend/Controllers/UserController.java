package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Game.UserSession;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Game.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    /*@RequestMapping(value="" , method = RequestMethod.GET)
    public ResponseEntity<ResponseModel<Iterable<User>>> ListUsers() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(userRepository.findAll(),200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(e.getMessage(),500));
        }
}*/


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel<User>> GetUserById(@RequestParam(value = "token", required = true) String token) {
        UserSession session = UserSession.Get(token);
        if (session != null) {
            User usr = session.getUser();
            return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(usr));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));
        }
    }


    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseModel<String>> DeleteUserById(@RequestParam(value = "token", required = true) String token) {
        UserSession session = UserSession.Get(token);
        if (session != null) {
            try {
                userRepository.delete(session.getUser());
                return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel("Deleted"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(e.getMessage(), 500));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<User>> UpdateUserById(@RequestParam(value = "token", required = true) String token, @RequestBody User user) {
        UserSession session = UserSession.Get(token);
        if (session != null) {
            try {
                User usr = session.getUser();
                if (!Strings.isNullOrEmpty(user.getEmail()))
                    usr.setEmail(user.getEmail());
                if (!Strings.isNullOrEmpty(user.getDeviceIMEI()))
                    usr.setDeviceIMEI(user.getDeviceIMEI());
                if (!Strings.isNullOrEmpty(user.getPassWord()))
                    usr.setPassWord(user.getPassWord());
                userRepository.save(usr);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(usr));

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(e.getMessage(), 500));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));
        }
    }
}
