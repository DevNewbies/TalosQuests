package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullArgumentException;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Utilities.Response;
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
public class SessionController extends BaseController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> GetSession(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        return Response.success(session);

    }
}
