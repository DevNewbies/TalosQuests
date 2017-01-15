package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Nikolas on 15/1/2017.
 */
@RequestMapping("/Admin/Session")
@RestController
public class SessionManagementController extends AdminController {
    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> DisplaySessions(
            @PathVariable("param") Optional<Long> param,
            @RequestParam(value = "token", required = true) String token)
            throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        Class<? extends View.Simple> view = session.getUser().getAccess().getCanManageService() ? View.Internal.class : View.Extended.class;

        if (!param.isPresent())
            return Response.success(sessionService.getAllSessions(), view, 200);
        else {
            Session _session = sessionService.getById(param.get());
            if (_session == null)
                return Response.fail("Session with this id not found", 404);

            return Response.success(_session, view, 200);
        }
    }

    @RequestMapping(value = {"",  "/{param}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> DeleteSession(
            @PathVariable("param") Optional<Long> param,
            @RequestParam(value = "password") Optional<String> password,
            @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageUsers())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent()) {
            if (!session.getUser().getAccess().getCanWipeSessions())
                return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

            if (!password.isPresent())
                return Response.fail("You are about to wipe all sessions on database. You must provide your password", HttpStatus.FORBIDDEN);

            if (!session.getUser().hashStr(password.get()).equals(session.getUser().getPassWord()))
                return Response.fail("Incorrect Password.", HttpStatus.FORBIDDEN);

            sessionService.wipe();
            return Response.success(null, 200, "Session Database Wiped.");
        } else {
            Session _session = sessionService.getById(param.get());
            if (_session == null)
                return Response.fail("User not found.", HttpStatus.NOT_FOUND);

            sessionService.delete(_session);
            return Response.success(null, 200, "Session Deleted.");

        }
    }
}
