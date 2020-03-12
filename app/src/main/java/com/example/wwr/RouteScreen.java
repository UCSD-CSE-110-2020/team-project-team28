package com.example.wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.example.wwr.fitness.FitnessService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteScreen extends AppCompatActivity {
    private UserInfo user;
    public static int currentPosition;
    public static RecyclerView routeScreenView;
    public static RecyclerView.Adapter routeAdapter;
    public static RecyclerView.LayoutManager routeLayoutManager;
    public static ArrayList<Route> routeList;
    public static CheckedTextView checkedRoute;
    public String TAG = "Upload to firestore";

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("route list", null);
        Type type = new TypeToken<ArrayList<Route>>() {}.getType();
        routeList = gson.fromJson(json, type);
        Log.d("loadRouteList", "Route list has been loaded");

        if (routeList == null) {
            routeList = new ArrayList<>();
        }

        // Load team Routes
        json = sharedPreferences.getString("team route list", null);
        ArrayList<Route> teamRoute = gson.fromJson(json, type);

        if (teamRoute != null) {
            // Add team routes
            addTeamRoute(teamRoute);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new UserInfo(this);
        setContentView(R.layout.activity_route_screen);
        loadData();

        routeScreenView = findViewById(R.id.routeScreen);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new RouteScreenAdapter(routeList, this);
        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);
        checkedRoute = findViewById(R.id.team_checkedRoute);

        // if the route doesn't exist yet
        if (getIntent().getBooleanExtra("goToDetail", false)) {
            Intent intent = new Intent(this, RoutesActivity.class);
            intent.putExtra("newTime", getIntent().getLongExtra("newTime", 0));
            Log.d("goToDetail", "Add the completed route from walk screen.");
            startActivity(intent);
        }

        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        String currPos = sp.getString("currPos", "0");
        currentPosition = Integer.parseInt(currPos);

        // this gets called when we walk on an existing route
        if (getIntent().getBooleanExtra("updateRoute", false)) {
            if (currentPosition < routeList.size()) {

                // set the route as walked
                routeList.get(currentPosition).setWalked();
                // get the last walk's time, smiles, and distance
                int seconds = (int) user.getLastIntentionalTime();
                long currSteps = user.getLastIntentSteps();
                double miles = user.getLastIntentMiles();

                FitnessService fitnessService = GoogleFitSingleton.getFitnessService();
                fitnessService.updateStepCount();

                routeList.get(currentPosition).updateSteps(currSteps);
                routeList.get(currentPosition).updateSeconds(seconds);
                routeList.get(currentPosition).updateMiles(miles);
                routeAdapter.notifyDataSetChanged();
                Log.d("updateOldWalk", "Update the old walk.");
                saveData();
            }
        }

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButton);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteScreen.super.onBackPressed();
            }
        });

        // add a manual route
        Button addRouteButton = (Button) findViewById(R.id.addRouteButton);
        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);
                intent.putExtra("addNewRoute", true);
                startActivity(intent);
            }
        });

        // Upload the routes
        Button uploadRouteButton = (Button) findViewById(R.id.uploadRouteButton);
        uploadRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> routes = new HashMap<>();
                routes.put("routes", routeList);

                SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String userName = sharedPreferences.getString("userName", "test");

                db.collection("appUsers").document(userName)
                        .update(routes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                Toast.makeText(getApplicationContext(), "Routes successfully uploaded!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                db.collection("appUsers").document(userName)
                                        .set(routes)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                Toast.makeText(getApplicationContext(), "Routes successfully uploaded!",
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
        });
    }

    public static void addToRouteList(String userName, String userEmail, String routeName, String startingLocation, long totalSteps,
                                      double totalMiles, long totalSeconds, String flatOrHilly,
                                      String loopOrOut, String streetOrTrail, String surface,
                                      String difficulty, String note, boolean isFavorite, boolean manuallyAdded) {
        int image = 0;
        if (isFavorite) {
            image = R.drawable.ic_stars_black_24dp;
        }
        routeList.add(new Route(userName, userEmail, routeName, startingLocation, totalSteps, totalMiles,
                totalSeconds, flatOrHilly, loopOrOut, streetOrTrail, surface, difficulty,
                note, isFavorite, image, manuallyAdded));
        routeAdapter.notifyDataSetChanged();
        Log.d("notifyList", "Notify list that the data has been updated.");
    }

    public void saveData() {
        SharedPreferences userPref = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(RouteScreen.routeList);
        editor.putString("route list", json);
        editor.apply();
        Log.d("loadRouteList", "Route list has been saved");
    }

    public void addTeamRoute(ArrayList<Route> teamRouteList) {
        for (Route teamRoute: teamRouteList) {
            boolean isDuplicate = false;
            for (Route myRoute: routeList) {
                if (myRoute.getUserName().equals(teamRoute.getUserName()) && myRoute.getName().equals(teamRoute.getName())
                        && myRoute.getStartLocation().equals(teamRoute.getStartLocation())) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                routeList.add(teamRoute);
            }
        }
    }
}

