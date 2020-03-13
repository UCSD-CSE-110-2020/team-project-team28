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
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";
    CollectionReference chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_details);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String teamName = sharedPreferences.getString("teamName", "");


        if (teamName != ""){
            loadTeamRoute();
        }

        if (getIntent().getStringExtra("TEST") != null) {
            chat = null;
        } else {
            chat = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY)
                    .collection(MESSAGES_KEY);
        }

        statusGroup = findViewById(R.id.groupStatus);


        Button exit = findViewById(R.id.proposed_walk_exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked = findViewById(statusGroup.getCheckedRadioButtonId());
                if (checked != null) {
                    status = checked.getText().toString();
                    addUserStatusToFirebase();
                    sendMessage(" updated their status to " + status);
                }
                finish();
            }
        });

        editWalk = findViewById(R.id.edit_proposed_walk_button);
        editWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToProposeWalkActivity();
            }
        });
    }

    public void switchToProposeWalkActivity() {
        Intent intent = new Intent(this, ProposeWalkActivity.class);
        startActivity(intent);
    }

    public void addUserStatusToFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String teamName = sharedPreferences.getString("teamName", "");

        Map<String, Object> users = new HashMap<>();
        users.put(userName, status);

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
                                        //display team status
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
                                        //display route details
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

    private void getOwnerToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
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
        // Load userName
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, userName + message);
        newMessage.put("token", owner_token);
        newMessage.put("mtype", "TeamWalk");
        newMessage.put("mteam", "team name");
        //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();

        chat.add(newMessage).addOnSuccessListener(result -> {
            //messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }
}
