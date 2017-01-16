package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Nikolas on 16/1/2017.
 */
@RequestMapping("/Admin/Game")
@RestController
public class GameManagementController extends AdminController {

    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> queryGames(
            @PathVariable Optional<Long> param,
            @RequestParam String token)
            throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageGames())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent())
            return Response.success(gameService.getAllGames(), View.Simple.class, 200);
        else {
            Game game = gameService.getGameById(param.get());
            if (game == null)
                return Response.fail("Game with this id not found.", 404);

            return Response.success(game, View.Extended.class, 200);
        }
    }

    @RequestMapping(value = {"/User/{userId}", "/User/{userId}/{gameId}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> queryGamesByUser(
            @PathVariable("userId") Long userId,
            @PathVariable("gameId") Optional<Long> gameId,
            @RequestParam String token)
            throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageGames())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        User user = userService.getUserById(userId);
        if (user == null)
            return Response.fail("User with this id doesn't exists",404);

        if (!gameId.isPresent()) {
            return Response.success(user.getGames(), View.Simple.class, 200);
        } else {
            Game game = gameService.getGameById(gameId.get());

            if (game == null || !user.getGames().contains(game))
                return Response.fail("User doesn't have any game with this id.", 404);

            return Response.success(game, View.Extended.class, 200);
        }
    }

    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteGames(
            @PathVariable Optional<Long> param,
            @RequestParam String token,
            @RequestParam Optional<String> password)
            throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageGames())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent()) {
            if (!session.getUser().getAccess().getCanWipeGames())
                return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

            if (!password.isPresent())
                return Response.fail("You are about to wipe all games on database. You must provide your password", HttpStatus.FORBIDDEN);

            if (!session.getUser().hashStr(password.get()).equals(session.getUser().getPassWord()))
                return Response.fail("Incorrect Password.", HttpStatus.FORBIDDEN);

            gameService.wipe(session.getUser());
            return Response.success(null, 200, "Game Database Wiped.");

        } else {
            Game game = gameService.getGameById(param.get());
            if (game == null)
                return Response.fail("Game with this id not found.", 404);

            gameService.delete(session.getUser(), game);
            return Response.success(null, 200, "Game Deleted.");
        }
    }

}
