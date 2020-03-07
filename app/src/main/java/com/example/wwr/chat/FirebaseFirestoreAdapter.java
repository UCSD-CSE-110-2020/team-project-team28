package com.example.wwr.chat;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FirebaseFirestoreAdapter implements ChatService {
    private static final String TAG = FirebaseFirestoreAdapter.class.getSimpleName();

    private CollectionReference chat;
    private String timeStampKey;
    private String fromKey;
    private String textKey;

    public FirebaseFirestoreAdapter(String collectionKey, String chatId, String messagesKey, String fromKey, String textKey, String timeStampKey) {
        this.fromKey = fromKey;
        this.textKey = textKey;
        this.timeStampKey = timeStampKey;
        this.chat = FirebaseFirestore.getInstance()
                .collection(collectionKey)
                .document(chatId)
                .collection(messagesKey);
    }

    @Override
    public Task<?> addMessage(Map<String, String> message) {
        return chat.add(message);
    }

    @Override
    public void subscribeToMessages(Consumer<List<ChatMessage>> listener) {
        chat.orderBy(timeStampKey, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG + " subscribeToMessages", error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                        List<ChatMessage> newMessages = documentChanges.stream()
                                .map(DocumentChange::getDocument)
                                .map(doc -> new ChatMessage(doc.getString(fromKey), doc.getString(textKey)))
                                .collect(Collectors.toList());

                        listener.accept(newMessages);
                    }
                });
    }
}
