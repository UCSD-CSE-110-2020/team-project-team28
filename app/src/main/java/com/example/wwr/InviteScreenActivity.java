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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class InviteScreenActivity extends AppCompatActivity {
    private static final String TAG = "InviteScreenActivity";
    String teamName;
    String inviteFrom;

    String accept = " joined your team!";
    String decline = " declined your team request";
    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";

    CollectionReference chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define teamName and inviteFrom depending on whether we are testing or not for mocking.
        if (getIntent().getStringExtra("TEAM")!= null){
            teamName = getIntent().getStringExtra("TEAM");
        }
        if (getIntent().getStringExtra("FROM")!= null){
            inviteFrom = getIntent().getStringExtra("FROM");
        }

        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);


        Button acceptInviteButton = findViewById(R.id.invite_accept_button);
        acceptInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user accepts invite, accept the invite and send the notification message to the team member.
                acceptInvite();
                sendToken(accept);
                finish();
            }
        });

        Button declineInviteButton = findViewById(R.id.invite_decline_button);
        declineInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user declines invite, decline the invite and send the notification message to the team member.
                sendToken(decline);
                finish();
            }
        });
    }


    public void acceptInvite() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> team = new HashMap<>();
        team.put("team", teamName);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");
        String userToken = sharedPreferences.getString("userToken", "test");
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("teamName", teamName);
        edit.apply();

        // Allow the user to be added to the team that he or she is invited to.
        db.collection("appUsers").document(userName)
                .update(team)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getApplicationContext(), "Joined a team!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection("appUsers").document(userName)
                                .set(team)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        Toast.makeText(getApplicationContext(), "Joined a team!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    }
                });

        // Update the user to the pre-existing team.
        Map<String, Object> user = new HashMap<>();
        user.put(userName, userToken);
        db.collection(teamName).document("Members")
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection(teamName).document("Members")
                                .set(user)
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

    // Send a message to a specific user using a token.
    private void sendMessage(String token, String message) {
        // Load userName
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, userName + message);
        newMessage.put("token", token);
        newMessage.put("mtype", "TeamResponse");
        newMessage.put("mteam",teamName);

        chat.add(newMessage).addOnSuccessListener(result -> {
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    private void sendToken(String message) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get the email of the user to send the notification to.
        db.collection("appUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Change the condition to specific user via email from the field
                                if (document.getId().equals(inviteFrom)) {
                                    sendMessage((String) document.get("token"), message);
                                    return;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}

