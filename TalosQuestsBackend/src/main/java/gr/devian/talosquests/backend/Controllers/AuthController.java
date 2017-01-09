package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullSessionException;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.User;
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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> Authenticate(@RequestBody AuthRegisterModel userModel) throws TalosQuestsNullSessionException {

        if ((Strings.isNullOrEmpty(userModel.getUserName()) && Strings.isNullOrEmpty(userModel.getEmail())) || Strings.isNullOrEmpty(userModel.getPassWord()))
            return Response.fail("Insufficient Credentials", HttpStatus.BAD_REQUEST);

        User user = null;
        if (!Strings.isNullOrEmpty(userModel.getUserName()))
            user = userService.getUserByUsername(userModel.getUserName());
        else if (!Strings.isNullOrEmpty(userModel.getEmail()))
            user = userService.getUserByEmail(userModel.getEmail());

        if (user == null)
            return Response.fail("Incorrect User Credentials", HttpStatus.UNAUTHORIZED);

        String hashedPass = user.hashStr(userModel.getPassWord());

        if (!user.getPassWord().equals(hashedPass))
            return Response.fail("Incorrect User Credentials", HttpStatus.UNAUTHORIZED);

        Session session = userService.getSessionByUser(user);
        if (session == null)
            session = userService.createSession(user);

        return Response.success(session,"Authenticated");


    }
}
