package org.bilyoner.service;

import org.bilyoner.model.Match;
import org.bilyoner.model.MatchOddsHistory;
import org.bilyoner.repository.MatchOddsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchOddsHistoryService {

    @Autowired
    private MatchOddsHistoryRepository matchOddsHistoryRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private OddsGenerator oddsGenerator;

    @Scheduled(fixedRate = 1000)
    public void updateMatchOdds() {
        List<Match> matches = matchService.getAll();

        matches.forEach(match -> {
            double[] newOdds = oddsGenerator.generateOdds();
            match.setHomeWinOdds(newOdds[0]);
            match.setDrawOdds(newOdds[1]);
            match.setAwayWinOdds(newOdds[2]);

            matchService.update(match);

            MatchOddsHistory history = new MatchOddsHistory(
                    match,
                    newOdds[0],
                    newOdds[1],
                    newOdds[2],
                    LocalDateTime.now()
            );

            matchOddsHistoryRepository.save(history);
        });
    }

   public List<MatchOddsHistory> getAllOrderByCreatedAtAsc() {
        return matchOddsHistoryRepository.findAllByOrderByCreatedAtAsc();
   }
}