package org.bilyoner.service;

import org.bilyoner.dto.MatchDto;
import org.bilyoner.exception.CustomException;
import org.bilyoner.model.Match;
import org.bilyoner.repository.MatchRepository;
import org.bilyoner.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

public class MatchServiceTest {
    @InjectMocks
    private MatchService matchService;

    @Mock
    private ValidationUtil<MatchDto> validationUtil;

    @Mock
    private OddsGenerator oddsGenerator;

    @Mock
    private MatchRepository matchRepository;

    private MatchDto matchDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        matchDto = new MatchDto();
        matchDto.setLeague("Premier League");
        matchDto.setHomeTeam("Manchester United");
        matchDto.setAwayTeam("Liverpool");
        matchDto.setMatchStartTime(LocalDateTime.parse("2024-10-30T06:03:31"));
    }

    @Test
    public void testCreateMatch_Success() {
        when(oddsGenerator.generateOdds()).thenReturn(new double[]{1.5, 3.2, 4.5});

        Match savedMatch = new Match();
        savedMatch.setLeague(matchDto.getLeague());
        savedMatch.setHomeTeam(matchDto.getHomeTeam());
        savedMatch.setAwayTeam(matchDto.getAwayTeam());
        savedMatch.setMatchStartTime(matchDto.getMatchStartTime());
        savedMatch.setHomeWinOdds(1.5);
        savedMatch.setAwayWinOdds(4.5);
        savedMatch.setDrawOdds(3.2);
        when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);
        Match createdMatch = matchService.create(matchDto);

        verify(validationUtil).validateMatchDto(matchDto);
        verify(matchRepository).save(any(Match.class));

        assertEquals(1.5, createdMatch.getHomeWinOdds());
        assertEquals(3.2, createdMatch.getDrawOdds());
        assertEquals(4.5, createdMatch.getAwayWinOdds());

        assertEquals(matchDto.getMatchStartTime(), createdMatch.getMatchStartTime());
    }

    @Test
    public void testCreateMatch_InvalidStartTime() {
        matchDto.setMatchStartTime(LocalDateTime.now().minusDays(1));

        when(oddsGenerator.generateOdds()).thenReturn(new double[]{1.5, 3.2, 4.5});

        CustomException exception = assertThrows(CustomException.class, () -> {
            matchService.create(matchDto);
        });

        assertEquals("Maç başlama tarihi şuandan önce olamaz", exception.getMessage());
        verify(matchRepository, never()).save(any(Match.class));
    }
}
