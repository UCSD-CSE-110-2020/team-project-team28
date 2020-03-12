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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wwr.fitness.FitnessService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class TeamRouteScreen extends AppCompatActivity {
    public static RecyclerView routeScreenView;
    public static RecyclerView.Adapter routeAdapter;
    public static RecyclerView.LayoutManager routeLayoutManager;
    public static ArrayList<Route> routeList;
    public static ArrayList<Route> myRouteList;

    public String TAG = "TeamRouteScreen";

    public void loadTeamRoutes() {
        routeList = new ArrayList<>();
        // Load routes of the team members.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            String userName = sharedPreferences.getString("userName", "");
                            String teamName = sharedPreferences.getString("teamName", "");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().equals(userName) && document.get("team") != null && document.get("team").equals(teamName)) {
                                    ArrayList<Map<String, String>> routes = (ArrayList<Map<String, String>>) document.get("routes");
                                    if (routes != null) {
                                        for (Map<String, String> route : routes) {
                                            String steps = String.valueOf(route.get("steps"));
                                            String totalMiles = String.valueOf(route.get("totalMiles"));
                                            String totalSeconds = String.valueOf(route.get("totalSeconds"));
                                            routeList.add(new Route(route.get("userName"), route.get("userEmail"), route.get("name"), route.get("startLocation"),
                                                    Long.parseLong(steps), Double.parseDouble(totalMiles), Long.parseLong(totalSeconds),
                                                    route.get("flatOrHilly"), route.get("loopOrOut"), route.get("streetOrTrail"),
                                                    route.get("surface"), route.get("difficulty"), route.get("note"), false, 0, true));
                                        }
                                    }
                                }
                                saveData();
                                routeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        Log.d("loadRouteList", "Route list has been loaded");
        this.loadMyRouteData();

    }

    public void loadMyRouteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("route list", null);
        Type type = new TypeToken<ArrayList<Route>>() {}.getType();
        myRouteList = gson.fromJson(json, type);
        Log.d("loadRouteList", "Route list has been loaded");
        // Toast.makeText(getApplicationContext(), "Team Routes Loaded!", Toast.LENGTH_LONG).show();

        if (myRouteList == null) {
            myRouteList = new ArrayList<>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_route_screen);
        loadTeamRoutes();

        routeScreenView = findViewById(R.id.teamRouteScreen);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new  TeamRouteScreenAdapter(routeList, this, myRouteList);
        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButtonFromTeam);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamRouteScreen.super.onBackPressed();
            }
        });

        Button currentProposedWalk = findViewById(R.id.current_proposed_walk_button);
        currentProposedWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProposedWalkDetails.class);
                startActivity(intent);
            }
        });
    }

    public void saveData() {
        SharedPreferences userPref = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(routeList);
        editor.putString("team route list", json);
        editor.apply();
        Log.d("loadRouteList", "Route list has been saved");
    }


}
