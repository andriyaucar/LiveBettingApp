package org.bilyoner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MatchDto {
    private Long id;

    @NotNull(message = "Lig bilgisi boş olamaz")
    private String league;

    @NotNull(message = "Ev sahibi takım bilgisi boş olamaz")
    private String homeTeam;

    @NotNull(message = "Deplasman takımı bilgisi boş olamaz")
    private String awayTeam;

    @NotNull(message = "Maç başlama tarihi bilgisi boş olamaz")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime matchStartTime;

    public MatchDto() {
    }

    public MatchDto(Long id, String league, String homeTeam, String awayTeam, LocalDateTime matchStartTime) {
        this.id = id;
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStartTime = matchStartTime;
    }

    public MatchDto(String league, String homeTeam, String awayTeam, LocalDateTime matchStartTime) {
        this.league = league;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStartTime = matchStartTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public LocalDateTime getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(LocalDateTime matchStartTime) {
        this.matchStartTime = matchStartTime;
    }
}
