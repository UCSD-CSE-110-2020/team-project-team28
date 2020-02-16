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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RouteDetailTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetail.class);
    }

    @Test
    public void testRouteDetail(){
        ActivityScenario<RouteDetail> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            Button startRoute = activity.findViewById(R.id.route_info_start_button);
            TextView routeName = activity.findViewById(R.id.route_detail_title);
            assertNotNull(routeName);
            TextView routeStartLocation = activity.findViewById(R.id.route_detail_start_location);
            assertNotNull(routeStartLocation);
            TextView routeTime = activity.findViewById(R.id.route_detail_time_taken);
            assertNotNull(routeTime);
            TextView routeSteps = activity.findViewById(R.id.route_detail_steps);
            assertNotNull(routeSteps);
            TextView routeDistance = activity.findViewById(R.id.route_detail_distance);
            assertNotNull(routeDistance);
            TextView routeFeature = activity.findViewById(R.id.route_detail_features);
            assertNotNull(routeFeature);
            TextView routeNote = activity.findViewById(R.id.route_detail_note);
            assertNotNull(routeNote);
            startRoute.performClick();
        });
    }

}
