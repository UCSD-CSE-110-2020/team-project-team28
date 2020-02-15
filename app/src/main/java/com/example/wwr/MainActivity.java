package com.example.wwr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "MainActivity";
    public static FitnessService fitnessService;
    final int BOOST_STEPS = 500;
    final String MOCK_STEP_COUNTER= "stepMultiplierCount";
    public static long startSteps;
    public static long finalSteps;
    //context for the SharedPreferences in the walkingDistanceMiles
    public static double miles;
    public static int inches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra("previousActivity") != null &&
            getIntent().getStringExtra("previousActivity").equals("Route Detail")) {
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

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainactivity) {
                return new GoogleFitAdapter(mainactivity);
            }
        });

        Button button = (Button) findViewById(R.id.dailyActivityToRoutes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRouteScreen();
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        GoogleFitSingleton.setFitnessService(fitnessService);

        fitnessService.setup();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            Log.d("firstLogin", "Height input because of first installation of the app.");
            heightActivity();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("total_inches", MODE_PRIVATE);
        this.inches = sharedPreferences.getInt("total_inch", 0);


        startButton.setOnClickListener(new View.OnClickListener() {

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
                walkActivity();
            }
        });

        Button mockPageBtn = (Button) findViewById(R.id.goToMockPage);
        mockPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mockPageActivity();
            }
        });
    }

    public void mockPageActivity() {
        Intent intent = new Intent(this, MockScreenActivity.class);
        startActivity(intent);
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
        Log.d("updateTime", "Time has been updated.");
    }

    public void setStepCount(long stepCount) {
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        int stepMultiplier = prefs.getInt(MOCK_STEP_COUNTER, 0);
        long totalSteps = stepCount + (stepMultiplier * BOOST_STEPS);
        TextView t = findViewById(R.id.daily_steps_num);
        t.setText(String.valueOf(totalSteps));
        this.startSteps = totalSteps;
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

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
    }
}
