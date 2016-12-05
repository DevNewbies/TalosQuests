package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.ResponseModel;
import gr.devian.talosquests.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel<Session>> GetSession(@RequestParam(value = "token", required = true) String token) {
        Session session = userService.getSessionByToken(token);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.CreateSuccessModel(session));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseModel.CreateFailModel("Token is not valid", 401));
        }
    }
}
