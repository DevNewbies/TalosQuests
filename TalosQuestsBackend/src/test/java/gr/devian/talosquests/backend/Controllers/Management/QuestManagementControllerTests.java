package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.Models.Quest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 12/1/2017.
 */
@Transactional
public class QuestManagementControllerTests extends AbstractControllerTest {

    @Test
    public void testListQuestsWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Admin/Quest")
                .param("token", "invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListQuestsWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(get("/Admin/Quest")
                .param("token", testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testListQuestsWithValidTokenWithPermissions() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Quest")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListQuestsWithValidTokenWithPermissionsAndInvalidIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Quest/" + Long.MAX_VALUE)
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testListQuestsWithValidTokenWithPermissionsAndValidIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(get("/Admin/Quest/" + testQuestSerres1.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testWipeQuestsWithInvalidToken() throws Exception {

        mockMvc.perform(delete("/Admin/Quest")
                .param("token", "invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeQuestsWithValidTokenButNoPermissions() throws Exception {

        mockMvc.perform(delete("/Admin/Quest")
                .param("token", testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testWipeQuestsWithValidTokenWithPermissionsWithoutPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Quest")
                .param("token", testSession.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeQuestsWithValidTokenWithPermissionsWithInvalidPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Quest")
                .param("token", testSession.getToken())
                .param("password", "invalid"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeQuestsWithValidTokenWithPermissionsWithValidPassword() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Quest")
                .param("token", testSession.getToken())
                .param("password", passWordPassing))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testWipeQuestsWithValidTokenWithPermissionsWithValidPasswordButNoWipePermission() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Admin"));
        mockMvc.perform(delete("/Admin/Quest")
                .param("token", testSession.getToken())
                .param("password", passWordPassing))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteQuestWithValidTokenWithPermissionsAndInvalidIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Quest/" + Long.MAX_VALUE)
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testDeleteQuestsWithValidTokenWithPermissionsAndValidIdSpecified() throws Exception {
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(delete("/Admin/Quest/" + testQuestSerres1.getId())
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testUpdateQuestsWithInvalidToken() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());

        mockMvc.perform(put("/Admin/Quest/" + Long.MAX_VALUE)
                .param("token", "invalid")
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testUpdateQuestsWithValidTokenButNoPermissions() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());

        mockMvc.perform(put("/Admin/Quest/" + Long.MAX_VALUE)
                .param("token", testSession.getToken())
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testUpdateQuestsWithValidTokenWithPermissionsWithInvalidId() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(put("/Admin/Quest/" + Long.MAX_VALUE)
                .param("token", testSession.getToken())
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testUpdateQuestsWithValidTokenWithPermissionsWithValidId() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(put("/Admin/Quest/" + testQuestSerres2.getId())
                .param("token", testSession.getToken())
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }


    @Test
    public void testCreateQuestsWithInvalidToken() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());

        mockMvc.perform(post("/Admin/Quest")
                .param("token", "invalid")
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testCreateQuestsWithValidTokenButNoPermissions() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());

        mockMvc.perform(post("/Admin/Quest")
                .param("token", testSession.getToken())
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testCreateQuestsWithValidTokenWithPermissionsWithInvalidId() throws Exception {
        Quest quest = new Quest();
        quest.setContent("temp");
        quest.setAvailableChoices(testQuestSerres2.getAvailableChoices());
        quest.setCorrectChoice(testQuestSerres2.getCorrectChoice());
        quest.setLocation(testLocationAthens1);
        quest.setName("temp");
        quest.setExp(testQuestSerres2.getExp());
        testUserWithSession.setAccess(accessService.getByName("Root"));
        mockMvc.perform(post("/Admin/Quest")
                .param("token", testSession.getToken())
                .content(mapToJson(quest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }



}
