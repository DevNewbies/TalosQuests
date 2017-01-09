package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

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
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageOtherUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent())
            return Response.success(userService.getAllUsers(), 200);
        else {
            try {
                long id = Long.parseLong(param.get());
                return Response.success(userService.getUserById(id), 200);
            } catch (NumberFormatException exc) {
                if (param.get().contains("@"))
                    return Response.success(userService.findUsersByEmail("%" + param.get() + "%"), 200);
                else
                    return Response.success(userService.findUsersByName("%" + param.get() + "%"), 200);
            }
        }
    }

    @RequestMapping(value = {"/User", "/Users", "/User/{param}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> DeleteUser(
            @PathVariable("param") Optional<Long> param,
            @RequestParam(value = "password") Optional<String> password,
            @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageOtherUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent() && !session.getUser().getAccess().getCanWipeUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent() && !password.isPresent())
            return Response.fail("You are about to wipe all users on database. You must provide your password", HttpStatus.FORBIDDEN);

        if (!param.isPresent() && !session.getUser().hashStr(password.get()).equals(session.getUser().getPassWord()))
            return Response.fail("Incorrect Password.", HttpStatus.FORBIDDEN);

        if (!param.isPresent()) {
            userService.wipe();
            return Response.success(null, 200, "User Database Wiped.");
        } else {
            User user = userService.getUserById(param.get());
            if (user == null)
                return Response.fail("User not found.", HttpStatus.NOT_FOUND);
            userService.removeUser(user);
            return Response.success(null, 200, "User Deleted.");
        }

    }

    @RequestMapping("/List")
    public ResponseEntity<Object> Doc() {
        for (RequestMappingInfo method : handlerMapping.getHandlerMethods().keySet()) {
            for (String s : method.getPatternsCondition().getPatterns()) {
                logger.warn(s);
            }
        }
        return Response.success(handlerMapping.getHandlerMethods().size());
    }
}
