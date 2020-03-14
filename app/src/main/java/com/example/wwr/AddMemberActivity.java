package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.wwr.chat.ChatService;
import com.example.wwr.chat.FirebaseFirestoreAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMemberActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";

    String from = "You have an invite to join a team from ";
    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";

    CollectionReference chat;
    String email_str;
    String team_str;

    private static final String FIRESTORE_CHAT_SERVICE = "FIRESTORE_CHAT_SERVICE";

    String CHAT_ID = "chat1";

    String TIMESTAMP_KEY = "timestamp";

    CollectionReference walk;
    ChatService notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        /* Set notifications using firebase firestore adapters or real firebase depending on whether
         * we are testing or not. */
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

        EditText email = findViewById(R.id.teamEmail);
        EditText team = findViewById(R.id.teamName);
        SharedPreferences sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String teamName_str = sp.getString("teamName", "Team Name");
        team.setText(teamName_str);

        Button enter = findViewById(R.id.addMember);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_str = email.getText().toString();
                team_str = team.getText().toString();
                // Create the team.
                CreateATeam(team_str);
                Log.d("create team", "creating a new team");
                // Send the token.
                sendToken();
            }
        });

        Button exit = findViewById(R.id.exitAddMember);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exit the screen.
                finish();
            }
        });

    }

    // Send a message to a specific user using a token.
    private void sendMessage(String token) {
        if (from == null || from.isEmpty()) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        // Save the token and the user name.
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, from + userName);
        newMessage.put("token", token);
        newMessage.put("mtype", "TeamInvite");
        newMessage.put("mteam",team_str);

        notifications.addMessage(newMessage).addOnSuccessListener(result -> {
            Log.d(TAG, "message success: " + newMessage);
        });
    }

    // Send message to the specific user using a token.
    private void sendToken() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get the email of the user to send the notification to.
        db.collection("appUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Change the condition to specific user via email from the field
                                if (document.get("email") != null && document.get("email").equals(email_str)) {
                                    sendMessage((String) document.get("token"));

                                    return;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    // Add a user to the specified team name.
    public void CreateATeam(String teamName) {
        Toast.makeText(getApplicationContext(), "in create a team", Toast.LENGTH_LONG).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> team = new HashMap<>();
        team.put("team", teamName);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");
        String userToken = sharedPreferences.getString("userToken", "test");
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("teamName", teamName);
        edit.apply();

        // Add user to the list of appUsers.
        db.collection("appUsers").document(userName)
                .update(team)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getApplicationContext(), "Created a team!",
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
                                        Toast.makeText(getApplicationContext(), "Created a team!",
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

        // Add user to the team.
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
}

