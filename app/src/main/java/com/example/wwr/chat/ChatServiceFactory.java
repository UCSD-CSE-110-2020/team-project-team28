package com.example.wwr.chat;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ChatServiceFactory {
    private static final String TAG = ChatServiceFactory.class.getSimpleName();

    public FirebaseFirestoreAdapter f;

    private Map<String, Blueprint> blueprints = new HashMap<>();

    public void put(String key, Blueprint blueprint) {
        blueprints.put(key, blueprint);
    }

    public ChatService create(String key, String chatId) {
        Log.i(TAG, String.format("Creating ChatService with key '%s' and chatId '%s'", key, chatId));
        return blueprints.get(key).create(chatId);
    }

    public interface Blueprint {
        ChatService create(String chatId);
    }
    public ChatService createFirebaseFirestoreChatService(String collectionKey, String chatId, String messagesKey, String fromKey, String textKey, String timeStampKey) {
        f = new FirebaseFirestoreAdapter(collectionKey, chatId, messagesKey, fromKey, textKey, timeStampKey);
        return f;
    }
}