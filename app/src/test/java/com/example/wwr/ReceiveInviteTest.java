package com.example.wwr;

        import android.content.Intent;
        import android.widget.Button;

        import androidx.test.core.app.ActivityScenario;
        import androidx.test.core.app.ApplicationProvider;
        import androidx.test.ext.junit.runners.AndroidJUnit4;

        import com.example.wwr.LogInActivity;
        import com.example.wwr.MainActivity;
        import com.example.wwr.R;
        import com.example.wwr.messaging.MessageFactory;
        import com.example.wwr.messaging.MessageService;
        import com.google.firebase.firestore.CollectionReference;

        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.mockito.Mockito;
        import org.robolectric.shadows.ShadowLog;

        import static com.google.common.truth.Truth.assertThat;
        import static org.mockito.ArgumentMatchers.anyString;
        import static org.mockito.ArgumentMatchers.eq;

        import static com.google.common.base.CharMatcher.any;

@RunWith(AndroidJUnit4.class)
public class ReceiveInviteTest {

    private Intent intent;

    @Test
    public void testReceivingWalkProposal() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);


        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button button = activity.findViewById(R.id.startWWRButton);
            button.performClick();
        });
        Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        //CollectionReference chat = Mockito.mock(CollectionReference.class, Mockito.RETURNS_MOCK);

        //Mockito.when(chatSe)



    }

    @Test
    public void testChat() {
        MessageService messageService = Mockito.mock(MessageService.class);
        MessageFactory messageFactory = Mockito.mock(MessageFactory.class);

        Mockito.when(messageFactory.createFirebaseFirestoreChatService(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(messageService);



       // ActivityScenario.launch(MainActivity.class);

       // Mockito.verify(messageService).subscribeToMessages(any());

    }

//    @Test
//    public void testSubscribeToChat1() {
//        ShadowLog.stream = System.out;
//        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
//        intent.putExtra(MainActivity.CHAT_MESSAGE_SERVICE_EXTRA, "TEST_CHAT_SERVICE");
//
//        MessageFactory chatServiceFactory = MyApplication.getChatServiceFactory();
//        chatServiceFactory.put("TEST_CHAT_SERVICE", chatId -> {
//            assertThat(chatId).isEqualTo("chat1");
//            return new TestChatService();
//        });
//
//        ActivityScenario.launch(intent);
//    }

}
