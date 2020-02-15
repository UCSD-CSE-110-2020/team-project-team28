package com.example.wwr;

import android.app.Application;

import com.example.wwr.fitness.FitnessService;

public class GoogleFitSingleton extends Application {
    private static FitnessService fitnessService;

    public void onCreate() {
        super.onCreate();
    }

    public static FitnessService getFitnessService() {
        return fitnessService;
    }

    public static void setFitnessService(FitnessService service) {
        fitnessService = service;
    }
}