package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getStringExtra("TEAM")!= null){
            teamName = getIntent().getStringExtra("TEAM");
        }


        Button acceptInviteButton = findViewById(R.id.invite_accept_button);
        acceptInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptInvite();
                finish();
            }
        });

        Button declineInviteButton = findViewById(R.id.invite_decline_button);
        declineInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //acceptInvite(false);
                finish();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void acceptInvite() {
        Toast.makeText(getApplicationContext(), "in create a team", Toast.LENGTH_LONG).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> team = new HashMap<>();
        team.put("team", teamName);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "test");
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("teamName", teamName);
        edit.apply();

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
    }
}

