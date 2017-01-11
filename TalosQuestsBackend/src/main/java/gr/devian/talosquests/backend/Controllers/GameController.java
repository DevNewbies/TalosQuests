package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Exceptions.*;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Nikolas on 17/12/2016.
 */
@RestController
@RequestMapping("/Game")
public class GameController extends BaseController {

    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> showGame(
            @PathVariable(value = "param") Optional<Long> param,
            @RequestParam(value = "token", required = true) String token
    ) throws TalosQuestsNullSessionException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (param.isPresent()) {
            Game game = gameService.getGameById(param.get());
            if (game == null)
                return Response.fail("Game not found.", 404);
            if (!game.getUser().equals(session.getUser()))
                return Response.fail("Game not found.", 404);
            return Response.success(game);
        } else {
            User user = session.getUser();
            return Response.success(user.getGames());
        }
    }
    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteGame(
            @RequestParam(value = "token", required = true) String token,
            @PathVariable("param") Optional<Long> param) throws TalosQuestsException {
        Session session = userService.getSessionByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!param.isPresent()) {
            User user = session.getUser();
            Game game = gameService.getGameById(param.get());
            if (game == null)
                return Response.fail("Game not found.", 404);
            if (!user.getGames().contains(game))
                return Response.fail("Game not found.", 404);
            try {
                gameService.delete(game);
                return Response.success("Deleted.");
            } catch (TalosQuestsAccessViolationException exc) {
                return Response.fail(exc.getMessage(), HttpStatus.FORBIDDEN);
            }
        }
        else {
            try {
                gameService.delete(session.getUser());
                return Response.success("Deleted.");
            } catch (TalosQuestsAccessViolationException exc) {
                return Response.fail(exc.getMessage(), HttpStatus.FORBIDDEN);
            }
        }
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

}
