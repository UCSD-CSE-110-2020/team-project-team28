package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProposeWalkActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String from = "You have an invite to join a team from ";
    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";

    String routeName;
    String startingLocation;
    String information;
    String date;
    String time;
    String status;
    CollectionReference chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_walk);
        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button proposeWalk = findViewById(R.id.propose_walk_propose_button);
        proposeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "Proposed";
                addWalkToFirebase();
                sendMessageToTeam(" proposed a team walk!");
            }
        });

        Button cancelWalk = findViewById(R.id.propose_walk_cancel_button);
        cancelWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "Canceled";
                addWalkToFirebase();
                sendMessageToTeam(" canceled the team walk.");
            }
        });

        Button exitButton = findViewById(R.id.propose_walk_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button scheduleWalk = findViewById(R.id.propose_walk_schedule_button);
        scheduleWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "Scheduled";
                addWalkToFirebase();
                sendMessageToTeam(" scheduled the team walk!");
            }
        });

        // Get route name and starting location
        if (getIntent().getStringExtra("route name") != null && getIntent().getStringExtra("starting location")!=null) {
            routeName = getIntent().getStringExtra("route name");
            startingLocation = getIntent().getStringExtra("starting location");
            information = routeName + "\n" + startingLocation;
            TextView routeDetail = findViewById(R.id.proposed_walk_details);
            routeDetail.setText(information);
        } else {
            loadRouteInfo();
        }
    }

    //send a message to the entire team
    private void sendMessageToTeam(String message){
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String teamName = sharedPreferences.getString("teamName", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(teamName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals("Members")) {
                                for (Map.Entry<String, Object> entry : document.getData().entrySet()){
                                    sendMessage(message, entry.getValue().toString());
                                }
                            }
                        }
                    }
                });
    }

    // Send a message to a specific user using a token.
    private void sendMessage(String message, String token) {
        if (from == null || from.isEmpty()) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load userName
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, userName + message);
        newMessage.put("token", token);
        newMessage.put("mtype", "TeamWalk");
        newMessage.put("mteam", "team name");
        //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();

        chat.add(newMessage).addOnSuccessListener(result -> {
            //messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    public void addWalkToFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        TextView proposedTime = findViewById(R.id.propose_walk_time);
        time = proposedTime.getText().toString();
        TextView proposedDate = findViewById(R.id.propose_walk_date);
        date = proposedDate.getText().toString();
        String teamName = sharedPreferences.getString("teamName", "");

        Map<String, Object> walkInfo = new HashMap<>();
        walkInfo.put("Route", routeName);
        walkInfo.put("StartingLocation", startingLocation);
        walkInfo.put("Date", date);
        walkInfo.put("Time", time);
        walkInfo.put("Owner", userName);
        walkInfo.put("Status", status);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(teamName).document("proposedWalk")
                .update(walkInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        // delete previous walk status
                        if (status.equals("Proposed")){
                            db.collection(teamName).document("status").delete();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection(teamName).document("proposedWalk")
                                .set(walkInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully created!");
                                        // delete previous walk status
                                        db.collection(teamName).document("status").delete();
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

    public void loadRouteInfo() {
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
                               if (document.getId().equals("proposedWalk")){
                                    routeName = document.get("Route").toString();
                                    startingLocation = document.get("StartingLocation").toString();
                                   information = routeName + "\n" + startingLocation;
                                   Log.d("route loaded", routeName);
                                   TextView routeDetail = findViewById(R.id.proposed_walk_details);
                                   routeDetail.setText(information);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
