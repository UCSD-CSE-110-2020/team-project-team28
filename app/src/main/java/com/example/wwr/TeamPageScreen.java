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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TeamPageScreen extends AppCompatActivity {

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
        setContentView(R.layout.activity_team_page);
        routeList = new ArrayList<>();
//        loadData();

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

    public void startAddMember() {
        Intent intent = new Intent(this, AddMemberActivity.class);
        startActivity(intent);
    }

}
