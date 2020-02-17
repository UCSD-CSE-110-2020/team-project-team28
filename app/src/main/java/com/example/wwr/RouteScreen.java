package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wwr.fitness.FitnessService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RouteScreen extends AppCompatActivity {
    public static RecyclerView routeScreenView;
    public static RecyclerView.Adapter routeAdapter;
    public static RecyclerView.LayoutManager routeLayoutManager;
    public static ArrayList<Route> routeList;
    public static int currentPosition;

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("route list", null);
        Type type = new TypeToken<ArrayList<Route>>() {}.getType();
        routeList = gson.fromJson(json, type);

        if (routeList == null) {
            routeList = new ArrayList<>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_screen);

        loadData();

        routeScreenView = findViewById(R.id.routeScreen);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new RouteScreenAdapter(routeList, this);

        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);

        if (getIntent().getBooleanExtra("goToDetail", false)) {
            Intent intent = new Intent(this, RoutesActivity.class);
            intent.putExtra("newTime", getIntent().getLongExtra("newTime", 0));
            startActivity(intent);
        }

        if (getIntent().getBooleanExtra("updateRoute", false)) {
            if (this.currentPosition < routeList.size()) {

                //Toast.makeText(getApplicationContext(), "I'm here bitch", Toast.LENGTH_LONG).show();

                int seconds = (int) getIntent().getLongExtra("newTime", 0) / 1000;
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                long previousSteps = prefs.getLong("totalSteps", 0);

                //long steps = MainActivity.finalSteps - MainActivity.startSteps;
                //long steps = MainActivity.startSteps - previousSteps;
                FitnessService fitnessService = GoogleFitSingleton.getFitnessService();
                fitnessService.updateStepCount();
                long steps = MainActivity.startSteps - previousSteps;

                //long steps = MainActivity.startSteps;

                Toast.makeText(getApplicationContext(), steps + "", Toast.LENGTH_LONG).show();
                routeList.get(this.currentPosition).updateSteps(steps);
                routeList.get(this.currentPosition).updateSeconds(seconds);
                routeAdapter.notifyDataSetChanged();
                saveData();
            }
        }

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButton);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
    }

    public static void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public void saveData() {
        SharedPreferences userPref = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(RouteScreen.routeList);
        editor.putString("route list", json);
        editor.apply();
    }

}