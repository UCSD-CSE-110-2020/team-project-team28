package com.example.wwr;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wwr.chat.ChatService;
import com.example.wwr.chat.ChatServiceFactory;
import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class TestChatSubscriptionWithMockito {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private Intent intent;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestChatSubscriptionWithMockito.TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(),  MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testSubscribeToChat1() {
        ChatService chatService = Mockito.mock(ChatService.class);
        ChatServiceFactory chatServiceFactory = Mockito.mock(ChatServiceFactory.class);

        Mockito.when(chatServiceFactory.createFirebaseFirestoreChatService(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(chatService);

        MyApplication.setChatServiceFactory(chatServiceFactory);

        ActivityScenario.launch(intent);

        Mockito.verify(chatServiceFactory).createFirebaseFirestoreChatService(anyString(), eq("chat1"), anyString(), anyString(), anyString(), anyString());
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity stepCountActivity;

        public TestFitnessService(MainActivity stepCountActivity) {
            this.stepCountActivity = stepCountActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            //stepCountActivity.setStepCount(1000);
        }
    }
}