package com.example.wwr;
import android.util.Log;

// Follows the strategy pattern by acting as a strategy for the DistanceCalculator.
public class WalkingDistanceMiles implements DistanceCalculator {
    private static final double STRIDE_FACTOR = 0.413;
    private static final int FEET_IN_MILE = 5280;
    private static final int INCHES_IN_FEET = 12;

    // Gets the distance that the user has walked.
    public double getDistance(long steps) {
        int inches = MainActivity.inches;
        Log.d("total_inches", "Total inches is " + inches);
        return (double) steps *
                (
                        (( double)inches *this.STRIDE_FACTOR) / //stride length inches
                                ((double)INCHES_IN_FEET * (double)FEET_IN_MILE) //inches in mile
                );
    }
}
