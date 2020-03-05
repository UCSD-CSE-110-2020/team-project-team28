package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMemberActivity extends AppCompatActivity {

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
        //SharedPreferences sharedPreferences = getSharedPreferences("Notifications", Context.MODE_PRIVATE);

        //from = sharedPreferences.getString(FROM_KEY, null);

        //chat = FirebaseFirestore.getInstance()
        //        .collection(COLLECTION_KEY)
        //        .document(DOCUMENT_KEY)
        //        .collection(MESSAGES_KEY);

        //initMessageUpdateListener();

        EditText email = findViewById(R.id.teamEmail);

        Button enter = findViewById(R.id.addMember);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //something about sending this user the notification
                //sendNotification();
            }
        });

        Button exit = findViewById(R.id.exitAddMember);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //email.addTextChangedListener(new TextWatcher() {
        //    @Override
        //    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        //    }

        //    @Override
        //    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //        from = s.toString();
        //        sharedPreferences.edit().putString(FROM_KEY, from).apply();
        //    }

        //    @Override
        //    public void afterTextChanged(Editable s) {

        //    }
        //});
    }

    //private void sendNotification() {
    //    if (from == null || from.isEmpty()) {
    //        Toast.makeText(this, "Enter an email", Toast.LENGTH_LONG).show();
    //        return;
    //    }

    //    EditText messageView = findViewById(R.id.teamEmail);

    //    Map<String, String> newMessage = new HashMap<>();
    //    newMessage.put(FROM_KEY, from);
    //    newMessage.put(TEXT_KEY, from + " has invited you to a team");

    //    chat.add(newMessage).addOnSuccessListener(result -> {
    //        messageView.setText("Insert Email");
    //    }).addOnFailureListener(error -> {
    //        Log.e(TAG, error.getLocalizedMessage());
    //    });
    //}

    //private void initMessageUpdateListener() {
    //    chat.addSnapshotListener((newChatSnapShot, error) -> {
    //        if (error != null) {
    //            Log.e(TAG, error.getLocalizedMessage());
    //            return;
    //        }

    //        if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
    //            StringBuilder sb = new StringBuilder();
    //            List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
    //            documentChanges.forEach(change -> {
    //                QueryDocumentSnapshot document = change.getDocument();
    //                sb.append(document.get(FROM_KEY));
    //                sb.append(":\n");
    //                sb.append(document.get(TEXT_KEY));
    //                sb.append("\n");
    //                sb.append("---\n");
    //            });


    //            //TextView chatView = findViewById(R.id.chat);
    //            //chatView.append(sb.toString());
    //        }
    //    });
    //}
}
