package org.bilyoner.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MatchOddsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    private double homeWinOdds;
    private double drawOdds;
    private double awayWinOdds;

    private LocalDateTime createdAt;

    public MatchOddsHistory() {
    }

    public MatchOddsHistory(Match match, double homeWinOdds, double drawOdds, double awayWinOdds, LocalDateTime createdAt) {
        this.match = match;
        this.homeWinOdds = homeWinOdds;
        this.drawOdds = drawOdds;
        this.awayWinOdds = awayWinOdds;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public double getHomeWinOdds() {
        return homeWinOdds;
    }

    public void setHomeWinOdds(double homeWinOdds) {
        this.homeWinOdds = homeWinOdds;
    }

    public double getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(double drawOdds) {
        this.drawOdds = drawOdds;
    }

    public double getAwayWinOdds() {
        return awayWinOdds;
    }

    public void setAwayWinOdds(double awayWinOdds) {
        this.awayWinOdds = awayWinOdds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
