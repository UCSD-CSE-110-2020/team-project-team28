package com.example.wwr.messaging;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface MessageService {
    Task<?> addMessage(Map<String, String> message);

    void subscribeToMessages(Consumer<List<Message>> listener);
}
