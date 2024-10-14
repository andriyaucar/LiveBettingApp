package org.bilyoner.service;

import org.bilyoner.model.Match;
import org.bilyoner.model.MatchOddsHistory;
import org.bilyoner.repository.MatchOddsHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MatchOddsHistoryServiceTest {
    @InjectMocks
    private MatchOddsHistoryService matchOddsHistoryService;

    @Mock
    private MatchService matchService;

    @Mock
    private MatchOddsHistoryRepository matchOddsHistoryRepository;

    @Mock
    private OddsGenerator oddsGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateMatchOdds() {
        Match match1 = new Match();
        match1.setHomeWinOdds(2.0);
        match1.setDrawOdds(3.5);
        match1.setAwayWinOdds(4.0);

        Match match2 = new Match();
        match2.setHomeWinOdds(1.8);
        match2.setDrawOdds(3.2);
        match2.setAwayWinOdds(3.9);

        List<Match> matches = Arrays.asList(match1, match2);
        when(matchService.getAll()).thenReturn(matches);
        when(oddsGenerator.generateOdds()).thenReturn(new double[]{1.9, 3.6, 4.1}, new double[]{2.1, 3.3, 3.8});
        doNothing().when(matchService).update(any(Match.class));
        when(matchOddsHistoryRepository.save(any(MatchOddsHistory.class))).thenReturn(null);
        matchOddsHistoryService.updateMatchOdds();

        verify(matchService, times(1)).getAll();
        verify(oddsGenerator, times(2)).generateOdds();
        verify(matchService, times(2)).update(any(Match.class));
        verify(matchOddsHistoryRepository, times(2)).save(any(MatchOddsHistory.class));
    }

    @Test
    public void testGetAllOrderByCreatedAtAsc() {
        MatchOddsHistory history1 = new MatchOddsHistory(new Match(), 2.0, 3.5, 4.0, LocalDateTime.now().minusMinutes(5));
        MatchOddsHistory history2 = new MatchOddsHistory(new Match(), 1.8, 3.2, 3.9, LocalDateTime.now().minusMinutes(1));

        when(matchOddsHistoryRepository.findAllByOrderByCreatedAtAsc()).thenReturn(Arrays.asList(history1, history2));

        List<MatchOddsHistory> result = matchOddsHistoryService.getAllOrderByCreatedAtAsc();

        assertEquals(2, result.size());
        assertEquals(2.0, result.get(0).getHomeWinOdds());
        assertEquals(1.8, result.get(1).getHomeWinOdds());
        verify(matchOddsHistoryRepository, times(1)).findAllByOrderByCreatedAtAsc();
    }
}
