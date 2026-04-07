package com.studyplanner.controller;

import com.studyplanner.service.StudySessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class StudySessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudySessionService service;

    // GET sans authentification = 401
    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isUnauthorized());
    }

    // GET avec authentification = 200
    @Test
    @WithMockUser(username = "alice")
    void shouldReturn200WhenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk());
    }

    // POST avec données invalides = 400 :
    @Test
    @WithMockUser(username = "alice")
    void shouldReturn400WhenInvalidData() throws Exception {
        String invalidJson = """
            {
                "subject": "",
                "startTime": "2020-01-01T10:00:00",
                "endTime": "2020-01-01T08:00:00"
            }
            """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

}