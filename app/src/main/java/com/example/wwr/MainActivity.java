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
import com.google.android.gms.fitness.request.DataReadRequest;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "MainActivity";
    private FitnessService fitnessService;
    private TextView textSteps;

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

        Button button = (Button) findViewById(R.id.dailyActivityToRoutes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRouteScreen();
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        Button btnUpdateSteps = findViewById(R.id.updateSteps);
        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
            }
        });

        fitnessService.setup();

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);

        if(firstStart){
            heightActivity();
        }

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fitnessService.updateStepCount();
                int startSteps = getCurrentSteps();
                walkActivity();
                int endSteps = getCurrentSteps();
                int totalSteps = endSteps - startSteps;
                fitnessService.updateStepCount();

                TextView t = findViewById(R.id.last_steps_num);
                t.setText(String.valueOf(totalSteps));
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
        Long totalTime = sharedPreferences.getLong("time", 0);
        int totalInt = totalTime.intValue() / 1000;
        String hours = totalInt / 3600 + "hrs ";
        String minutes = (totalInt / 60) % 60 + "min ";
        String seconds = totalInt % 60 + "s";
        String time = hours + minutes + seconds;

        TextView displayTime = (TextView) findViewById(R.id.last_time_num);
        displayTime.setText(time);
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
        Intent intent = new Intent(this, WalkScreenActivity.class);
        startActivity(intent);
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