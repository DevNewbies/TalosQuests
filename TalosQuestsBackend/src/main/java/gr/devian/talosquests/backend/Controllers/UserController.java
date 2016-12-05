package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 19/11/2016.
 */

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel<User>> GetUserById(@RequestParam(value = "token", required = true) String token) {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));

        User user = session.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(user));

    }


    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseModel<String>> DeleteUserById(@RequestParam(value = "token", required = true) String token, @RequestParam(value = "pass", required = true) String pass) {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));

        String saltedPass = pass + "_saltedPass:" + session.getUser().getSalt() + "_hashedByUsername:" + session.getUser().getUserName();
        String hashedPass = SecurityTools.MD5(saltedPass);

        if (session.getUser().getPassWord() == hashedPass)
            userService.removeUser(session.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel("Deleted"));


    }

    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<User>> UpdateUserById(@RequestParam(value = "token", required = true) String token, @RequestBody AuthRegisterModel model) {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));

        User user = userService.updateUser(session.getUser(),model);
        if (user == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel("Unexpected Internal Errors Occurred. User couldn't be updated.", 500));

        return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(user));


    }
}
