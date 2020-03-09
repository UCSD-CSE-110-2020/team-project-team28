package com.example.wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_details);
        loadTeamRoute();


        statusGroup = findViewById(R.id.groupStatus);


        Button exit = findViewById(R.id.proposed_walk_exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked = findViewById(statusGroup.getCheckedRadioButtonId());
                status = checked.getText().toString();
                addUserStatusToFirebase();
                finish();
            }
        });



    }


    public void addUserStatusToFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");

        Map<String, Object> users = new HashMap<>();
        users.put(userName, status);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("team").document("status")
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
                        db.collection("team").document("status")
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
        db.collection("team")
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
                                        walkDetails_str = document.get("Route").toString() + "\n" + document.get("StartingLocation") +
                                                "\n" + "Owner: " + document.get("Owner").toString() + "\n" + "Date: " + document.get("Date") +
                                        "\n" + "Time: " + document.get("Time");
                                        Log.d(TAG, walkDetails_str);
                                        //display route details
                                        TextView details = findViewById(R.id.proposed_walk_details);
                                        details.setText(walkDetails_str);
                                    }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



        //String route_str = db.collection("team").document("proposedWalk").;


    }
}
