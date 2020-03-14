package com.example.wwr;

/*
 * A user should have the ability to do all the following things and maintain all the following
 * data points, maintains SRP and follows our design principles, allows functionality of user
 * to be in just one file. This allows the application of strategy pattern.
 */
interface IUser {

    // ensures user can always get their own step count
    long getDailySteps();
    void setDailySteps(long dailySteps);
    void saveCurrentSteps();
    void increaseDailyMockSteps();
    long getLastIntentSteps();

    // ensures user can get distance and walk time of routes
    double getDailyMiles();
    void setLastIntentionalWalkTime(long time);
    long getLastIntentionalTime();
    double getLastIntentMiles();
}
