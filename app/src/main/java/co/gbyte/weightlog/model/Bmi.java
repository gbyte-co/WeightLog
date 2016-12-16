package co.gbyte.weightlog.model;

/**
 * Created by walt on 15/12/16.
 *
 */

public final class Bmi {

    /* Constants: */
    // Height (millimeters)
    private static final int MIN_HEIGHT = 700;
    private static final int MAX_HEIGHT = 2500;

    // Weight (grams)
    private static final int MIN_WEIGHT = 2000;
    private static final int MAX_WEIGHT = 640000;

    // BMI
    private static final double VSU  = 15.0;
    private static final double SU   = 16.0;
    private static final double U    = 18.5;
    private static final double OW   = 25.0;
    private static final double MO   = 30.0;
    private static final double SO   = 35.0;
    private static final double VSO  = 40.0;

    private Bmi() {}

    public static double calc(int height, int weight) throws IllegalArgumentException {

        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new IllegalArgumentException("height must be between " + MIN_HEIGHT
                                               + " and " + MAX_HEIGHT);
        }

        if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new IllegalArgumentException("weight must be between " + MIN_WEIGHT
                    + " and " + MAX_WEIGHT);
        }

        return (double) weight * 1000 / (height * height);
    }
}
