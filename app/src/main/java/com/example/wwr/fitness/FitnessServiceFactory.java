package com.example.wwr.fitness;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.example.wwr.MainActivity;
//import com.example.wwr.StepCountActivity;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, MainActivity mainactivity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(mainactivity);
    }

    public interface BluePrint {
        FitnessService create(MainActivity mainActivity);
    }
}

