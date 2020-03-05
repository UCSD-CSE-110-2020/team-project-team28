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

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AddTeammateRouteUnitTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), TeamRouteScreen.class);
    }

    @Test
    public void addTeammateRoute(){
        ActivityScenario<TeamRouteScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button button = activity.findViewById(R.id.backToMainMenuButtonFromTeam);
            assertNotNull(button);
            assertEquals(activity.routeList.size(), 0);
            TeamRouteScreen.routeList.add(
                    new Route("Tim Jang", "w1jang@ucsd.edu", 100, 5.5, 10,
                            "flat", "hilly", "trail", "even", "hard",
                            "Gotta go home", true, 0));
            assertEquals(activity.routeList.size(), 1);
            assertEquals(activity.routeList.get(0).getName(), "Tim Jang");
            button.performClick();
        });

    }
}
