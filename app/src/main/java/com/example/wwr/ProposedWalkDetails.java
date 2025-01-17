package com.example.wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wwr.chat.ChatService;
import com.example.wwr.chat.FirebaseFirestoreAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProposedWalkDetails extends AppCompatActivity {
    private static final String TAG = "ProposedWalkDetails";
    RadioGroup statusGroup;
    String status;
    ArrayList<String> teamStatus;
    String teamStatus_str = "";
    String walkDetails_str = "";
    String owner_str;
    String owner_token;
    Button editWalk;
    String COLLECTION_KEY = "chats";
    String CHAT_ID = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";
    CollectionReference chat;
    ChatService notifications;
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";
    private static final String FIRESTORE_CHAT_SERVICE = "FIRESTORE_CHAT_SERVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_details);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String teamName = sharedPreferences.getString("teamName", "");

        // If the teamName exists, load the team routes.
        if (teamName != ""){
            loadTeamRoute();
        }

        // Initializes chat and notifications so that we can mock this class while testing.
        if (getIntent().getStringExtra("TEST") != null) {
            chat = null;
            notifications = null;
        } else {
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

        // Find the radio group for the 3 options: accept, decline: bad time, decline: bad route.
        statusGroup = findViewById(R.id.groupStatus);
        Button exit = findViewById(R.id.proposed_walk_exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check which option was selected.
                RadioButton checked = findViewById(statusGroup.getCheckedRadioButtonId());
                // If an option was selected, send the appropriate message to the team members.
                if (checked != null) {
                    status = checked.getText().toString();
                    addUserStatusToFirebase();
                    sendMessage(" updated their status to " + status);
                }
                // Exit the activity.
                finish();
            }
        });

        editWalk = findViewById(R.id.edit_proposed_walk_button);
        editWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            // Switch to the propose walk activity.
            public void onClick(View v) {
                switchToProposeWalkActivity();
            }
        });
    }

    public void switchToProposeWalkActivity() {
        // Go to proposed walk activity.
        Intent intent = new Intent(this, ProposeWalkActivity.class);
        startActivity(intent);
    }

    public void addUserStatusToFirebase() {
        // Add user status to the firebase.
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String teamName = sharedPreferences.getString("teamName", "");

        Map<String, Object> users = new HashMap<>();
        users.put(userName, status);

        // Update the status of the member on the proposed walk.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(teamName).document("status")
                .update(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection(teamName).document("status")
                                .set(users)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully created!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                        Toast.makeText(getApplicationContext(), "Upload failed!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    public void loadTeamRoute() {
        // Load the status of the team members on the route.
        teamStatus = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String teamName = sharedPreferences.getString("teamName", "");
        Log.d("team name", teamName);

        // Get team members
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(teamName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getId().equals("status")) {
                                        // Display team status.
                                        for (Map.Entry<String, Object> entry : document.getData().entrySet()){
                                            teamStatus_str += entry.getKey() + ":  " + entry.getValue() + "\n";
                                        }
                                        TextView team_status = findViewById(R.id.proposed_walk_team_details);
                                        team_status.setText(teamStatus_str);

                                    } else if (document.getId().equals("proposedWalk")){
                                        walkDetails_str = document.get("Route").toString() + "\n" + document.get("StartingLocation").toString() +
                                                "\n" + "Owner: " + document.get("Owner").toString() + "\n" + "Date: " + document.get("Date").toString() +
                                        "\n" + "Time: " + document.get("Time").toString() + "\n" + "Status: " + document.get("Status").toString();
                                        Log.d(TAG, walkDetails_str);
                                        // Display route details.
                                        TextView details = findViewById(R.id.proposed_walk_details);
                                        details.setText(walkDetails_str);
                                        owner_str =document.get("Owner").toString();
                                        getOwnerToken();
                                        if (owner_str != null && owner_str.equals(userName)){
                                            editWalk.setVisibility(View.VISIBLE);
                                        }
                                    }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void getOwnerToken() {
        // Get the specific token of the user.
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String teamName = sharedPreferences.getString("teamName", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (!teamName.equals("")) {
            db.collection(teamName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals("Members")) {
                                    owner_token = document.get(owner_str).toString();
                                }
                            }
                        }
                    });
        }
    }

    private void sendMessage(String message) {
        // Send message to the speicific user with the specific status of the walk.
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, userName + message);
        newMessage.put("token", owner_token);
        newMessage.put("mtype", "TeamWalk");
        newMessage.put("mteam", "team name");
        notifications.addMessage(newMessage).addOnSuccessListener(result -> {
            Log.d(TAG, "message success: " + newMessage);
        });
    }
}
