package org.bilyoner.controller;

import org.bilyoner.dto.MatchDto;
import org.bilyoner.model.Match;
import org.bilyoner.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping(value = "/create")
    public ResponseEntity<Match> create(@RequestBody MatchDto matchDto) {
        Match createdMatch = matchService.create(matchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMatch);
    }

    @GetMapping(value = "get-all")
    public List<Match> getAll() {
        return matchService.getAll();
    }
}
