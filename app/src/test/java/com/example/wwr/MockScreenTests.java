package com.example.wwr;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import static com.google.common.truth.Truth.assertThat;


@RunWith(AndroidJUnit4.class)
public class MockScreenTests {

    private Intent intent;

    @Test
    public void testAddMockStepsButton() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        // main activity
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {

            TextView t = activity.findViewById(R.id.daily_steps_num);
            activity.setStepCount(0);
            Button mockPage = activity.findViewById(R.id.goToMockPage);
            mockPage.performClick();
        });

        Intent intent1 = new Intent(ApplicationProvider.getApplicationContext(), MockScreenActivity.class);

        // mock screen activity
        ActivityScenario<MockScreenActivity> scenario1 = ActivityScenario.launch(intent1);
        scenario1.onActivity(activity -> {

            Button addSteps = activity.findViewById(R.id.addMockSteps);
            addSteps.performClick();
            addSteps.performClick();
            addSteps.performClick();
            addSteps.performClick();
        });

        // back to main activity
        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {

            TextView t = activity.findViewById(R.id.daily_steps_num);
            activity.setStepCount(100);
            assertThat((MainActivity.startSteps)).isEqualTo(2100);
        });
    }
}
