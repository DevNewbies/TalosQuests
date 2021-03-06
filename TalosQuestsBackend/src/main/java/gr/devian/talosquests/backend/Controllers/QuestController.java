package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.UserQuest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 10/1/2017.
 */
@RestController
@RequestMapping("/Game/Quest")
public class QuestController extends BaseController {
    @RequestMapping(value = {"","/Active"}, method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveGameQuest(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullArgumentException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        if (user.getActiveGame().getActiveUserQuest() == null)
            return Response.fail("User doesn't have any active quest.", 404);

        return Response.success(gameService.getActiveQuest(user.getActiveGame()), HttpStatus.OK);

    }

    @RequestMapping(value = "/Complete", method = RequestMethod.GET)
    public ResponseEntity<Object> getCompleteQuests(@RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        return Response.success(user.getActiveGame().getCompletedUserQuests(), HttpStatus.OK);

    }

    @RequestMapping(value = "/Incomplete", method = RequestMethod.GET)
    public ResponseEntity<Object> getIncompleteQuests(@RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        return Response.success(user.getActiveGame().getIncompleteUserQuests(), HttpStatus.OK);

    }

    @RequestMapping(value = "/Next", method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveGameNextQuest(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullArgumentException, TalosQuestsNullArgumentException, TalosQuestsLocationServiceUnavailableException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        try {
            UserQuest userQuest = gameService.getNextQuest(user.getActiveGame());
            return Response.success(userQuest);
        } catch (TalosQuestsLocationsNotAvailableException e) {
            return Response.fail(e.getMessage(), 404);
        }
    }

    @RequestMapping(value = "/SubmitAnswer", method = RequestMethod.POST)
    public ResponseEntity<Object> submitAnswer(@RequestParam(value = "token", required = true) String token, @RequestBody(required = true) QuestChoice choice) throws TalosQuestsNullArgumentException, TalosQuestsNullArgumentException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        if (user.getActiveGame().getActiveUserQuest() == null)
            return Response.fail("User doesn't have any active quest.", 404);

        Boolean state = gameService.submitQuestAnswer(user.getActiveGame(), choice);
        gameService.finishQuest(user.getActiveGame(), state);

        return Response.success(state);

    }
}
