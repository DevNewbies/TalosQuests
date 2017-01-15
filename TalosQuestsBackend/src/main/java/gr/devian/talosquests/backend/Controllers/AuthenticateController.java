package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nikolas on 20/11/2016.
 */
@RestController
@RequestMapping("/Auth")
public class AuthenticateController extends BaseController {
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> Authenticate(@RequestBody AuthRegisterModel userModel) throws TalosQuestsNullArgumentException {

        if ((Strings.isNullOrEmpty(userModel.getUserName()) && Strings.isNullOrEmpty(userModel.getEmail())) || Strings.isNullOrEmpty(userModel.getPassWord()))
            return Response.fail("Insufficient Credentials.", HttpStatus.BAD_REQUEST);

        User user = null;
        if (!Strings.isNullOrEmpty(userModel.getUserName()))
            user = userService.getUserByUsername(userModel.getUserName());
        else if (!Strings.isNullOrEmpty(userModel.getEmail()))
            user = userService.getUserByEmail(userModel.getEmail());

        if (user == null)
            return Response.fail("Incorrect User Credentials.", HttpStatus.UNAUTHORIZED);


        String hashedPass = user.hashStr(userModel.getPassWord());

        if (!user.getPassWord().equals(hashedPass))
            return Response.fail("Incorrect User Credentials.", HttpStatus.UNAUTHORIZED);
        Session session = sessionService.getByUser(user);

        if (user.getBanned())
            return Response.fail("User is banned.", HttpStatus.FORBIDDEN);

        if (session == null)
            session = sessionService.create(user);

        return Response.success(session, View.Simple.class, "Authenticated.");


    }
}
