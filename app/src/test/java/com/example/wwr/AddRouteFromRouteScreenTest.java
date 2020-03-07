package com.example.wwr;
import android.content.Intent;
import android.widget.Button;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AddRouteFromRouteScreenTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteScreen.class);
    }

    @Test
    public void testAddRouteFromRouteScreen(){
        ActivityScenario<RouteScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button addButton = activity.findViewById(R.id.addRouteButton);
            assertNotNull(addButton);

            assertEquals(RouteScreen.routeList.size(), 0);
            assertEquals(RouteScreen.currentPosition, 0);
            RouteScreen.addToRouteList("Home", "Tim", "All star", "California", 100,
                    100, 9, "flat", "loop",
                    "street", "even", "hard", "Wow", false);
            addButton.performClick();
            assertEquals(RouteScreen.routeList.size(), 1);
            assertEquals(RouteScreen.routeList.get(RouteScreen.currentPosition).getName(), "All star");
        });
    }
}