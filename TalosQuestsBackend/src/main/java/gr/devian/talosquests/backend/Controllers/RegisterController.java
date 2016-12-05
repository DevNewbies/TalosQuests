package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Models.User;
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

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<User>> Register(@RequestBody AuthRegisterModel model) {
        if (!Strings.isNullOrEmpty(model.getUserName())) {
            User user = userService.getUserByUsername(model.getUserName());
            if (user != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(ResponseModel.CreateFailModel("User already Exists", 302));

            user = userService.createUser(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseModel.CreateSuccessModel(user, 201));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Incomplete registration form", 400));
        }
    }
}