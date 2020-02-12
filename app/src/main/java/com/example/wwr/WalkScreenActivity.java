package com.example.wwr;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.wwr.MainActivity;

public class WalkScreenActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private FitnessService fitnessService;

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

        endButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                //String time_string = chronometer.getContentDescription().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("recentWalk", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putLong("time",time);
                editor.apply();

                finish();
            }
        });
    }

}
