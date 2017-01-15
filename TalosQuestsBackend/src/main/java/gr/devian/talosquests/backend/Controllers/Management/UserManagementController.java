package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Nikolas on 9/1/2017.
 */
@RestController
public class UserManagementController extends AdminController {

    @RequestMapping(value = {"/User", "/Users", "/User/{param}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> DisplayUser(
            @PathVariable("param") Optional<String> param,
            @RequestParam(value = "token", required = true) String token)
            throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        Class<? extends View.Simple> view = session.getUser().getAccess().getCanManageService() ? View.Internal.class : View.Extended.class;

        if (!param.isPresent())
            return Response.success(userService.getAllUsers(), view, 200);
        else {
            try {
                long id = Long.parseLong(param.get());
                User user = userService.getUserById(id);
                if (user == null)
                    return Response.fail("User with this id not found.", 404);

                return Response.success(user, view, 200);
            } catch (NumberFormatException exc) {
                if (param.get().contains("@"))
                    return Response.success(userService.findUsersByEmail("%" + param.get() + "%"), view, 200);
                else
                    return Response.success(userService.findUsersByName("%" + param.get() + "%"), view, 200);
            }
        }
    }

    @RequestMapping(value = {"/User", "/Users", "/User/{param}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> DeleteUser(
            @PathVariable("param") Optional<Long> param,
            @RequestParam(value = "password") Optional<String> password,
            @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent()) {
            if (!session.getUser().getAccess().getCanWipeUsers())
                return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

            if (!password.isPresent())
                return Response.fail("You are about to wipe all users on database. You must provide your password", HttpStatus.FORBIDDEN);

            if (!session.getUser().hashStr(password.get()).equals(session.getUser().getPassWord()))
                return Response.fail("Incorrect Password.", HttpStatus.FORBIDDEN);

            userService.wipe();
            return Response.success(null, 200, "User Database Wiped.");
        } else {
            User user = userService.getUserById(param.get());
            if (user == null)
                return Response.fail("User not found.", HttpStatus.NOT_FOUND);
            if (user.equals(session.getUser()))
                return Response.fail("You cannot delete your self.", HttpStatus.FORBIDDEN);
            userService.delete(user);
            return Response.success(null, 200, "User Deleted.");

        }
    }

    @RequestMapping(value = {"/User/{param}"}, method = RequestMethod.PUT)
    public ResponseEntity<Object> EditUser(@PathVariable("param") Optional<Long> param, @RequestBody AuthRegisterModel model, @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        User user = userService.getUserById(param.get());
        if (user == null)
            return Response.fail("User not found.", HttpStatus.NOT_FOUND);
        Class<? extends View.Simple> view = session.getUser().getAccess().getCanManageService() ? View.Internal.class : View.Extended.class;
        return Response.success(userService.update(session.getUser(), user, model), view);
    }

    @RequestMapping(value = {"/User/SetBannedState/{param}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> BanUser(@PathVariable("param") Long param, @RequestParam(value = "ban", required = true) Boolean ban, @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid.", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanBanUsers())
            return Response.fail("Access Denied.", HttpStatus.FORBIDDEN);

        User user = userService.getUserById(param);
        if (user == null)
            return Response.fail("User not found.", HttpStatus.NOT_FOUND);

        if (user.equals(session.getUser()))
            return Response.fail("You cannot add/remove a ban to/from your self.", HttpStatus.FORBIDDEN);

        userService.setBannedState(session.getUser(), user, ban);

        return Response.success("Success.");
    }
}
