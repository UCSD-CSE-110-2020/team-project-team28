package com.example.wwr;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/*
 * Whole purpose of this class is to put all stats and methods of a user in one place
 */
public class UserInfo {
    Context context;
    private final double STRIDE_FACTOR = 0.413;
    private final double FEET_IN_MILE = 5280;
    private final double INCHES_IN_FEET = 12;
    private final int MOCK_STEP_COUNT = 500;

    static String DATA = "prefs";
    static SharedPreferences pf;
    static SharedPreferences.Editor editor;

    public UserInfo(Context context) {
        this.context = context;
    }

    public long getDailySteps() {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return pf.getLong("dailyTotalSteps", 0);
    }

    public void setDailySteps(long dailySteps) {
        long mockStepsTotal = pf.getLong("dailyTotalMockSteps", 0) * MOCK_STEP_COUNT;
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        editor = pf.edit();
        editor.putLong("dailyTotalSteps", dailySteps + mockStepsTotal);
        editor.apply();
    }

    public void increaseDailyMockSteps() {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        long mockSteps = pf.getLong("dailyTotalMockSteps", 0);
        editor = pf.edit();
        editor.putLong("dailyTotalMockSteps", ++mockSteps);
        editor.apply();
    }

    public double getDailyMiles() {
        return (this.getDailySteps() * this.getHeight() * STRIDE_FACTOR) / (INCHES_IN_FEET * FEET_IN_MILE);
    }

    // in milliseconds
    public void setLastIntentionalWalkTime(long time) {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        editor = pf.edit();
        editor.putLong("intentionalTime", time);
        editor.apply();
    }

    public long getLastIntentionalTime() {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return pf.getLong("intentionalTime", 0);
    }

    public void saveCurrentSteps() {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        editor = pf.edit();
        editor.putLong("currentSteps", this.getDailySteps());
        editor.apply();
    }

    private long getPreviouslySavedSteps() {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return pf.getLong("currentSteps", 0);
    }

    public long getLastIntentSteps() {
        long previousSteps = this.getPreviouslySavedSteps();
        long currentSteps = this.getDailySteps();
        return currentSteps - previousSteps;
    }

    public double getLastIntentMiles() {
        return (this.getLastIntentSteps() * this.getHeight() * STRIDE_FACTOR) /
        (INCHES_IN_FEET * FEET_IN_MILE);
    }

    private int getHeight() {
        pf = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return pf.getInt("totalInches", 0);

    }

    static void createDataBase(Context cxt) {
        pf = cxt.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        editor = pf.edit();
        editor.putLong("dailyTotalSteps", 0);
        editor.putLong("dailyTotalMockSteps", 0);
        editor.putLong("dailyTotalMiles", 0);
        editor.putLong("intentionalTime", 0);
        editor.putLong("currentSteps", 0);
        editor.apply();
    }
}
