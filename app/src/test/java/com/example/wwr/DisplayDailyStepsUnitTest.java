package com.example.wwr;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wwr.MainActivity;
import com.example.wwr.R;
import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class DisplayDailyStepsUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(),  MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testUpdateStepsButton() {
        nextStepCount = 0;

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textSteps = activity.findViewById(R.id.daily_steps_num);
            activity.updateSteps();
            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
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
            //stepCountActivity.setStepCount(nextStepCount);
        }
    }
}