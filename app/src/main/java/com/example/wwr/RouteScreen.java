package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wwr.fitness.FitnessService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RouteScreen extends AppCompatActivity {
    private UserInfo user;
    public static RecyclerView routeScreenView;
    public static RecyclerView.Adapter routeAdapter;
    public static RecyclerView.LayoutManager routeLayoutManager;
    public static ArrayList<Route> routeList;

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

        // if the route doesn't exist yet
        if (getIntent().getBooleanExtra("goToDetail", false)) {
            Intent intent = new Intent(this, RoutesActivity.class);
            intent.putExtra("newTime", getIntent().getLongExtra("newTime", 0));
            Log.d("goToDetail", "Add the completed route from walk screen.");
            startActivity(intent);
        }

        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        String currPos = sp.getString("currPos", "0");

        // this gets called when we have a walk on an existing route
        int currentPosition = Integer.parseInt(currPos);

        if (getIntent().getBooleanExtra("updateRoute", false)) {

            if (currentPosition < routeList.size()) {

                // get the last walk's time, smiles, and distance
                int seconds = (int)user.getLastIntentionalTime();
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

        Button addRouteButton = (Button) findViewById(R.id.addRouteButton);
        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);
                intent.putExtra("addNewRoute", true);
                startActivity(intent);
            }
        });
    }

    public static void addToRouteList(String routeName, String startingLocation, long totalSteps,
                                      double totalMiles, long totalSeconds, String flatOrHilly,
                                      String loopOrOut, String streetOrTrail, String surface,
                                      String difficulty, String note, boolean isFavorite) {
        int image = 0;
        if (isFavorite) {
            image = R.drawable.ic_stars_black_24dp;
        }
        routeList.add(new Route(routeName, startingLocation, totalSteps, totalMiles,
                totalSeconds, flatOrHilly, loopOrOut, streetOrTrail, surface, difficulty,
                note, isFavorite, image));
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

}
