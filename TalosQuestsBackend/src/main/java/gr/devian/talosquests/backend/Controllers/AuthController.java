package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Models.AuthModel;
import gr.devian.talosquests.backend.Models.AuthToken;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 20/11/2016.
 */
@RestController
@RequestMapping("/Auth")
public class AuthController {
    private UserRepository userRepository;

    @Autowired
    public void initRepo(UserRepository _userRepository) {

        userRepository = _userRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel<AuthToken> Authenticate(@RequestBody AuthModel model) {
        User usr;
        ResponseModel<AuthToken> resp;
        if (model != null) {
            if (!Strings.isNullOrEmpty(model.getImei())) {
                if (model.getFacebookId() != 0) {
                    usr = userRepository.findUserByFacebookId(model.getFacebookId());
                    if (usr != null) {
                        if (usr.getDeviceImei().equals(model.getImei())) {
                            resp = ResponseModel.CreateSuccessModel(new AuthToken());
                        }
                        else {
                            resp = ResponseModel.CreateFailModel("Incorrect Credentials",401);
                        }
                    }
                    else {
                        resp = ResponseModel.CreateFailModel("No User found with these Credentials",404);
                    }
                }
                else if (!Strings.isNullOrEmpty(model.getUserName()) && !Strings.isNullOrEmpty(model.getPassWord())) {
                    usr = userRepository.findUserByUsername(model.getUserName());
                    if (usr != null) {
                        if (usr.getPassword().equals(model.getPassWord())) {
                            resp = ResponseModel.CreateSuccessModel(new AuthToken());
                        }
                        else {
                            resp = ResponseModel.CreateFailModel("Incorrect Credentials",403);
                        }
                    }
                    else {
                        resp = ResponseModel.CreateFailModel("No User found with these Credentials",404);
                    }
                }
                else if (!Strings.isNullOrEmpty(model.getToken())) {
                    usr = userRepository.findUserByAccessToken(model.getToken());
                    if (usr != null) {
                        if (usr.getAccessToken().equals(model.getToken()) && usr.getDeviceImei().equals(model.getImei())) {
                            resp = ResponseModel.CreateSuccessModel(new AuthToken());
                        }
                        else {
                            resp = ResponseModel.CreateFailModel("Incorrect Credentials",403);
                        }
                    }
                    else {
                        resp = ResponseModel.CreateFailModel("No User found with these Credentials",404);
                    }
                }
                else {
                    resp = ResponseModel.CreateFailModel("Authentication Credentials are Incomplete",403);
                }
            }
            else {
                resp = ResponseModel.CreateFailModel("Authentication Credentials are Incomplete",403);
            }
        }
        else {
            resp = ResponseModel.CreateFailModel("No Authentication Credentials Given",500);
        }
        return resp;
    }
}
