package com.example.wwr;

import android.app.Application;

import com.example.wwr.messaging.MessageFactory;

public class MyApplication extends Application {
    private static MessageFactory messageFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        messageFactory = new MessageFactory();
    }

    public static MessageFactory getMessageFactory() {
        return  messageFactory;
    }

    public static MessageFactory setMessageFactory(MessageFactory mf) {
        return messageFactory = mf;
    }
}
