package com.example.wwr;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TeamRouteScreenTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), TeamRouteScreen.class);
        intent.putExtra("TEST", "TEST");
    }

    @Test
    public void testTeamRouteScreen(){

            /*
        ActivityScenario<TeamRouteScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            assertNotNull(TeamRouteScreen.routeList);
            ArrayList<Route> route = TeamRouteScreen.routeList;
            assertEquals(route.size(), 0);
            route.add(new Route("Home", "Tim", "CSE", "CA", 10, 10.0,
                            10, "flat", "loop", "street", "surface",
                            "hard", "", true, 0, false));
            assertEquals(route.size(), 1);
            assertEquals(route.get(route.size() - 1).getName(), "CSE");

        });
             */
    }
}
