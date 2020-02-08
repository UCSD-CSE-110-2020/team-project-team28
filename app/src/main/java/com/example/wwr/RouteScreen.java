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


       addToRouteList("Home", "California",
                6510, 110.0, 70, "Great Run", true);

       addToRouteList("Staples", "California",
                320, 10.0, 5, "Not good", false);

        addToRouteList("Wedding", "Miami",
                40000, 230.0, 300, "Wow", true);

        addToRouteList("Home", "California",
                6510, 110.0, 70, "Great Run asdfddfaksfjlfjlasjflajdfladjslfjaklsdjflkadsjfkladsjflkdjasklfjdaslkfjaldksjfklajkfajl", true);

        addToRouteList("Staples", "California",
                320, 10.0, 5, "Not good", false);

        addToRouteList("Wedding", "Miami",
                40000, 230.0, 300, "Wow", true);


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
    }

    public void addToRouteList(String routeName, String startingLocation,
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
