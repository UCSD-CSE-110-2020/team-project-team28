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

    ArrayList<RouteScreenList> routeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_screen);

//        ArrayList<RouteScreenList> routeList = new ArrayList<>();

//        routeList.add(new RouteScreenList(R.drawable.ic_stars_black_24dp,
//                "Kyire", "A", "B", "C"));
//
//        routeList.add(new RouteScreenList(R.drawable.ic_stars_black_24dp,
//                "ro", "c", "a", "b"));

        addToRouteList(true, "Home", "Kyrie", "Lee",
                "Wow");
        addToRouteList(false, "Home", "Kye", "Lee6",
                "Wowzers");

        addToRouteList(true, "Home", "Kyrie", "Lee",
                "Wow");
        addToRouteList(false, "Home", "Kye", "Lee6",
                "Wowzers");
        addToRouteList(true, "Home", "Kyrie", "Lee",
                "Wow");
        addToRouteList(false, "Home", "Kye", "Lee6",
                "Wowzers");
        addToRouteList(true, "Home", "Kyrie", "Lee",
                "Wow");
        addToRouteList(false, "Home", "Kye", "Lee6",
                "Wowzers");
        addToRouteList(true, "Home", "Kyrie", "Lee",
                "Wow");
        addToRouteList(false, "Home", "Kye", "Lee6",
                "Wowzers");



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

    public void addToRouteList(boolean isFavorite, String routeName, String text2,
                         String text3, String text4) {

        if (isFavorite) {
            routeList.add(new RouteScreenList(R.drawable.ic_stars_black_24dp,
                    routeName, text2, text3, text4));
        } else {
            routeList.add(new RouteScreenList(0,
                    routeName, text2, text3, text4));
        }
    }
}
