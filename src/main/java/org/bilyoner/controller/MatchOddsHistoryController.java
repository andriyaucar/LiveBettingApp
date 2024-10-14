package org.bilyoner.controller;

import org.bilyoner.model.MatchOddsHistory;
import org.bilyoner.service.MatchOddsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/match-odds-history")
public class MatchOddsHistoryController {

    @Autowired
    private MatchOddsHistoryService matchOddsHistoryService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<MatchOddsHistory>> getAll() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> matchOddsHistoryService.getAllOrderByCreatedAtAsc());
    }
}
