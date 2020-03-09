package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ProposeWalkActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String from = "You have an invite to join a team from ";
    String COLLECTION_KEY = "notifications";
    String CHAT_ID = "chat1";
    String DOCUMENT_KEY = "proposed";
    String MESSAGES_KEY = "team_walk";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    String routeName;
    String startingLocation;
    String information;
    String date;
    String time;
    CollectionReference chat;
    String email_str;

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
                addWalkToFirebase();
                sendMessage();
            }
        });

        // Get route name and starting location
        routeName = getIntent().getStringExtra("route name");
        startingLocation = getIntent().getStringExtra("starting location");
        information = routeName + "\n" + startingLocation;



        TextView routeDetail = findViewById(R.id.proposed_walk_details);
        routeDetail.setText(information);
    }

    // Send a message to a specific user using a token.
    private void sendMessage() {
        if (from == null || from.isEmpty()) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load userName
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, "invite to team walk from " + userName);
        //newMessage.put("token", token);
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

        Map<String, Object> walkInfo = new HashMap<>();
        walkInfo.put("Route", routeName);
        walkInfo.put("StartingLocation", startingLocation);
        walkInfo.put("Date", date);
        walkInfo.put("Time", time);
        walkInfo.put("Owner", userName);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("team").document("proposedWalk")
                .update(walkInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection("team").document("proposedWalk")
                                .set(walkInfo)
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

}
