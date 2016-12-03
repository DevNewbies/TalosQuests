package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Game.UserSession;
import gr.devian.talosquests.backend.Game.UserToken;
import gr.devian.talosquests.backend.Models.AuthModel;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Game.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    public ResponseEntity<ResponseModel<UserSession>> Authenticate(@RequestBody AuthModel model) throws NoSuchAlgorithmException {
        User usr;

        if (model != null) {
            /*if (model.getFacebookId() != 0) {
                usr = userRepository.findUserByFacebookId(model.getFacebookId());
                if (usr != null) {
                    resp = ResponseModel.CreateSuccessModel(new AuthToken());
                } else {
                    resp = ResponseModel.CreateFailModel("No User found with these Credentials", 404);
                }
            } else*/
            if (!Strings.isNullOrEmpty(model.getUserName()) && !Strings.isNullOrEmpty(model.getPassWord())) {
                usr = userRepository.findUserByUserName(model.getUserName());
                if (usr != null) {

                    String saltedPass = model.getPassWord()+"."+usr.getSalt();
                    String hashedPass = "";
                    try {

                        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                        byte[] array = md.digest(saltedPass.getBytes( "UTF-8" ));
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < array.length; i++) {
                            sb.append( String.format( "%02x", array[i]));
                        }
                        hashedPass = sb.toString();
                    } catch ( NoSuchAlgorithmException | UnsupportedEncodingException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(e.getMessage(), 500));
                    }
                    if (usr.getPassWord().equals(hashedPass)) {

                        UserSession sess = UserSession.Create(usr);
                        return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(sess));

                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Wrong User Credentials", 401));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("No such User", 401));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Insufficient Credentials", 400));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseModel.CreateFailModel("Insufficient Credentials", 400));
        }
    }
}
