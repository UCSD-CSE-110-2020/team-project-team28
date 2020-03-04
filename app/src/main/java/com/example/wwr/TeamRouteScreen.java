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

public class TeamRouteScreen extends AppCompatActivity {
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
        setContentView(R.layout.activity_team_route_screen);
        routeList = new ArrayList<>();
//        loadData();

        routeScreenView = findViewById(R.id.teamRouteScreen);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new TeamRouteScreenAdapter(routeList, this);
        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButtonFromTeam);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamRouteScreen.super.onBackPressed();
            }
        });
    }

}
