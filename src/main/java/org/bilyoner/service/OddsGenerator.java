package org.bilyoner.service;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

@Service
public class OddsGenerator {
    private static final double MIN_ODDS = 1.05;
    private static final double MAX_ODDS = 10.0;
    private static final double MIN_PROBABILITY = 1.05;
    private static final double MAX_PROBABILITY = 1.20;

    private final Random random = new Random();

    public double[] generateOdds() {
        double[] odds = new double[3];
        double totalProbability;

        do {
            odds[0] = MIN_ODDS + (MAX_ODDS - MIN_ODDS) * random.nextDouble();
            odds[1] = MIN_ODDS + (MAX_ODDS - MIN_ODDS) * random.nextDouble();
            odds[2] = MIN_ODDS + (MAX_ODDS - MIN_ODDS) * random.nextDouble();

            double homeWinProbability = 1 / odds[0];
            double drawProbability = 1 / odds[1];
            double awayWinProbability = 1 / odds[2];

            totalProbability = homeWinProbability + drawProbability + awayWinProbability;

        } while (totalProbability < MIN_PROBABILITY || totalProbability > MAX_PROBABILITY);

        return new double[] { formatOdds(odds[0]), formatOdds(odds[1]), formatOdds(odds[2]) };
    }

    private double formatOdds(double odds) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
        return Double.parseDouble(decimalFormat.format(odds));
    }
}
