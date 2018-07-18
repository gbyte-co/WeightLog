package co.gbyte.weightlog.model;

import java.util.Date;
import java.util.UUID;

/**
 * Provides abstraction of body weight and calculates Body Mass Index.
 */
public class Weight {
    private UUID mId;
    private Date mTime;
    // body weight is stored in grams
    private int mWeight;
    private String mNote;
    // body height is stored in millimeters
    private static int sHeight = 0;

    private static final int sHeightMin =  540;
    private static final int sHeightMax = 2750;

    private static final int sWeightMin =   2000;
    private static final int sWeightMax = 640000;

    public Weight() {
        this(UUID.randomUUID());
    }

    public Weight(UUID id) {
        mId = id;
        mTime = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) throws IllegalArgumentException {

        if (weight < sWeightMin || weight > sWeightMax) {
            throw new IllegalArgumentException("weight must be between " + sWeightMin
                    + " and " + sWeightMax + " grams");
        }

        mWeight = weight;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public static void setHeight(int height) throws IllegalArgumentException {

        if (height < sHeightMin || height > sHeightMax) {
            throw new IllegalArgumentException("height must be between " + sHeightMin
                    + " and " + sHeightMax + " millimeters");
        }

        sHeight = height;
    }

    public static void resetHeight() {

        sHeight = 0;
    }

    /**
     * Computes Body Mass Index if height and weight are set.
     * Otherwise returns 0.
     */
    public double bmi() {
        if (sHeight < sHeightMin || sHeight > sHeightMax) {
            return 0.0;
        }

        if (this.mWeight == 0) {
            return 0.0;
        }

        return (double) mWeight * 1000 / (sHeight * sHeight);
    }

    // ToDo: find better solution: This shouldn't be in in this class(?)
    public String getWeightStringKg() {
        Double weight = mWeight/1000.0;
        return weight.toString();
    }
}
