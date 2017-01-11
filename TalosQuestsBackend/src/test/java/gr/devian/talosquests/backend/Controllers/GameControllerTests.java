package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Services.LocationService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Xrysa on 18/12/2016.
 */
@Transactional
public class GameControllerTests extends AbstractControllerTest {


    @Test
    public void testListGamesWithoutToken() throws Exception {

        mockMvc.perform(get("/Game"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListGamesWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game")
                .param("token", "invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testListGamesWithValidToken() throws Exception {

        mockMvc.perform(get("/Game")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testShowGameWithValidTokenAndValidGame() throws Exception {
        mockMvc.perform(get("/Game/"+testGameForUserWithSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testShowGameWithValidTokenAndInvalidGame() throws Exception {
        mockMvc.perform(get("/Game/"+Long.MAX_VALUE)
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testShowGameWithValidTokenAndOtherUserGame() throws Exception {
        mockMvc.perform(get("/Game/"+testGameForUserWithoutSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testCreateWithoutTokenWithoutLocationModel() throws Exception {

        mockMvc.perform(post("/Game/Create"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testCreateWithInvalidTokenWithoutLocationModel() throws Exception {

        mockMvc.perform(post("/Game/Create")
                .param("token", "invalid")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateWithValidTokenWithoutLocationModel() throws Exception {

        mockMvc.perform(post("/Game/Create")
                .param("token", testSession.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateWithInvalidTokenWithLocationModel() throws Exception {

        mockMvc.perform(post("/Game/Create")
                .param("token", "invalid")
                .content(mapToJson(new LatLng()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }


    @Test
    public void testCreateWithValidTokenWithValidLocationModel() throws Exception {

        mockMvc.perform(post("/Game/Create")
                .param("token", testSession.getToken())
                .content(mapToJson(new LatLng(41.089798, 23.551346)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void testCreateWithValidTokenWithValidLocationModelAndNoQuestsAvailableOnDesiredArea() throws Exception {

        mockMvc.perform(post("/Game/Create")
                .param("token", testSession.getToken())
                .content(mapToJson(testLocationAthens1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testCreateWithValidTokenWithValidLocationModelAndLocationServiceOffline() throws Exception {
        LocationService.enableService = false;
        mockMvc.perform(post("/Game/Create")
                .param("token", testSession.getToken())
                .content(mapToJson(new LatLng(41.089798, 23.551346)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        LocationService.enableService = true;

    }

    @Test
    public void testCreateWithValidTokenWithInvalidLocationModel() throws Exception {

        mockMvc.perform(post("/Game/Create")
                .param("token", testSession.getToken())
                .content(mapToJson(new LatLng()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }


    @Test
    public void testContinueByIdWithInvalidId() throws Exception {

        mockMvc.perform(get("/Game/Continue/-1")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testContinueByIdWithOtherUsersGameId() throws Exception {

        mockMvc.perform(get("/Game/Continue/" + testGameForUserWithoutSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testContinueByIdWithValidId() throws Exception {

        mockMvc.perform(get("/Game/Continue/" + testGameForUserWithSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testContinueByIdWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game/Continue/" + testGameForUserWithSession.getId())
                .param("token", "Invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteByIdWithValidId() throws Exception {

        mockMvc.perform(delete("/Game/" + testGameForUserWithSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteByIdWithValidIdWhenUserHasNoAccess() throws Exception {

        testUserWithSession.getAccess().setCanManageOwnData(false);
        mockMvc.perform(delete("/Game/" + testGameForUserWithSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteAllWhenUserHasNoAccess() throws Exception {

        testUserWithSession.getAccess().setCanManageOwnData(false);
        mockMvc.perform(delete("/Game")
                .param("token", testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteAll() throws Exception {

        mockMvc.perform(delete("/Game")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteByIdWithInvalidToken() throws Exception {

        mockMvc.perform(delete("/Game/" + testGameForUserWithSession.getId())
                .param("token", "Invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testDeleteByIdWithInvalidId() throws Exception {

        mockMvc.perform(delete("/Game/"+Long.MAX_VALUE)
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteByIdWithOtherUsersGameId() throws Exception {

        mockMvc.perform(delete("/Game/" + testGameForUserWithoutSession.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveWithValidToken() throws Exception {

        mockMvc.perform(get("/Game/Active")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveWithValidTokenAndActiveGameSet() throws Exception {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        mockMvc.perform(get("/Game/Active")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testActiveWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game/Active")
                .param("token", "Invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }




}
