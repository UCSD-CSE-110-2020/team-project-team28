package com.example.wwr;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DisplayDailyStepsTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button button = activity.findViewById(R.id.startWWRButton);
            button.performClick();
        });
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        intent.putExtra("GOOGLE_FIT", TEST_SERVICE);
    }

    @Test
    public void testDailyStepCount() {
        /*
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView dailySteps = activity.findViewById(R.id.daily_steps_num);
            assertEquals(dailySteps.getText().toString(), "0");
        });

         */
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity stepCountActivity;

        public TestFitnessService(MainActivity stepCountActivity) {
            this.stepCountActivity = stepCountActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            //stepCountActivity.setStepCount(1000);
        }
    }
}