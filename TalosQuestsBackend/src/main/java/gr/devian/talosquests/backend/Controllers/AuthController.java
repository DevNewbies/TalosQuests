package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Game.SecurityTools;
import gr.devian.talosquests.backend.Game.UserSession;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Game.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Repositories.UserSessionRepository;
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
public class AuthController {
    private UserRepository userRepository;


    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    public void initRepo(UserRepository _userRepository) {

        userRepository = _userRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<UserSession>> Authenticate(@RequestBody AuthRegisterModel userModel) {
        if (userModel != null) {
            if (!Strings.isNullOrEmpty(userModel.getUserName()) && !Strings.isNullOrEmpty(userModel.getPassWord())) {
                try {
                    User usr = userRepository.findUserByUserName(userModel.getUserName());
                    if (usr != null) {

                        String saltedPass = userModel.getPassWord() + "_saltedPass:" + usr.getSalt() + "_hashedByUsername:" + userModel.getUserName();
                        String hashedPass = SecurityTools.MD5(saltedPass);

                        if (usr.getPassWord().equals(hashedPass)) {
                            UserSession ses = UserSession.Get(usr);
                            if (ses == null) {
                                sessionRepository.deleteUserSessionByUser(usr);
                                ses = UserSession.Create(usr);
                            }
                            usr.setActiveSession(ses);

                            sessionRepository.save(ses);
                            userRepository.save(usr);

                            return ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body(ResponseModel.CreateSuccessModel(ses));

                        } else {
                            return ResponseEntity
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .body(ResponseModel.CreateFailModel("Wrong User Credentials", 401));
                        }


                    } else {
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ResponseModel.CreateFailModel("No such User", 401));
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
        } else

        {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.CreateFailModel("Insufficient Credentials", 400));
        }
    }
}
