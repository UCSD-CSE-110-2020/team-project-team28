package com.example.wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class GoogleSignInUnitTest {

    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
    }

    @Test
    public void testGoogleSignIn() {
        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            //Button loginButton = activity.findViewById(R.id.signInButton);
            //assertNotNull(loginButton);

            Button logoutButton = activity.findViewById(R.id.signOutButton);
            assertNotNull(logoutButton);

            Button startButton = activity.findViewById(R.id.startWWRButton);
            assertNotNull(startButton);

            //startButton.performClick();
        });
    }

}
