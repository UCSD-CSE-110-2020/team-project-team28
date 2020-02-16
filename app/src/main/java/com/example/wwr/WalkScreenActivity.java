package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.wwr.fitness.FitnessService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

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

        Intent previous = getIntent();

        TextView routeName = findViewById(R.id.route_name);

        String name = previous.getStringExtra("route name");

        if (name != null) {
            routeName.setText(name);
        }

        Button endButton = (Button) findViewById(R.id.end_button);
        endButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FitnessService fitnessService = GoogleFitSingleton.getFitnessService();
                try {
                    fitnessService.setFinalStepCount();
                    wait(100000);
                    Log.d("THIRD", "DISPLAY THIRD" + MainActivity.finalSteps);
                } catch (Exception e) {
                }

                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                //String time_string = chronometer.getContentDescription().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putLong("time", time);
                editor.apply();

                String previousString = "";
                previousString = previous.getStringExtra("previousClass");
                Intent intent = new Intent(getApplicationContext(), RouteScreen.class);

                if (previousString != null && previousString.equals("MainActivity")) {
                    intent.putExtra("goToDetail", true);
                    intent.putExtra("newTime", time);
                }

                String previousActivity = previous.getStringExtra("previousScreen");
                if (previousActivity != null && previousActivity.equals("Route Detail")) {
                    intent.putExtra("updateRoute", true);
                    intent.putExtra("newTime", time);
                    try {
                        wait(100000);
                    } catch (Exception e) {

                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }

}
