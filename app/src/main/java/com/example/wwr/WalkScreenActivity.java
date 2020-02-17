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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WalkScreenActivity extends AppCompatActivity {
    private int mockStartTime;
    private int mockEndTime;
    private int mockTotalTime = 0;
    private boolean startPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Chronometer chronometer = findViewById(R.id.chronometer);
        Intent previous = getIntent();

        TextView routeName = findViewById(R.id.route_name);

        String name = previous.getStringExtra("route name");

        if (name != null) {
            routeName.setText(name);
            Log.d("routeNameSet", "Route name has been set");
        }

        Button startWalk = (Button) findViewById(R.id.startWalk);
        startWalk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startPressed = true;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        // button to grab mock time
        Button newStartTime = (Button) findViewById(R.id.submitStartTime);
        Button newEndTime = (Button) findViewById(R.id.submitEndTime);

        newStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText x = (EditText) findViewById(R.id.startTimeEdit);
                String time = x.getText().toString();
                mockStartTime = Integer.parseInt(time);
                chronometer.stop();
            }
        });

        newEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText x = (EditText) findViewById(R.id.endTimeEdit);
                String time = x.getText().toString();
                mockEndTime = Integer.parseInt(time);
                mockTotalTime = mockEndTime - mockStartTime;
            }
        });


        Button addSteps = (Button) findViewById(R.id.addMockSteps);
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.stepMultiplier++;
                SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                boolean firstMultiplier = prefs.getBoolean("firstMultiplier", true);
                SharedPreferences.Editor editor = prefs.edit();
                if (firstMultiplier) {
                    editor.putBoolean("firstMultiplier", false);
                    editor.putInt("stepMultiplierCount", 1);
                    editor.apply();
                } else {
                    int temp = prefs.getInt("stepMultiplierCount", 0);
                    editor.putInt("stepMultiplierCount", ++temp);
                    editor.apply();
                }
            }
        });

        // new addition - update the steps so we have a starting point for the walk
        FitnessService fitnessService = GoogleFitSingleton.getFitnessService();
        fitnessService.updateStepCount();

        //Toast.makeText(getApplicationContext(), String.valueOf(MainActivity.startSteps), Toast.LENGTH_LONG).show();
        Button endButton = (Button) findViewById(R.id.end_button);
        endButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    // when end the walk
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("totalSteps", MainActivity.startSteps);
                    editor.apply();
                    fitnessService.updateStepCount();
                    //fitnessService.setFinalStepCount();
                    wait(100000);
                } catch (Exception e) {
                }

                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                long time;

                // Need to see if mock time was used, if so need to use mock time
                if (mockTotalTime == 0) {

                    if (!startPressed) {
                        time = 0;
                    } else {
                        time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    }

                    editor.putLong("time", time);
                    editor.apply();
                } else {
                    long mockWalkTime = mockEndTime - mockStartTime;

                    String startTime = String.valueOf(mockStartTime);
                    String endTime = String.valueOf(mockEndTime);

                    startTime = startTime.substring(0, 2);
                    endTime = endTime.substring(0, 2);

                    int startTimeInt = Integer.parseInt(startTime);
                    int endTimeInt = Integer.parseInt(endTime);

                    int milTimeConversion = (endTimeInt - startTimeInt) * 40;


                    // at this point we have it in minutes
                    mockWalkTime -= milTimeConversion;
                    mockWalkTime *= 60000;
                    time = mockWalkTime;
                    editor.putLong("time", mockWalkTime);
                    editor.apply();
                }

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
