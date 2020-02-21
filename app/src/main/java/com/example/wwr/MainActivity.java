package com.example.wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "MainActivity";

    public static FitnessService fitnessService;
    final int BOOST_STEPS = 500;
    final String MOCK_STEP_COUNTER = "stepMultiplierCount";
    public static long startSteps;
    public static long finalSteps;
    public static int inches;
    DistanceCalculator walkingDistanceMiles = new WalkingDistanceMiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("previousActivity") != null &&
                getIntent().getStringExtra("previousActivity").equals("Route Detail")) {
            String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
            fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
            fitnessService.setup();
            fitnessService.updateStepCount();
            GoogleFitSingleton.setFitnessService(fitnessService);

            try {
                fitnessService.updateStepCount();
                wait(1000);
            } catch (Exception e) {
            }

            Intent intent = new Intent(getApplicationContext(), WalkScreenActivity.class);
            intent.putExtra("previousScreen", "Route Detail");
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
        fitnessService.updateStepCount();
        GoogleFitSingleton.setFitnessService(fitnessService);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            Log.d("firstLogin", "Height input because of first installation of the app.");
            heightActivity();
        }

        Button button = (Button) findViewById(R.id.dailyActivityToRoutes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRouteScreen();
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    fitnessService.updateStepCount();
                    wait(1000);
                } catch (Exception e) {
                }
                TextView steps = (TextView) findViewById(R.id.last_steps_num);
                String step = startSteps + "";
                steps.setText(step);
                SharedPreferences sharedPreferences = getSharedPreferences("total_inches", MODE_PRIVATE);
                MainActivity.inches = sharedPreferences.getInt("total_inch", 0);
                walkActivity();
            }
        });
    }

    public void heightActivity(){
        Intent intent = new Intent(this,AskHeight_Activity.class);
        startActivity(intent);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor =prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

    public void walkActivity() {
        Intent intent = new Intent(getApplicationContext(), WalkScreenActivity.class);
        intent.putExtra("previousClass", "MainActivity");
        startActivity(intent);
    }

    public void switchToRouteScreen() {
        Intent intent = new Intent(this, RouteScreen.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        try {
            fitnessService.updateStepCount();
            wait(1000);
        } catch (Exception e) {
        }

        // Update the time.
        SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
        Long totalTime = sharedPreferences.getLong("time", 0);
        int totalInt = totalTime.intValue() / 1000;
        String hours = totalInt / 3600 + "hrs ";
        String minutes = (totalInt / 60) % 60 + "min ";
        String seconds = totalInt % 60 + "s";
        String time = hours + minutes + seconds;

        TextView displayTime = (TextView) findViewById(R.id.last_time_num);
        displayTime.setText(time);

        SharedPreferences shared = getSharedPreferences("prefs", MODE_PRIVATE);
        TextView lastDistance = findViewById(R.id.last_distance_num);
        String mi = shared.getString("last intentional steps", "0");
        lastDistance.setText(mi + " miles");

        Log.d("updateIntentionalWalk", "Last intentional walk details have been updated.");
    }

    public void setStepCount(long stepCount) {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int stepMultiplier = prefs.getInt(MOCK_STEP_COUNTER, 0);
        long totalSteps = stepCount + (stepMultiplier * BOOST_STEPS);
        TextView t = findViewById(R.id.daily_steps_num);
        t.setText(String.valueOf(totalSteps));
        this.startSteps = totalSteps;
        TextView distance = findViewById(R.id.daily_distance_num);
        double miles = walkingDistanceMiles.getDistance(this.startSteps);
        distance.setText(String.format("%.2f", miles) + " miles");
    }

    public void setLastStepCount(long stepCount) {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        long previousSteps = MainActivity.startSteps - prefs.getLong("totalSteps", 0);
        TextView displaySteps = (TextView) findViewById(R.id.last_steps_num);
        displaySteps.setText(String.valueOf(previousSteps));
    }

    public void setFinalStepCount(long stepCount) {
        this.finalSteps = stepCount;
        TextView t = findViewById(R.id.daily_steps_num);
        t.setText(String.valueOf(stepCount));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateSteps() {
        fitnessService.updateStepCount();
    }

    public void setCount(long stepCount) {
        TextView t = findViewById(R.id.daily_steps_num);
        t.setText(String.valueOf(stepCount));
    }
}
