package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class RouteScreen extends AppCompatActivity {
    public static RecyclerView routeScreenView;
    public static RecyclerView.Adapter routeAdapter;
    public static RecyclerView.LayoutManager routeLayoutManager;

    public static ArrayList<Route> routeList = new ArrayList<Route>();


    public static void notifyInsert() {
        routeAdapter.notifyItemInserted(routeList.size() - 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_screen);

        routeScreenView = findViewById(R.id.routeScreen);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new RouteScreenAdapter(routeList, this);

        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButton);

        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        if (isFavorite) {
            routeList.add(new Route(routeName, startingLocation, totalSteps, totalMiles,
                    totalMinutes, note, isFavorite, R.drawable.ic_stars_black_24dp));
        } else {
            routeList.add(new Route(routeName, startingLocation, totalSteps, totalMiles,
                    totalMinutes, note, isFavorite, 0));
        }
    }
}
