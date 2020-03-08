package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wwr.fitness.FitnessService;

import static com.example.wwr.RouteScreen.currentPosition;

public class RouteDetail extends AppCompatActivity {
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


        name.setText(getIntent().getStringExtra("routeName"));
        startLocation.setText(getIntent().getStringExtra("startLocation"));
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

        favoriteBtn.setChecked(RouteScreen.routeList.get(RouteScreen.currentPosition).getFavorite());

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RouteScreen.routeList.get(RouteScreen.currentPosition).setFavorite( (favoriteBtn.isChecked()) );

                RouteScreen.routeAdapter.notifyDataSetChanged();

            }
        });

    }
}
