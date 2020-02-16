package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.wwr.fitness.FitnessService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class WalkScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        Button endButton = (Button) findViewById(R.id.end_button);

        Intent previous = getIntent();

        TextView routeName = findViewById(R.id.route_name);

        String name = previous.getStringExtra("route name");

        if (name != null) {
            routeName.setText(name);
        }

        endButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                //String time_string = chronometer.getContentDescription().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putLong("time", time);
                editor.apply();

                String previousString = "";

                previousString = previous.getStringExtra("previousClass");

                Intent intent = new Intent(getApplicationContext(), RouteScreen.class);
                intent.putExtra("newTime", time);

                if (previousString != null && previousString.equals("MainActivity")) {
                    intent.putExtra("goToDetail", true);
                }

                String previousActivity = previous.getStringExtra("previousActivity");
                if (previousActivity != null && previousActivity.equals("Route Detail")) {
                    intent.putExtra("updateRoute", true);
                }

                FitnessService fitnessService = GoogleFitSingleton.getFitnessService();
                fitnessService.setFinalStepCount();
                startActivity(intent);

                finish();
            }
        });
    }

}
