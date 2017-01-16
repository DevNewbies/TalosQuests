package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.AbstractControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 16/1/2017.
 */
@Transactional
public class GameManagementControllerTests extends AbstractControllerTest {

    @Test
    public void testListGamesWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Admin/Game")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListGameWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(get("/Admin/Game")
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListGamesWithValidTokenWithPermissions() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response", hasSize(2)))
                .andReturn();
    }

    @Test
    public void testListGamesWithValidTokenWithPermissionsAndValidUserIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/"+testGameForUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListGamesWithValidTokenWithPermissionsAndInvalidUserIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testListGamesByUserWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Admin/Game/User/"+testUserWithSession.getId())
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListGameByUserWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(get("/Admin/Game/User/"+testUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListGamesByUserWithValidTokenWithPermissionsWithValidUserIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/User/"+testUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response", hasSize(1)))
                .andReturn();
    }

    @Test
    public void testListGamesByUserWithValidTokenWithPermissionsWithInvalidUserIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/User/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListGamesByUserWithValidTokenWithPermissionsAndValidUserIdSpecifiedWithInvalidGameIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/User/"+testUserWithSession.getId()+"/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListGamesByUserWithValidTokenWithPermissionsAndValidUserIdSpecifiedWithOtherUsersGameIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/User/"+testUserWithSession.getId()+"/"+testGameForUserWithoutSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListGamesByUserWithValidTokenWithPermissionsAndValidUserIdSpecifiedWithValidGameIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Game/User/"+testUserWithSession.getId()+"/"+testGameForUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testDeleteGamesWithInvalidToken() throws Exception {

        mockMvc.perform(delete("/Admin/Game")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeGamesWithValidTokenAndPasswordButNoPermissions() throws Exception {
        accessService.setUserAccessLevel(testUserWithSession,"Admin");
        assertTrue(testUserWithSession.getAccess().getCanBanUsers());
        mockMvc.perform(delete("/Admin/Game")
                .param("token",testSession.getToken())
                .param("password",passWordPassing))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testDeleteGameWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(delete("/Admin/Game/" + testGameForUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeGameWithValidTokenWithPermissionsWithoutPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Game")
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeGameWithValidTokenWithPermissionsWithIncorrectPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Game")
                .param("token",testSession.getToken())
                .param("password","invalid"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeGameWithValidTokenWithPermissionsWithCorrectPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Game")
                .param("token",testSession.getToken())
                .param("password",passWordPassing))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteGameWithValidTokenWithPermissionsWithInvalidGameId() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Game/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testDeleteUserWithValidTokenWithPermissionsWithValidGameId() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Game/"+testGameForUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


}
