package com.example.wwr.chat;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface ChatService {
    Task<?> addMessage(Map<String, String> message);

    void subscribeToMessages(Consumer<List<ChatMessage>> listener);
}