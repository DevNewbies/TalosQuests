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
 * Created by Nikolas on 15/1/2017.
 */
@Transactional
public class SessionManagementControllerTests extends AbstractControllerTest {
    @Test
    public void testListSessionsWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Admin/Session")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListSessionsWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(get("/Admin/Session")
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListSessionsWithValidTokenWithPermissions() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Session")
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.response", hasSize(1)))
                .andReturn();
    }

    @Test
    public void testListSessionsWithValidTokenWithPermissionsAndValidIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Session/"+testSession.getSessionId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListSessionsWithValidTokenWithPermissionsAndInvalidIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Session/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteSessionWithInvalidToken() throws Exception {

        mockMvc.perform(delete("/Admin/Session")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeSessionsWithValidTokenAndPasswordButNoPermissions() throws Exception {
        accessService.setUserAccessLevel(testUserWithSession,accessService.getByName("Admin"));
        testUserWithSession.getAccess().setCanWipeSessions(false);

        mockMvc.perform(delete("/Admin/Session")
                .param("token",testSession.getToken())
                .param("password",passWordPassing))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testDeleteSessionWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(delete("/Admin/Session/" + testSession.getSessionId())
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeSessionsWithValidTokenWithPermissionsWithoutPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Session")
                .param("token",testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeSessionsWithValidTokenWithPermissionsWithIncorrectPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Session")
                .param("token",testSession.getToken())
                .param("password","invalid"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeSessionsWithValidTokenWithPermissionsWithCorrectPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Session")
                .param("token",testSession.getToken())
                .param("password",passWordPassing))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteSessionWithValidTokenWithPermissionsWithInvalidUserId() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Session/"+Long.MAX_VALUE)
                .param("token",testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteSessionWithValidTokenWithPermissionsWithValidSessionId() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Session/"+testSession.getSessionId())
                .param("token",testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


}
