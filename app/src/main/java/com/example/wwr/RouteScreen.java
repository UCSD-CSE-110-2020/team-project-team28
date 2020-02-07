package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class RouteScreen extends AppCompatActivity {
    private RecyclerView routeScreenView;
    private RecyclerView.Adapter routeAdapter;
    private RecyclerView.LayoutManager routeLayoutManager;

    ArrayList<Route> routeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_screen);


       addToRouteList(true, "Home", "California",
               100, 100.0, 5);

        addToRouteList(false, "HomeStruck", "Californian",
                1000, 200.69, 5);

        addToRouteList(true, "Home", "California",
                100, 100.0, 5);

        addToRouteList(false, "HomeStruck", "Californian",
                1000, 200.69, 5);

        addToRouteList(true, "Home", "California",
                100, 100.0, 5);

        addToRouteList(false, "HomeStruck", "Californian",
                1000, 200.69, 5);

        addToRouteList(true, "Home", "California",
                100, 100.0, 5);

        addToRouteList(false, "HomeStruck", "Californian",
                1000, 200.69, 5);

        addToRouteList(true, "Home", "California",
                100, 100.0, 5);

        addToRouteList(false, "HomeStruck", "Californian",
                1000, 200.69, 5);


        routeScreenView = findViewById(R.id.routeScreen);
        routeScreenView.setHasFixedSize(true);
        routeLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new RouteScreenAdapter(routeList);

        routeScreenView.setLayoutManager(routeLayoutManager);
        routeScreenView.setAdapter(routeAdapter);

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButton);

        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void addToRouteList(boolean isFavorite, String routeName, String startingLocation,
                         int totalSteps, double totalMiles, int totalMinutes) {

        if (isFavorite) {
            routeList.add(new Route(routeName, startingLocation, totalSteps, totalMiles,
                    totalMinutes, R.drawable.ic_stars_black_24dp));
        } else {
            routeList.add(new Route(routeName, startingLocation, totalSteps, totalMiles,
                    totalMinutes, 0));
        }
    }
}
