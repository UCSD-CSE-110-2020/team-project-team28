package com.example.wwr;

import android.app.Application;

import com.example.wwr.chat.ChatServiceFactory;

import com.example.wwr.fitness.FitnessServiceFactory;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    private static ChatServiceFactory chatServiceFactory;
    private static FitnessServiceFactory fitnessServiceFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        chatServiceFactory = new ChatServiceFactory();
        fitnessServiceFactory = new FitnessServiceFactory();
    }

    public static ChatServiceFactory getChatServiceFactory() {
        return chatServiceFactory;
    }

    public static FitnessServiceFactory getFitnessServiceFactory() {
        return fitnessServiceFactory;
    }

    public static ChatServiceFactory setChatServiceFactory(ChatServiceFactory ch) {
        chatServiceFactory = ch;
        return chatServiceFactory;
    }
}
