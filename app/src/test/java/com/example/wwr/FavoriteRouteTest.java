package com.example.wwr;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class FavoriteRouteTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteScreen.class);
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void addRouteToFavorite() {
        ActivityScenario<RouteScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button addButton = activity.findViewById(R.id.addRouteButton);
            assertNotNull(addButton);
            assertEquals(RouteScreen.routeList.size(), 0);
            assertEquals(RouteScreen.currentPosition, 0);
            addButton.performClick();

            RouteScreen.addToRouteList("Wonsuk", "w1jang@ucsd.edu", "Home", "California", 100,
                    10, 99, "flat", "loop",
                    "street", "even", "hard", "Wow", false);
            assertEquals(RouteScreen.routeList.size(), 1);
            assertEquals(RouteScreen.routeList.get(RouteScreen.currentPosition).getName(), "Home");

            assertEquals(RouteScreen.routeList.size(), 1);
            assertEquals(RouteScreen.routeList.get(RouteScreen.routeList.size() - 1).getUserName(), "Wonsuk");
            assertEquals(RouteScreen.routeList.get(RouteScreen.routeList.size() - 1).getUserEmail(), "w1jang@ucsd.edu");

            Button backToMainScreenButton = activity.findViewById(R.id.backToMainMenuButton);
            assertNotNull(backToMainScreenButton);

            CheckBox box = activity.findViewById(R.id.favorite_btn);
            assertEquals(box, null);
            assertEquals(RouteScreen.routeList.get(0).getFavorite(), false);
        });
    }
}
