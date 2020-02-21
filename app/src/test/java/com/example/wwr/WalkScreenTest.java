package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
    }

    @Test
    public void testWalkScreen(){
        /*
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button mockPage = activity.findViewById(R.id.start_button);
            mockPage.performClick();
        });

        Intent intent1 = new Intent(ApplicationProvider.getApplicationContext(), WalkScreenActivity.class);
        ActivityScenario<WalkScreenActivity> scenario1 = ActivityScenario.launch(intent1);
        scenario1.onActivity(activity -> {
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

         */
    }
}

