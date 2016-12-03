package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Game.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Xrysa on 21/11/2016.
 */
@RestController
@RequestMapping("/Register")
public class RegisterController {
    private UserRepository userRepository;

    @Autowired
    public void initRepo(UserRepository _userRepository) {

        userRepository = _userRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<User>> Register(@RequestBody User user) {
        ResponseModel<User> resp;
        User usr;
        try {


            /*if (user.getFaceBook() != null) {
                usr = userRepository.findUserByFacebookId(user.getFaceBook().getId());
                if (usr != null) {
                    resp = ResponseModel.CreateFailModel("User found", 404);
                } else {
                    userRepository.save(user);
                    resp = ResponseModel.CreateSuccessModel(user);
                }
            }
            else*/
            if (!Strings.isNullOrEmpty(user.getUserName())) {
                usr = userRepository.findUserByUserName(user.getUserName());
                if (usr != null) {
                    return ResponseEntity.status(HttpStatus.FOUND).body(ResponseModel.CreateFailModel("User found", 302));
                } else {
                    if (!Strings.isNullOrEmpty(user.getEmail()) && !Strings.isNullOrEmpty(user.getPassWord())) {
                        userRepository.save(user);
                        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseModel.CreateSuccessModel(user, 202));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Incomplete registration form", 400));
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Incomplete registration form", 400));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(e.getMessage(), 500));
        }

    }
}