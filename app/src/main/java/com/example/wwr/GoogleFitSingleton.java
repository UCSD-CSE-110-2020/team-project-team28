package com.example.wwr;

import android.app.Application;
import android.util.Log;

import com.example.wwr.fitness.FitnessService;

public class GoogleFitSingleton extends Application {
    private static FitnessService fitnessService;

    public void onCreate() {
        super.onCreate();
    }

    public static FitnessService getFitnessService() {
        return fitnessService;
    }

    // Allows fitnessService to be used globally.
    public static void setFitnessService(FitnessService service) {
        fitnessService = service;
        Log.d("setSingleton", "Google fit singleton has been set.");
    }
}