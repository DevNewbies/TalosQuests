package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Game.SecurityTools;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
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
    public ResponseEntity<ResponseModel<User>> Register(@RequestBody AuthRegisterModel user) {
        if (!Strings.isNullOrEmpty(user.getUserName())) {
            User usr = userRepository.findUserByUserName(user.getUserName());
            if (usr != null) {
                return ResponseEntity.status(HttpStatus.FOUND).body(ResponseModel.CreateFailModel("User already Exists", 302));
            } else {
                if (!Strings.isNullOrEmpty(user.getEmail()) && !Strings.isNullOrEmpty(user.getPassWord())) {
                    User temp = new User();

                    temp.setSalt(SecurityTools.GenerateRandomToken());
                    temp.setUserName(user.getUserName());
                    temp.setEmail(user.getEmail());
                    temp.setDeviceIMEI(user.getImei());
                    temp.setPassWord(SecurityTools.MD5(user.getPassWord() + "_saltedPass:" + temp.getSalt() + "_hashedByUsername:" + user.getUserName()));

                    userRepository.save(temp);
                    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseModel.CreateSuccessModel(temp, 201));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Incomplete registration form", 400));
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Incomplete registration form", 400));
        }
    }
}