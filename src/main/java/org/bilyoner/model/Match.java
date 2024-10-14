package org.bilyoner.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String league;

    @Column(nullable = false)
    private String homeTeam;

    @Column(nullable = false)
    private String awayTeam;

    @Column(nullable = false)
    private Double homeWinOdds;

    @Column(nullable = false)
    private Double drawOdds;

    @Column(nullable = false)
    private Double awayWinOdds;

    @Column(nullable = false)
    private LocalDateTime matchStartTime;

    public Match() {
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

    public Double getHomeWinOdds() {
        return homeWinOdds;
    }

    public void setHomeWinOdds(Double homeWinOdds) {
        this.homeWinOdds = homeWinOdds;
    }

    public Double getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(Double drawOdds) {
        this.drawOdds = drawOdds;
    }

    public Double getAwayWinOdds() {
        return awayWinOdds;
    }

    public void setAwayWinOdds(Double awayWinOdds) {
        this.awayWinOdds = awayWinOdds;
    }

    public LocalDateTime getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(LocalDateTime matchStartTime) {
        this.matchStartTime = matchStartTime;
    }
}
