package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 20/11/2016.
 */
@RestController
@RequestMapping("/Auth")
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<Session>> Authenticate(@RequestBody AuthRegisterModel userModel) {

        if (
                (!Strings.isNullOrEmpty(userModel.getUserName()) || !Strings.isNullOrEmpty(userModel.getEmail()))
                        && !Strings.isNullOrEmpty(userModel.getPassWord())) {
            try {
                User user = null;
                if (!Strings.isNullOrEmpty(userModel.getUserName()))
                    user = userService.getUserByUsername(userModel.getUserName());
                else if (!Strings.isNullOrEmpty(userModel.getEmail()))
                    user = userService.getUserByEmail(userModel.getEmail());

                if (user == null)
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(ResponseModel.CreateFailModel("No such User", 401));


                String saltedPass = userModel.getPassWord() + "_saltedPass:" + user.getSalt() + "_hashedByUsername:" + user.getUserName();
                String hashedPass = SecurityTools.MD5(saltedPass);

                if (user.getPassWord().equals(hashedPass)) {
                    Session session = userService.getSessionByUser(user);
                    if (session == null) {
                        session = userService.createSession(user);
                    }

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(ResponseModel.CreateSuccessModel(session));

                } else {
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(ResponseModel.CreateFailModel("Wrong User Credentials", 401));
                }


            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ResponseModel.CreateFailModel(e.getMessage(), 500));
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.CreateFailModel("Insufficient Credentials", 400));
        }
    }
}
