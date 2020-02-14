package com.example.wwr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;

public class GoogleFitService extends Service {
    private final IBinder iBinder = new LocalService();

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "MainActivity";
    public FitnessService fitnessService;
    private long startSteps;

    public GoogleFitService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    class LocalService extends Binder {
        public GoogleFitService getService() {
            return GoogleFitService.this;
        }
    }
}
