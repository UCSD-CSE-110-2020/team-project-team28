package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

public class RouteDetail extends AppCompatActivity {
    TextView memberName;
    TextView name;
    TextView startLocation;
    TextView timeTaken;
    TextView steps;
    TextView distance;
    TextView features;
    TextView note;
    CheckBox isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        name = (TextView) findViewById(R.id.route_detail_title);
        startLocation = (TextView) findViewById(R.id.route_detail_start_location);
        timeTaken = (TextView) findViewById(R.id.route_detail_time_taken);
        steps = (TextView) findViewById(R.id.route_detail_steps);
        distance = (TextView) findViewById(R.id.route_detail_distance);
        features = (TextView) findViewById(R.id.route_detail_features);
        note = (TextView) findViewById(R.id.route_detail_note);
        memberName = findViewById(R.id.route_detail_member_name);

        String routeName = getIntent().getStringExtra("routeName");
        String startingLocation = getIntent().getStringExtra("startLocation");
        memberName.setText(getIntent().getStringExtra("memberName"));
        name.setText(routeName);
        startLocation.setText(startingLocation);
        timeTaken.setText(getIntent().getStringExtra("timeTaken"));
        steps.setText(getIntent().getStringExtra("steps"));
        distance.setText(getIntent().getStringExtra("distance"));
        features.setText(getIntent().getStringExtra("features"));
        note.setText(getIntent().getStringExtra("note"));

        Button startFromExistingRoute = (Button) findViewById(R.id.route_info_start_button);
        startFromExistingRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                intent.putExtra("route name", getIntent().getStringExtra("routeName"));
                intent.putExtra("previousActivity", "Route Detail");
                Log.d("restartWalk", "Restart existing walk.");
                startActivity(intent);
            }
        });

        CheckBox favoriteBtn = (CheckBox) findViewById(R.id.favorite_btn);
        int position = getIntent().getIntExtra("position", 0);
        if (RouteScreen.routeList != null) {
            favoriteBtn.setChecked(RouteScreen.routeList.get(position).getFavorite());
        }
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteScreen.routeList.get(position).setFavorite( (favoriteBtn.isChecked()) );
                saveData();
                RouteScreen.routeAdapter.notifyDataSetChanged();
            }
        });

        Button proposeWalk = findViewById(R.id.route_info_propose_button);
        proposeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getApplicationContext(), ProposeWalkActivity.class);
                String routeName = getIntent().getStringExtra("routeName");
                String startingLocation = getIntent().getStringExtra("startLocation");
                intent.putExtra("route name", routeName);
                intent.putExtra("starting location", startingLocation);
                startActivity(intent);
            }
        });

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
