package com.example.wwr;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class StartWalkFromRouteScreenTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetail.class);
    }

    @Test
    public void testWalkScreen(){
        /*
        ActivityScenario<RouteDetail> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startButton = activity.findViewById(R.id.route_info_start_button);
            startButton.performClick();
        });

        Intent intent1 = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        ActivityScenario<MainActivity> scenario1 = ActivityScenario.launch(intent1);
        scenario1.onActivity(activity -> {
            activity.setStepCount(0);
            Button mockPage = activity.findViewById(R.id.start_button);
            mockPage.performClick();
        });

        Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), WalkScreenActivity.class);
        ActivityScenario<WalkScreenActivity> scenario2 = ActivityScenario.launch(intent2);
        scenario2.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.chronometer));
            TextView routeName = activity.findViewById(R.id.route_name);
            assertEquals(routeName.getText(), "Route Name");
            Button endButton = activity.findViewById(R.id.end_button);
            endButton.performClick();
        });

        Intent intent3 = new Intent(ApplicationProvider.getApplicationContext(), RouteScreen.class);
        ActivityScenario<RouteScreen> scenario3 = ActivityScenario.launch(intent3);
        scenario3.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.routeScreen));
        });

         */
    }
}