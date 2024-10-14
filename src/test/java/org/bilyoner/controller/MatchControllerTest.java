package org.bilyoner.controller;

import org.bilyoner.dto.MatchDto;
import org.bilyoner.model.Match;
import org.bilyoner.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "matchService")
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(matchController).build();
    }

    @Test
    public void testCreateMatch_Success() throws Exception {
        MatchDto matchDto = new MatchDto("Premier League", "Manchester United", "Liverpool", LocalDateTime.parse("2024-10-30T15:30:00"));

        Match mockMatch = new Match();
        mockMatch.setId(1L);
        mockMatch.setLeague(matchDto.getLeague());
        mockMatch.setHomeTeam(matchDto.getHomeTeam());
        mockMatch.setAwayTeam(matchDto.getAwayTeam());
        mockMatch.setMatchStartTime(matchDto.getMatchStartTime());

        when(matchService.create(any(MatchDto.class))).thenReturn(mockMatch);

        mockMvc.perform(post("/match/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"league\": \"Premier League\", \"homeTeam\": \"Manchester United\", \"awayTeam\": \"Liverpool\", \"matchStartTime\": \"2024-10-30T15:30:00\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.homeTeam").value("Manchester United"))
                .andExpect(jsonPath("$.awayTeam").value("Liverpool"))
                .andExpect(jsonPath("$.league").value("Premier League"))
                .andExpect(jsonPath("$.matchStartTime[0]").value(2024))
                .andExpect(jsonPath("$.matchStartTime[1]").value(10))
                .andExpect(jsonPath("$.matchStartTime[2]").value(30))
                .andExpect(jsonPath("$.matchStartTime[3]").value(15))
                .andExpect(jsonPath("$.matchStartTime[4]").value(30));
    }

    @Test
    public void testGetAllMatches_Success() throws Exception {
        Match match1 = new Match();
        match1.setId(1L);

        Match match2 = new Match();
        match2.setId(2L);
        List<Match> matches = Arrays.asList(match1, match2);
        when(matchService.getAll()).thenReturn(matches);

        mockMvc.perform(get("/match/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
