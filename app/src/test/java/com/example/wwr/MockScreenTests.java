package com.example.wwr;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class MockScreenTests {

    private Intent intent;

    @Test
    public void testAddMockStepsButton() {
        /*intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);

        // main activity
        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button button = activity.findViewById(R.id.startWWRButton);
            button.performClick();
        });
        Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        // mock screen activity

        ActivityScenario<MainActivity> scenario1 = ActivityScenario.launch(intent2);
        scenario1.onActivity(activity -> {
            TextView t = activity.findViewById(R.id.daily_steps_num);
            activity.setStepCount(0);
            Button mockPage = activity.findViewById(R.id.start_button);
            mockPage.performClick();
        });

        Intent intent1 = new Intent(ApplicationProvider.getApplicationContext(), WalkScreenActivity.class);

        // mock screen activity
        ActivityScenario<WalkScreenActivity> scenario3 = ActivityScenario.launch(intent1);
        scenario3.onActivity(activity -> {

            Button addSteps = activity.findViewById(R.id.addMockSteps);
            addSteps.performClick();
            addSteps.performClick();
            addSteps.performClick();
            addSteps.performClick();
        });

        // back to main activity
        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(intent);
        scenario2.onActivity(activity -> {
            TextView t = activity.findViewById(R.id.daily_steps_num);
            activity.setStepCount(100);
            assertThat((MainActivity.startSteps)).isEqualTo(2100);
        });
         */
    }
}
