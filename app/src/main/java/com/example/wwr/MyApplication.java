package com.example.wwr;

import android.app.Application;

import com.example.wwr.chat.ChatServiceFactory;

import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.messaging.MessageFactory;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    private static MessageFactory messageFactory;
    private static FitnessServiceFactory fitnessServiceFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        messageFactory = new MessageFactory();
        fitnessServiceFactory = new FitnessServiceFactory();
    }

    public static MessageFactory getMessageFactory() {
        return  messageFactory;
    }

    public static FitnessServiceFactory getFitnessServiceFactory() {
        return fitnessServiceFactory;
    }

    public static MessageFactory setMessageFactory(MessageFactory mf) {
        return messageFactory = mf;
    }
}
