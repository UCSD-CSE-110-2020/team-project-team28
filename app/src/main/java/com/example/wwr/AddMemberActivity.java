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

    String from = "You have an invite to join a team from ";
    String COLLECTION_KEY = "chats";
    String CHAT_ID = "chat1";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    String email_str;

    //String TAG = AddMemberActivity.class.getSimpleName();

    //String COLLECTION_KEY = "chats";
    //String DOCUMENT_KEY = "chats1";
    //String MESSAGES_KEY = "messages";
    //String FROM_KEY = "from";
    //String TEXT_KEY = "text";
    //String TIMESTAMP_KEY = "timestamp";

    //CollectionReference chat;
    //String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);

        EditText email = findViewById(R.id.teamEmail);

        Button enter = findViewById(R.id.addMember);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_str = email.getText().toString();
                sendToken();
            }
        });

        Button exit = findViewById(R.id.exitAddMember);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendMessage();
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

        // Load userName
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userName);
        newMessage.put(TEXT_KEY, from + userName);
        newMessage.put("token", token);
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();

        chat.add(newMessage).addOnSuccessListener(result -> {
            //messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    private void sendToken() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get the email of the user to send the notification to.
        db.collection("appUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // TODO: Get the email from the field and find the token of the user corresponding to that email.

                            SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            String userName = sharedPreferences.getString("userName", "test");

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Change the condition to specific user via email from the field
                                if (document.get("email").equals(email_str)) {
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

}
