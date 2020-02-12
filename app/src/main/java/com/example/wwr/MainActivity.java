package com.example.wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.GoogleFitAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.wwr.fitness.FitnessServiceFactory;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "MainActivity";
    private FitnessService fitnessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainactivity) {
                return new GoogleFitAdapter(mainactivity);
            }
        });

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        Button btnUpdateSteps = findViewById(R.id.updateSteps);
        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
            }
        });

        fitnessService.setup();

        Button button = (Button) findViewById(R.id.dailyActivityToRoutes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRouteScreen();
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                walkActivity();
            }
        });
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    public void setStepCount(long stepCount) {
        TextView t = findViewById(R.id.daily_steps_num);
        t.setText(String.valueOf(stepCount));
    }

    public int getCurrentSteps(){
        TextView t = findViewById(R.id.daily_steps_num);
        int currentSteps = Integer.parseInt(t.getText().toString());
        return currentSteps;
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

    public void walkActivity(){
        fitnessService.updateStepCount();
        int startSteps = getCurrentSteps();
        Intent intent = new Intent(this, WalkScreenActivity.class);
        startActivity(intent);
        fitnessService.updateStepCount();
        int endSteps = getCurrentSteps();
        int totalSteps = endSteps - startSteps;
        TextView t = findViewById(R.id.last_steps_num);
        t.setText(String.valueOf(totalSteps));

        SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
        //Long totalTime = sharedPreferences.getLong("time", 0);
        Long totalTime = sharedPreferences.getLong("time", 0);
        TextView displayTime = (TextView) findViewById(R.id.last_time_num);
        displayTime.setText(totalTime.toString());
    }

    public void switchToRouteScreen() {
        Intent intent = new Intent(this, RouteScreen.class);
        startActivity(intent);
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
}
