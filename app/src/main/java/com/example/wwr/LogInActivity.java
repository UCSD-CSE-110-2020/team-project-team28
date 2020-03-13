package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wwr.chat.ChatService;
import com.example.wwr.chat.FirebaseFirestoreAdapter;
import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;
import com.google.firebase.firestore.CollectionReference;


public class LogInActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "LogInActivity";

    //new
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";
    private static final String FIRESTORE_CHAT_SERVICE = "FIRESTORE_CHAT_SERVICE";

    String COLLECTION_KEY = "chats";
    String CHAT_ID = "chat1";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    CollectionReference walk;
    ChatService notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("previousActivity") != null &&
                getIntent().getStringExtra("previousActivity").equals("Route Detail")) {
            launchMainActivity(true);
        }
        else if (getIntent().getExtras() != null && getIntent().getExtras().get("mtype") != null &&
                getIntent().getExtras().get("mtype").equals("TeamInvite")){
            String inviteFrom = getIntent().getExtras().get("mfrom").toString();
            String team = getIntent().getExtras().get("mteam").toString();
            goToInvitePage(team, inviteFrom);
        }else if (getIntent().getExtras() != null && getIntent().getExtras().get("mtype") != null &&
                getIntent().getExtras().get("mtype").equals("TeamWalk")){
            switchToProposedRouteScreen();
        }else if(getIntent().getExtras() != null && getIntent().getExtras().get("mtype") != null &&
                getIntent().getExtras().get("mtype").equals("TeamResponse")){
            switchToTeamPage();
        }

        setContentView(R.layout.activity_log_in);

        Button startWWR = findViewById(R.id.startWWRButton);
        startWWR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(false);
            }
        });

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });


        // new
        if (getIntent().hasExtra(CHAT_MESSAGE_SERVICE_EXTRA)) {
            MyApplication.getChatServiceFactory().put(FIRESTORE_CHAT_SERVICE, (chatId ->
                    new FirebaseFirestoreAdapter(COLLECTION_KEY, CHAT_ID, MESSAGES_KEY, FROM_KEY, TEXT_KEY, TIMESTAMP_KEY)));

            String chatServiceKey = getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA);
            if (chatServiceKey == null) {
                chatServiceKey = FIRESTORE_CHAT_SERVICE;
            }
            notifications = MyApplication.getChatServiceFactory().create(chatServiceKey, CHAT_ID);
        } else {

            notifications = MyApplication
                    .getChatServiceFactory()
                    .createFirebaseFirestoreChatService(COLLECTION_KEY, CHAT_ID, MESSAGES_KEY, FROM_KEY, TEXT_KEY, TIMESTAMP_KEY);

        }
    }

    public void launchMainActivity(boolean startPreviousWalk) {

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        //new
        if (getIntent().hasExtra(CHAT_MESSAGE_SERVICE_EXTRA)) {
            intent.putExtra(MainActivity.CHAT_MESSAGE_SERVICE_EXTRA, getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA));
        }
        if (startPreviousWalk) {
            intent.putExtra("previousActivity", "Route Detail");
        }
        startActivity(intent);
    }

    public void goToInvitePage(String teamName, String inviteFrom) {
        Intent intent = new Intent(this, InviteScreenActivity.class);
        intent.putExtra("TEAM", teamName);
        intent.putExtra("FROM", inviteFrom);
        //intent.putExtra(MainActivity.CHAT_MESSAGE_SERVICE_EXTRA,CHAT_MESSAGE_SERVICE_EXTRA);
        startActivity(intent);
    }
    public void switchToProposedRouteScreen() {
        Intent intent = new Intent(this, ProposedWalkDetails.class);
        //intent.putExtra(MainActivity.CHAT_MESSAGE_SERVICE_EXTRA,CHAT_MESSAGE_SERVICE_EXTRA);
        startActivity(intent);
    }

    public void switchToTeamPage() {
        Intent intent = new Intent(this, TeamPageScreen.class);
        intent.putExtra(TeamPageScreen.CHAT_MESSAGE_SERVICE_EXTRA,CHAT_MESSAGE_SERVICE_EXTRA);
        startActivity(intent);
    }


    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }
}

