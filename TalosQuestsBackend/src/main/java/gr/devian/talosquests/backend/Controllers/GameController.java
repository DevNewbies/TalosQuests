package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Services.GameService;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 17/12/2016.
 */
@RestController
@RequestMapping("/Game")
public class GameController extends BaseController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> listGames(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        return Response.success(user.getGames());
    }

    @RequestMapping(value = "/Create", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(@RequestParam(value = "token", required = true) String token, @RequestBody(required = true) LatLng model) throws TalosQuestsException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();
        user.setLastLocation(model);
        try {
            Game game = gameService.create(user);
            return Response.success(game);
        } catch (TalosQuestsLocationServiceUnavailableException e) {
            return Response.fail("Location Service is unavailable. Game cannot be created", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (TalosQuestsLocationNotProvidedException e) {
            return Response.fail("Location Not Provided. Game cannot be created", HttpStatus.NOT_FOUND);
        } catch (TalosQuestsLocationsNotAvailableException e) {
            return Response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/Continue", method = RequestMethod.GET)
    public ResponseEntity<Object> continueGame(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();
        if (user.getActiveGame() != null)
            return Response.success(user.getActiveGame());
        return Response.fail("There is no active game for the User. You should specify which game you want to continue", 404);

    }

    @RequestMapping(value = "/Continue/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> continueGame(@RequestParam(value = "token", required = true) String token, @PathVariable("id") Long id) throws TalosQuestsNullArgumentException, TalosQuestsAccessViolationException, TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();
        Game game = gameService.getGameById(id);
        if (game == null)
            return Response.fail("No game with this id found", 404);

        if (!user.getGames().contains(game))
            return Response.fail("User doesn't have any game with this id", 404);

        gameService.setActiveGame(user, game);

        return Response.success(game);

    }

    @RequestMapping(value = "/Delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> deleteGame(@RequestParam(value = "token", required = true) String token, @PathVariable("id") Long id) throws TalosQuestsException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();
        Game game = gameService.getGameById(id);
        if (game == null)
            return Response.fail("No game with this id found", 404);

        if (!user.getGames().contains(game))
            return Response.fail("User doesn't have any game with this id", 404);
        try {
            gameService.delete(game);
        } catch (TalosQuestsAccessViolationException exc) {
            return Response.fail(exc.getMessage(), HttpStatus.FORBIDDEN);
        }

        return Response.success("Deleted");

    }

    @RequestMapping(value = "/Active", method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveGame(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();


        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        return Response.success(user.getActiveGame());

    }

    @RequestMapping(value = "/Active/GetQuest", method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveGameQuest(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        if (user.getActiveGame().getActiveQuest() == null)
            return Response.fail("User doesn't have any active quest.", 404);

        return Response.success(gameService.getActiveQuest(user.getActiveGame()), HttpStatus.OK);

    }

    @RequestMapping(value = "/Active/GetNextQuest", method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveGameNextQuest(@RequestParam(value = "token", required = true) String token) throws TalosQuestsNullArgumentException, TalosQuestsNullSessionException, TalosQuestsLocationServiceUnavailableException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        try {
            return Response.success(gameService.getNextQuest(user.getActiveGame()));
        } catch (TalosQuestsLocationsNotAvailableException e) {
            return Response.fail(e.getMessage(), 404);
        }
    }

    @RequestMapping(value = "/Active/SubmitAnswer", method = RequestMethod.POST)
    public ResponseEntity<Object> submitAnswer(@RequestParam(value = "token", required = true) String token, @RequestBody(required = true) QuestChoice choice) throws TalosQuestsNullArgumentException, TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        User user = session.getUser();

        if (user.getActiveGame() == null)
            return Response.fail("User doesn't have any active game.", 404);

        if (user.getActiveGame().getActiveQuest() == null)
            return Response.fail("User doesn't have any active quest.", 404);

        Boolean state = gameService.submitQuestAnswer(user.getActiveGame(), choice);
        gameService.finishQuest(user.getActiveGame(), state);

        return Response.success(state);

    }
}
