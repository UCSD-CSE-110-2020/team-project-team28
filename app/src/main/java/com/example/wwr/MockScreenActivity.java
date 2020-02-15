package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class MockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_screen);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenuButton);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
    }

}
