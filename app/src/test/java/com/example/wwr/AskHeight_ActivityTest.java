package com.example.wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wwr.fitness.FitnessServiceFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AskHeight_ActivityTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), AskHeight_Activity.class);
    }

    @Test
    public void testHeightSaved(){
        ActivityScenario<AskHeight_Activity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText feet = activity.findViewById(R.id.feet_input);
            EditText inches = activity.findViewById(R.id.inches_input);
            Button enter = activity.findViewById(R.id.enter_button);

            feet.setText("5");
            inches.setText("10");

            enter.performClick();

            SharedPreferences sharedPreferences = activity.getSharedPreferences("inches",MODE_PRIVATE);
            String inches_str = sharedPreferences.getString("inches_string","");

            assertEquals("10", inches_str);
        });
    }

}

