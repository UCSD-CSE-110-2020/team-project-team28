package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TeamRouteDetail extends AppCompatActivity {
    TextView userName;
    TextView name;
    TextView startLocation;
    TextView timeTaken;
    TextView steps;
    TextView distance;
    TextView features;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_route_detail);
        userName = (TextView) findViewById(R.id.team_route_user_name);
        name = (TextView) findViewById(R.id.team_route_detail_title);
        startLocation = (TextView) findViewById(R.id.team_route_detail_start_location);
        timeTaken = (TextView) findViewById(R.id.team_route_detail_time_taken);
        steps = (TextView) findViewById(R.id.team_route_detail_steps);
        distance = (TextView) findViewById(R.id.team_route_detail_distance);
        features = (TextView) findViewById(R.id.team_route_detail_features);
        note = (TextView) findViewById(R.id.team_route_detail_note);

        String routeName = getIntent().getStringExtra("routeName");
        String startingLocation = getIntent().getStringExtra("startLocation");
        userName.setText(getIntent().getStringExtra("team"));
        name.setText(routeName);
        startLocation.setText(startingLocation);
        timeTaken.setText(getIntent().getStringExtra("timeTaken"));
        steps.setText(getIntent().getStringExtra("steps"));
        distance.setText(getIntent().getStringExtra("distance"));
        features.setText(getIntent().getStringExtra("features"));
        note.setText(getIntent().getStringExtra("note"));

        Button proposeWalk = findViewById(R.id.team_route_info_propose_button);
        proposeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProposeWalkActivity.class);
                String routeName = getIntent().getStringExtra("routeName");
                String startingLocation = getIntent().getStringExtra("startLocation");
                intent.putExtra("route name", routeName);
                intent.putExtra("starting location", startingLocation);
                startActivity(intent);
            }
        });

    }
}
