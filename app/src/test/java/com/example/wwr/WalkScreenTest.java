package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WalkScreenTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), WalkScreenActivity.class);
    }

    @Test
    public void testWalkScreen(){
        ActivityScenario<WalkScreenActivity> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.chronometer));
            TextView routeName = activity.findViewById(R.id.route_name);
            assertEquals(routeName.getText(), "Route Name");
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Button endButton = activity.findViewById(R.id.end_button);
            endButton.performClick();

            SharedPreferences sharedPreferences = activity.getSharedPreferences("recentWalk", MODE_PRIVATE);
            long time = sharedPreferences.getLong("time", -1);
            assertEquals(time, 0);
        });
    }

}

