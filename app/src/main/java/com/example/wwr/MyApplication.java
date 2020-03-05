package com.example.wwr;

import android.app.Application;

import com.example.wwr.chat.ChatServiceFactory;

public class MyApplication extends Application {
    private static ChatServiceFactory chatServiceFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        chatServiceFactory = new ChatServiceFactory();
    }

    public static ChatServiceFactory getChatServiceFactory() {
        return chatServiceFactory;
    }

    public static ChatServiceFactory setChatServiceFactory(ChatServiceFactory csf) {
        return chatServiceFactory = csf;
    }
}
