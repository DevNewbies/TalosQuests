package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractControllerTest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nikolas on 11/1/2017.
 */
@Transactional
public class QuestControllerTests extends AbstractControllerTest {
    @Test
    public void testActiveGetQuestWithValidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Active")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetCompleteQuestsWithValidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Complete")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetCompleteQuestsWithValidTokenAndActiveGameSet() throws Exception {
        testUserWithSession.setActiveGame(testGameForUserWithSession);
        mockMvc.perform(get("/Game/Quest/Complete")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetCompleteQuestsWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Complete")
                .param("token", "invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetIncompleteQuestsWithValidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Incomplete")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetIncompleteQuestsWithValidTokenAndActiveGameSet() throws Exception {
        testUserWithSession.setActiveGame(testGameForUserWithSession);
        mockMvc.perform(get("/Game/Quest/Incomplete")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testGetIncompleteQuestsWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Incomplete")
                .param("token", "invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveGetQuestWithValidTokenAndActiveGameSet() throws Exception {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        mockMvc.perform(get("/Game/Quest/Active")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testActiveGetQuestWithValidTokenAndActiveGameSetAndActiveQuestSet() throws Exception {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);
        mockMvc.perform(get("/Game/Quest/Active")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testActiveGetQuestWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Active")
                .param("token", "Invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveGetNextQuestWithValidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Next")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testActiveGetNextQuestWithValidTokenAndActiveGameSetButNoMoreQuestsAvailable() throws Exception {

        testGameForUserWithSession.getIncompleteUserQuests().clear();
        gameRepository.save(testGameForUserWithSession);
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);

        mockMvc.perform(get("/Game/Quest/Next")
                .param("token", testSession.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testActiveGetNextQuestWithValidTokenAndActiveGameSet() throws Exception {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        mockMvc.perform(get("/Game/Quest/Next")
                .param("token", testSession.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveGetNextQuestWithInvalidToken() throws Exception {

        mockMvc.perform(get("/Game/Quest/Next")
                .param("token", "Invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveSubmitAnswerWithInvalidToken() throws Exception {

        mockMvc.perform(post("/Game/Quest/SubmitAnswer")
                .param("token", "Invalid"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testActiveSubmitAnswerWithInvalidTokenAndAnswerProvided() throws Exception {

        mockMvc.perform(post("/Game/Quest/SubmitAnswer")
                .param("token", "Invalid")
                .content(mapToJson(new QuestChoice("test")))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveSubmitAnswerWithValidToken() throws Exception {

        mockMvc.perform(post("/Game/Quest/SubmitAnswer")
                .param("token", testSession.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testActiveSubmitAnswerWithValidTokenAndAnswerProvided() throws Exception {

        mockMvc.perform(post("/Game/Quest/SubmitAnswer")
                .param("token", testSession.getToken())
                .content(mapToJson(new QuestChoice()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    @Test
    public void testActiveSubmitAnswerWithValidTokenAndAnswerProvidedAndActiveGameSet() throws Exception {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        mockMvc.perform(post("/Game/Quest/SubmitAnswer")
                .param("token", testSession.getToken())
                .content(mapToJson(new QuestChoice()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void testActiveSubmitAnswerWithValidTokenAndAnswerProvidedAndActiveGameSetAndActiveQuestSet() throws Exception {
        gameService.setActiveGame(testUserWithSession, testGameForUserWithSession);
        gameService.getNextQuest(testGameForUserWithSession);
        mockMvc.perform(post("/Game/Quest/SubmitAnswer")
                .param("token", testSession.getToken())
                .content(mapToJson(new QuestChoice()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }
}
