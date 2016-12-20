package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Xrysa on 21/11/2016.
 */
@RestController
@RequestMapping("/Register")
public class RegisterController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> Register(@RequestBody AuthRegisterModel model) {
        User user = userService.getUserByUsername(model.getUserName());
        if (user != null)
            return Response.fail("User with these info already exists.", HttpStatus.FOUND);
        try {
            user = userService.createUser(model);

            return Response.success(user, HttpStatus.CREATED, "Created");
        } catch (TalosQuestsCredentialsNotMetRequirementsException e) {
            return Response.fail("Credentials Not Met Requirements. Field: " + e.getField() + " Much Comply with Regex Pattern: " + e.getPattern(), HttpStatus.BAD_REQUEST);
        } catch (TalosQuestsInsufficientUserData e) {
            return Response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}