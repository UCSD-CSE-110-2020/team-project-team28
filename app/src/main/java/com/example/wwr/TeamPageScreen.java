package com.example.wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamPageScreen extends AppCompatActivity {
    public static RecyclerView routeScreenView;
    public static RecyclerView.Adapter routeAdapter;
    public static RecyclerView.LayoutManager routeLayoutManager;
    public static ArrayList<Route> routeList;

    private static final String TAG = "TeamPageScreen";
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";

    public void loadTeamUsers() {
        addTestMembers();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String teamName = sharedPreferences.getString("teamName", "");
        Log.d("team name", teamName);

        // Get team members
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("team") != null &&  document.get("email") != null && document.get("team").toString().equals(teamName))
                                    if (!document.getId().equals(userName) && !hasTeamMember(document.getId())) {
                                        routeList.add(new Route((String) document.getId(), (String) document.get("email"), "",
                                                "", 0, 0, 0, "", "",
                                                "", "", "", "", false, 0, true));
                                        routeAdapter.notifyDataSetChanged();
                                    }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        Log.d("loadRouteList", "Route list has been loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_page);
        uploadUserInformation();
        routeList = new ArrayList<>();
        loadTeamUsers();

        routeScreenView = findViewById(R.id.teamPage);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new TeamPageScreenAdapter(routeList, this);
        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);

        Button backToMainMenu = (Button) findViewById(R.id.backToMainFromTeamPage);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamPageScreen.super.onBackPressed();
            }
        });

        Button addMember = findViewById(R.id.addMemberButton);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddMember();
            }
        });

    }

    public void startAddMember(){
        Intent intent = new Intent(this, AddMemberActivity.class);
        //new
        if (getIntent().hasExtra(CHAT_MESSAGE_SERVICE_EXTRA)) {
            intent.putExtra(MainActivity.CHAT_MESSAGE_SERVICE_EXTRA, getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA));
        }
        startActivity(intent);
    }

    public void uploadUserInformation() {
        addTokenToFirebase();
        addEmailToFirebase();
    }


    public boolean hasTeamMember(String userName) {
        for (Route route: routeList) {
            if (route.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void addTokenToFirebase() {
        // Get user token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // Log and toast
                        Log.d(TAG, token);
                        addToken(token);
                    }
                });
    }

    public void addToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "FirebaseTests");
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("userToken", token);
        edit.apply();


        Map<String, Object> userToken = new HashMap<>();
        userToken.put("token", token);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appUsers").document(userName)
                .update(userToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection("appUsers").document(userName)
                                .set(userToken)
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


    public void addEmailToFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Test");
        String userEmail = sharedPreferences.getString("userEmail", "Test");

        Map<String, Object> email = new HashMap<>();
        email.put("email", userEmail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appUsers").document(userName)
                .update(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection("appUsers").document(userName)
                                .set(email)
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

    private void addTestMembers(){
        Gson gson = new Gson();
        SharedPreferences tprefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String storedHashMapString = tprefs.getString("team_members", "");
        String hi = tprefs.getString("hi", "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);

        if (testHashMap2 == null || testHashMap2.isEmpty()){
            return;
        }
        for(Map.Entry<String,String> entry: testHashMap2.entrySet()){
            routeList.add(new Route((String) entry.getKey(), entry.getValue(), "",
                    "", 0, 0, 0, "", "",
                    "", "", "", "", false, 0, true));
        }
    }


}
