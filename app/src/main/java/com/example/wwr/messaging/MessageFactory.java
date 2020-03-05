package com.example.wwr.messaging;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MessageFactory {

    private static final String TAG = MessageFactory.class.getSimpleName();

    public FirebaseFirestoreAdapter f;

    private Map<String, Blueprint> blueprints = new HashMap<>();

    public void put(String key, Blueprint blueprint) {
        blueprints.put(key, blueprint);
    }

    public MessageService create(String key, String chatId) {
        Log.i(TAG, String.format("Creating ChatService with key '%s' and chatId '%s'", key, chatId));
        return blueprints.get(key).create(chatId);
    }

    public interface Blueprint {
        MessageService create(String chatId);
    }
    public MessageService createFirebaseFirestoreChatService(String collectionKey, String chatId, String messagesKey, String fromKey, String textKey, String timeStampKey) {
        f = new FirebaseFirestoreAdapter(collectionKey, chatId, messagesKey, fromKey, textKey, timeStampKey);
        return f;
    }
}
