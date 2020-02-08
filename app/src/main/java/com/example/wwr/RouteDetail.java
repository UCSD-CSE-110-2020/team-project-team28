package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RouteDetail extends AppCompatActivity {
    TextView name;
    TextView startLocation;
    TextView timeTaken;
    TextView steps;
    TextView distance;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        name = (TextView) findViewById(R.id.route_detail_title);
        startLocation = (TextView) findViewById(R.id.route_detail_start_location);
        timeTaken = (TextView) findViewById(R.id.route_detail_time_taken);
        steps = (TextView) findViewById(R.id.route_detail_steps);
        distance = (TextView) findViewById(R.id.route_detail_distance);
        note = (TextView) findViewById(R.id.route_detail_note);

        name.setText(getIntent().getStringExtra("routeName"));
        startLocation.setText(getIntent().getStringExtra("startLocation"));
        timeTaken.setText(getIntent().getStringExtra("timeTaken"));
        steps.setText(getIntent().getStringExtra("steps"));
        distance.setText(getIntent().getStringExtra("distance"));
        note.setText(getIntent().getStringExtra("note"));

    }
}
