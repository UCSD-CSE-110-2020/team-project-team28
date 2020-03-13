package com.example.wwr;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import edu.ucsd.cse110.firebaselab.chat.ChatService;
import edu.ucsd.cse110.firebaselab.chat.ChatServiceFactory;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class TestChatSubscriptionWithMockito {

    @Test
    public void testSubscribeToChat1() {
        ChatService chatService = Mockito.mock(ChatService.class);
        ChatServiceFactory chatServiceFactory = Mockito.mock(ChatServiceFactory.class);

        Mockito.when(chatServiceFactory.createFirebaseFirestoreChatService(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(chatService);

        MyApplication.setChatServiceFactory(chatServiceFactory);

        ActivityScenario.launch(MainActivity.class);

        Mockito.verify(chatServiceFactory).createFirebaseFirestoreChatService(anyString(), eq("chat1"), anyString(), anyString(), anyString(), anyString());
    }
}
