package co.gbyte.weightlog.model;

import android.app.Application;
import android.content.Context;
import android.text.AndroidCharacter;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by walt on 18/10/16.
 *
 */

public class Weight {
    private UUID mId;
    private Date mTime;
    private int mWeight;
    private String mNote;

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

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public String getWeightString () {
        Double weight = mWeight/1000.0;
        return weight.toString();
    }
}
