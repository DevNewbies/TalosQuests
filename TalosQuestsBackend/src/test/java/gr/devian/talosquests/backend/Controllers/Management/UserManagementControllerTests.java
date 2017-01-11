package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by Nikolas on 11/1/2017.
 */
@Transactional
public class UserManagementControllerTests extends AbstractControllerTest {
   /*
     mockMvc.perform(put("/User")
                .param("token",testSession.getToken())
                .content(mapToJson(testAuthRegisterModelCreatedWithSession))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    */

    @Test
    public void testListUsersWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Admin/User")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListUsersWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(get("/Admin/User")
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListUsersWithValidTokenWithPermissions() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response", hasSize(2)))
                .andReturn();
    }

    @Test
    public void testListUsersWithValidTokenWithPermissionsAndUserSearchStringSpecified() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User/"+"withSession")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response", hasSize(1)))
                .andReturn();
    }

    @Test
    public void testListUsersWithValidTokenWithPermissionsAndEmailSearchStringSpecified() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User/"+"@test")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response", hasSize(2)))
                .andReturn();
    }

    @Test
    public void testListUsersWithValidTokenWithPermissionsAndIdSearchStringSpecified() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User/"+testUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testDeleteUsersWithInvalidToken() throws Exception {

        mockMvc.perform(delete("/Admin/User")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeUsersWithValidTokenAndPasswordButNoPermissions() throws Exception {
        userService.setAccessLevel(testUserWithSession,userService.getAccessLevelByName("Admin"));
        assertTrue(testUserWithSession.getAccess().getCanBanUsers());
        mockMvc.perform(delete("/Admin/User")
                .param("token",testSession.getToken())
                .param("password",passWordPassing))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testDeleteUsersWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(delete("/Admin/User/" + testUserWithoutSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeUsersWithValidTokenWithPermissionsWithoutPassword() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(delete("/Admin/User")
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeUsersWithValidTokenWithPermissionsWithIncorrectPassword() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(delete("/Admin/User")
                .param("token",testSession.getToken())
                .param("password","invalid"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeUsersWithValidTokenWithPermissionsWithCorrectPassword() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(delete("/Admin/User")
                .param("token",testSession.getToken())
                .param("password",passWordPassing))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteUserWithValidTokenWithPermissionsWithInvalidUserId() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(delete("/Admin/User/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteUserWithValidTokenWithPermissionsWithOwnUserId() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(delete("/Admin/User/"+testUserWithSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteUserWithValidTokenWithPermissionsWithValidUserId() throws Exception {
        testUserWithSession.setAccess(userService.getAccessLevelByName("Root"));
        mockMvc.perform(delete("/Admin/User/"+testUserWithoutSession.getId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testUpdateUserWithInvalidToken() throws Exception {
        mockMvc.perform(put("/Admin/User/"+testUserWithoutSession.getId())
                .param("token","invalid")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testUpdateUserWithValidTokenWithoutPermissions() throws Exception {
        mockMvc.perform(put("/Admin/User/"+testUserWithoutSession.getId())
                .param("token",testSession.getToken())
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testUpdateUserWithValidTokenWithPermissionsWithInvalidUserId() throws Exception {
        userService.setAccessLevel(testUserWithSession, userService.getAccessLevelByName("Root"));
        mockMvc.perform(put("/Admin/User/"+Long.MAX_VALUE)
                .param("token",testSession.getToken())
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testUpdateUserWithValidTokenWithPermissionsWithValidUserId() throws Exception {
        userService.setAccessLevel(testUserWithSession, userService.getAccessLevelByName("Root"));
        mockMvc.perform(put("/Admin/User/"+testUserWithoutSession.getId())
                .param("token",testSession.getToken())
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testSetBanStateWithInvalidToken() throws Exception {
        mockMvc.perform(get("/Admin/User/SetBannedState/"+testUserWithoutSession.getId())
                .param("token","invalid")
                .param("ban","true"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testSetBanStateWithValidTokenWithoutPermissions() throws Exception {
        mockMvc.perform(get("/Admin/User/SetBannedState/"+testUserWithoutSession.getId())
                .param("token",testSession.getToken())
                .param("ban","true"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testSetBanStateWithValidTokenWithPermissionsWithInvalidId() throws Exception {
        userService.setAccessLevel(testUserWithSession, userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User/SetBannedState/"+Long.MAX_VALUE)
                .param("token",testSession.getToken())
                .param("ban","true"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testSetBanStateWithValidTokenWithPermissionsWithOwnId() throws Exception {
        userService.setAccessLevel(testUserWithSession, userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User/SetBannedState/"+testUserWithSession.getId())
                .param("token",testSession.getToken())
                .param("ban","true"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testSetBanStateWithValidTokenWithPermissionsWithValidId() throws Exception {
        userService.setAccessLevel(testUserWithSession, userService.getAccessLevelByName("Root"));
        mockMvc.perform(get("/Admin/User/SetBannedState/"+testUserWithoutSession.getId())
                .param("token",testSession.getToken())
                .param("ban","true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

}
