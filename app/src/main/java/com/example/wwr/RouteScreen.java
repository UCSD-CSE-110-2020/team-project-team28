package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RouteScreen extends AppCompatActivity {
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

        if (routeList == null) {
            routeList = new ArrayList<>();
        }
    }

    public static void notifyInsert() {
        routeAdapter.notifyDataSetChanged();
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

        Intent previous = getIntent();

        if (previous.getBooleanExtra("goToDetail", false)) {
            Intent intent = new Intent(this, RoutesActivity.class);
            startActivity(intent);
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
                startActivity(intent);
            }
        });
    }

    public static void addToRouteList(String routeName, String startingLocation,
                         int totalSteps, double totalMiles, int totalMinutes, String note,
                               boolean isFavorite) {
        int image = 0;
        if (isFavorite) {
            image = R.drawable.ic_stars_black_24dp;
        }
        routeList.add(new Route(routeName, startingLocation, totalSteps, totalMiles,
                totalMinutes, note, isFavorite, image));

        notifyInsert();
    }
}