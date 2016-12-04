package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Game.User;
import gr.devian.talosquests.backend.Game.UserSession;
import gr.devian.talosquests.backend.Models.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nikolas on 4/12/2016.
 */
@RestController
@RequestMapping("/Session")
public class SessionController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel<UserSession>> GetSession(@RequestParam(value = "token", required = true) String token) {
        UserSession session = UserSession.Get(token);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(session));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));
        }
    }
}
