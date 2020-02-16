package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RoutesActivityNotesTest {

    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RoutesActivity.class);
    }


    @Test
    public void notesSaved(){

        ActivityScenario<RoutesActivity> scenario = ActivityScenario.launch(intent);
        //ActivityScenario<AskHeight_Activity> scenario = activityTestRule.getScenario();

        scenario.onActivity(activity -> {
            EditText routeName = activity.findViewById(R.id.routeNamePage);
            EditText notes = activity.findViewById(R.id.routeNotes);

            routeName.setText("");
            notes.setText("Notes for the route");



            Button okButton = activity.findViewById(R.id.button_ok);

            okButton.performClick();

            SharedPreferences sharedPreferences = activity.getSharedPreferences("shared preferences",MODE_PRIVATE);
            String notes_str = sharedPreferences.getString("notes","");

            assertEquals("Notes for the route", notes_str);

        });
    }

}