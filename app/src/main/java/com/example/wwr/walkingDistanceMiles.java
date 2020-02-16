package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.wwr.DistanceCalculator;

import static android.content.Context.MODE_PRIVATE;

public class walkingDistanceMiles implements DistanceCalculator {

    private static final double STRIDE_FACTOR = 0.413;
    private static final int FEET_IN_MILE = 5280;
    private static final int INCHES_IN_FEET = 12;

    public double getDistance(long steps) {
        //SharedPreferences
        SharedPreferences sharedPreferences = MainActivity.getMainActivityContext().getSharedPreferences("total_inches", MODE_PRIVATE);

        int inches = sharedPreferences.getInt("total_inch", 0);

        return (double) steps *
               (
                (( double)inches *this.STRIDE_FACTOR) / //stride length inches
                ((double)INCHES_IN_FEET * (double)FEET_IN_MILE) //inches in mile
               );
    }
}
