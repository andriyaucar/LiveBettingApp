package org.bilyoner.controller;


import org.bilyoner.model.Match;
import org.bilyoner.model.MatchOddsHistory;
import org.bilyoner.service.MatchOddsHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(MatchOddsHistoryController.class)
public class MatchOddsHistoryControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private MatchOddsHistoryService matchOddsHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll_MatchOddsHistoryFlux() {
        Match match = new Match();
        match.setId(1L);
        match.setLeague("Premier League");
        match.setHomeTeam("Manchester United");
        match.setAwayTeam("Liverpool");

        MatchOddsHistory odds1 = new MatchOddsHistory();
        odds1.setMatch(match);
        odds1.setHomeWinOdds(2.0);
        odds1.setDrawOdds(3.5);
        odds1.setAwayWinOdds(4.0);

        MatchOddsHistory odds2 = new MatchOddsHistory();
        odds2.setMatch(match);
        odds2.setHomeWinOdds(1.8);
        odds2.setDrawOdds(3.2);
        odds2.setAwayWinOdds(3.9);

        MatchOddsHistory odds3 = new MatchOddsHistory();
        odds3.setMatch(match);
        odds3.setHomeWinOdds(2.8);
        odds3.setDrawOdds(1.2);
        odds3.setAwayWinOdds(4.9);

        List<MatchOddsHistory> oddsList = Arrays.asList(odds1, odds2, odds3);

        when(matchOddsHistoryService.getAllOrderByCreatedAtAsc())
                .thenReturn(oddsList);

        webTestClient.get()
                .uri("/match-odds-history")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(List.class)
                .getResponseBody()
                .as(Flux::collectList)
                .doOnNext(response -> {
                    List<MatchOddsHistory> result = response.get(0);
                    assertEquals(2.0, result.get(0).getHomeWinOdds());
                    assertEquals(3.5, result.get(0).getDrawOdds());
                    assertEquals(4.0, result.get(0).getAwayWinOdds());
                })
                .doOnNext(response -> {
                    List<MatchOddsHistory> result = response.get(1);
                    assertEquals(1.8, result.get(0).getHomeWinOdds());
                    assertEquals(3.2, result.get(0).getDrawOdds());
                    assertEquals(3.9, result.get(0).getAwayWinOdds());
                })
                .doOnNext(response -> {
                    List<MatchOddsHistory> result = response.get(2);
                    assertEquals(2.8, result.get(0).getHomeWinOdds());
                    assertEquals(1.2, result.get(0).getDrawOdds());
                    assertEquals(4.9, result.get(0).getAwayWinOdds());
                })
                .block();
    }
}
