package org.bilyoner.service;

import org.bilyoner.dto.MatchDto;
import org.bilyoner.exception.CustomException;
import org.bilyoner.model.Match;
import org.bilyoner.repository.MatchRepository;
import org.bilyoner.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private OddsGenerator oddsGenerator;

    @Autowired
    private ValidationUtil<MatchDto> validationService;

    public Match create(MatchDto matchDto) {
        validationService.validateMatchDto(matchDto);

        Match match = new Match();
        match.setLeague(matchDto.getLeague());
        match.setHomeTeam(matchDto.getHomeTeam());
        match.setAwayTeam(matchDto.getAwayTeam());

        double[] odds = oddsGenerator.generateOdds();
        match.setHomeWinOdds(odds[0]);
        match.setDrawOdds(odds[1]);
        match.setAwayWinOdds(odds[2]);

        if (LocalDateTime.now().toInstant(ZoneOffset.UTC).isAfter(matchDto.getMatchStartTime().toInstant(ZoneOffset.UTC))) {
            throw new CustomException("Maç başlama tarihi şuandan önce olamaz");
        }

        match.setMatchStartTime(matchDto.getMatchStartTime());


        return matchRepository.save(match);
    }

    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    public void update(Match match) {
        matchRepository.save(match);
    }

    public Optional<Match> getOneById(Long id) {
        return matchRepository.findById(id);
    }
}
