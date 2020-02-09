package com.example.wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "MainActivity";

    private TextView textSteps;
    private FitnessService fitnessService;
    //Intent intent;
    //from sca

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        intent = new Intent(this, MainActivity.class);
//        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
//        startActivity(intent);
//
//        //get the steps
//        TextView dailySteps = (TextView) findViewById(R.id.daily_steps_num);
//
//        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
//        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
//        fitnessService.updateStepCount();


        Button button = (Button) findViewById(R.id.dailyActivityToRoutes);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRouteScreen();
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);

        if(firstStart){
            heightActivity();
        }


        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
    }

}