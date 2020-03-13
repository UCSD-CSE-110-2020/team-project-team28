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
        /*
        intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        // main activity
        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button button = activity.findViewById(R.id.startWWRButton);
            button.performClick();
        });
        Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
         */
    }
}
