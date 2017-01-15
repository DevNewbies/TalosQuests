package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Utilities.Response;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Nikolas on 10/1/2017.
 */
@RestController
@RequestMapping("/Admin/Quest")
public class QuestManagementController extends AdminController {

    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> viewQuestInfo(
            @PathVariable("param") Optional<Long> param,
            @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageQuests())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent()) {
            return Response.success(questService.getAllQuests(), View.Internal.class);
        } else {
            Quest quest = questService.getQuestById(param.get());
            if (quest == null)
                return Response.fail("Quest not found.", 404);
            return Response.success(quest, View.Internal.class);
        }
    }

    @RequestMapping(value = {"", "/{param}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteQuest(
            @PathVariable("param") Optional<Long> param,
            @RequestParam(value = "password", required = false) Optional<String> password,
            @RequestParam(value = "token", required = true) String token) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageQuests())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        if (!param.isPresent()) {
            if (!session.getUser().getAccess().getCanWipeQuests())
                return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

            if (!password.isPresent())
                return Response.fail("You are about to wipe all quests on database. You must provide your password", HttpStatus.FORBIDDEN);

            if (!param.isPresent() && !session.getUser().hashStr(password.get()).equals(session.getUser().getPassWord()))
                return Response.fail("Incorrect Password.", HttpStatus.FORBIDDEN);

            questService.wipe(session.getUser());
            return Response.success("Success.");
        } else {
            Quest quest = questService.getQuestById(param.get());
            if (quest == null)
                return Response.fail("Quest not found.", 404);
            questService.delete(session.getUser(), quest);
            return Response.success("Success.");
        }
    }

    @RequestMapping(value = {"/{param}"}, method = RequestMethod.PUT)
    public ResponseEntity<Object> editQuestInfo(
            @PathVariable("param") Long param,
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Quest model) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageQuests())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        Quest quest = questService.getQuestById(param);
        if (quest == null)
            return Response.fail("Quest not found.", 404);

        return Response.success(questService.update(session.getUser(), quest, model));
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public ResponseEntity<Object> addQuest(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Quest model) throws TalosQuestsException {
        Session session = sessionService.getByToken(token);
        if (session == null)
            return Response.fail("Token is not valid", HttpStatus.UNAUTHORIZED);

        if (!session.getUser().getAccess().getCanManageQuests())
            return Response.fail("Access Denied", HttpStatus.FORBIDDEN);

        return Response.success(questService.create(session.getUser(), model));
    }
}
