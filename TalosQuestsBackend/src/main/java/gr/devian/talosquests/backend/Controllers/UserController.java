package gr.devian.talosquests.backend.Controllers;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsAccessViolationException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullSessionException;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 19/11/2016.
 */

@RestController
@RequestMapping("/User")
public class UserController extends BaseController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> GetUserById(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        return Response.success(user,View.Extended.class);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<Object> DeleteUserById(@RequestParam(value = "token", required = true) String token, @RequestParam(value = "password", required = true) String password) throws TalosQuestsException {

        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        String hashedPass = session.getUser().hashStr(password);

        if (!session.getUser().getPassWord().equals(hashedPass))
            return Response.fail("Incorrect User Credentials", HttpStatus.UNAUTHORIZED);

        try {
            User user = userService.delete(session.getUser());
            return Response.success(user, View.Extended.class, HttpStatus.OK, "Deleted");
        } catch (TalosQuestsAccessViolationException exc) {
            return Response.fail(exc.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @JsonView(View.Simple.class)
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> UpdateUserById(@RequestParam(value = "token", required = true) String token, @RequestParam(value = "password", required = true) String password, @RequestBody(required = true) AuthRegisterModel model) throws TalosQuestsException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        String hashedPass = session.getUser().hashStr(password);

        if (!session.getUser().getPassWord().equals(hashedPass))
            return Response.fail("Incorrect User Credentials", HttpStatus.UNAUTHORIZED);

        try {
            User user = userService.update(session.getUser(), model);
            return Response.success(user, View.Extended.class, 200, "Updated");
        } catch (TalosQuestsCredentialsNotMetRequirementsException e) {
            return Response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (TalosQuestsAccessViolationException exc) {
            return Response.fail(exc.getMessage(), HttpStatus.FORBIDDEN);
        }

    }
}
