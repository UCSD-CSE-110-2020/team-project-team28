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
    private String mockStartTime;
    private String mockEndTime;
    private int mockTotalTime = 0;
    private boolean startPressed = false;
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";
    private IUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new UserInfo(this);

        setContentView(R.layout.activity_walk_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Chronometer chronometer = findViewById(R.id.chronometer);
        Intent previous = getIntent();
        TextView routeName = findViewById(R.id.route_name);

        // Determine if the route already has a name.
        String name = previous.getStringExtra("route name");
        if (name != null) {
            routeName.setText(name);
            Log.d("routeNameSet", "Route name has been set");
        }

        // Starts the walk.
        Button startWalk = (Button) findViewById(R.id.startWalk);
        startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPressed = true;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                user.saveCurrentSteps();
                Log.d("chronometerStart", "Chronometer has been started");
            }
        });

        // Button to allow the user to set a mock time.
        Button newStartTime = (Button) findViewById(R.id.submitStartTime);
        newStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText x = (EditText) findViewById(R.id.startTimeEdit);
                String time = x.getText().toString();
                mockStartTime = time;
                chronometer.stop();
                Log.d("chronometerStop", "Chronometer has been stopped.");
            }
        });

        // Button to allow the user to end the mock time.
        Button newEndTime = (Button) findViewById(R.id.submitEndTime);
        newEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText x = (EditText) findViewById(R.id.endTimeEdit);
                String time = x.getText().toString();
                mockEndTime = time;
                mockTotalTime = Integer.parseInt(mockEndTime) - Integer.parseInt(mockStartTime);
                Log.d("mockTimeCalculated", "Mock time has been calculated.");
            }
        });

        // Mock button to add 500 steps on each press.
        Button addSteps = (Button) findViewById(R.id.addMockSteps);
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.increaseDailyMockSteps();
            }
        });

        // Update the steps so we have a starting point for the walk
        FitnessService fitnessService = GoogleFitSingleton.getFitnessService();
        fitnessService.updateStepCount();

        // Button to end the walk.
        Button endButton = (Button) findViewById(R.id.end_button);
        endButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    fitnessService.updateStepCount();
                    wait(100000);
                } catch (Exception e) {
                }
                long time;
                // Need to see if mock time was used, if so need to use mock time.
                if (mockTotalTime == 0) {
                    if (!startPressed) {
                        time = 0;
                    } else {
                        time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    }
                    user.setLastIntentionalWalkTime(time);

                } else {
                  int beginTotal = Integer.parseInt(mockStartTime);
                  int endTotal = Integer.parseInt(mockEndTime);

                  int beginMinutes = beginTotal % 100;
                  int endMinutes = endTotal % 100;

                  int beginHours = (beginTotal - beginMinutes) / 100;
                  int endHours = (endTotal - endMinutes) / 100;

                  int hourDifference = (endHours - beginHours) * 3600;
                  int minDifference = (endMinutes - beginMinutes) * 60;

                  int totalSeconds =  hourDifference + minDifference;
                  totalSeconds = (totalSeconds < 0) ? (totalSeconds + (24*3600)) : totalSeconds;
                  time = (long)totalSeconds * 1000;

                  user.setLastIntentionalWalkTime(time);
                }

                String previousString = "";
                previousString = previous.getStringExtra("previousClass");
                Intent intent = new Intent(getApplicationContext(), RouteScreen.class);

                // Handles the case where you start a brand new walk.
                if (previousString != null && previousString.equals("MainActivity")) {
                    intent.putExtra("goToDetail", true);
                    intent.putExtra("newTime", time);
                }

                // Handles the case where you started an existing walk.
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
