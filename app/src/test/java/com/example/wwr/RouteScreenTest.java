package com.example.wwr;

import android.content.Intent;
import android.widget.Button;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RouteScreenTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteScreen.class);
    }

    @Test
    public void testRouteScreen(){
        ActivityScenario<RouteScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button addButton = activity.findViewById(R.id.addRouteButton);
            assertNotNull(addButton);

            assertEquals(RouteScreen.routeList.size(), 0);
            assertEquals(RouteScreen.currentPosition, 0);
            addButton.performClick();

            RouteScreen.addToRouteList("Home", "California", 100,
                    10, 99, "flat", "loop",
                    "street", "even", "hard", "Wow", false);
            assertEquals(RouteScreen.routeList.size(), 1);
            assertEquals(RouteScreen.routeList.get(RouteScreen.currentPosition).getName(), "Home");

            RouteScreen.addToRouteList("CSE 110", "California", 100,
                    10, 99, "flat", "loop",
                    "street", "even", "hard", "Wow", false);
            assertEquals(RouteScreen.routeList.size(), 2);
            assertEquals(RouteScreen.routeList.get(RouteScreen.routeList.size() - 1).getName(), "CSE 110");
        });
    }
}
