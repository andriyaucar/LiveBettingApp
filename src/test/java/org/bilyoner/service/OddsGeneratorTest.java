package org.bilyoner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class OddsGeneratorTest {
    private OddsGenerator oddsGenerator;

    @BeforeEach
    public void setUp() {
        oddsGenerator = new OddsGenerator();
    }

    @Test
    public void testGenerateOdds_WithinRange() {
        double[] odds = oddsGenerator.generateOdds();

        assertTrue(odds[0] >= 1.05 && odds[0] <= 10.0, "Home Win Odds is out of range: " + odds[0]);
        assertTrue(odds[1] >= 1.05 && odds[1] <= 10.0, "Draw Odds is out of range: " + odds[1]);
        assertTrue(odds[2] >= 1.05 && odds[2] <= 10.0, "Away Win Odds is out of range: " + odds[2]);
    }

    @Test
    public void testGenerateOdds_TotalProbability() {
        double[] odds = oddsGenerator.generateOdds();

        double homeWinProbability = 1 / odds[0];
        double drawProbability = 1 / odds[1];
        double awayWinProbability = 1 / odds[2];

        double totalProbability = homeWinProbability + drawProbability + awayWinProbability;
        assertTrue(totalProbability > 1.05 && totalProbability <= 1.20, "Total probability is incorrect: " + totalProbability);
    }
}
