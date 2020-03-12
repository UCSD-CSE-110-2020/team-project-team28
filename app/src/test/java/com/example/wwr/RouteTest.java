package com.example.wwr;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RouteTest {
    @Test
    public void testRoute() {
        Route home = new Route("Tim", "A", "Home", "CA", 100, 5.5, 10,
                "flat", "hilly", "trail", "even", "hard",
                "Gotta go home", true, 0, false);
        assertEquals(home.getName(), "Home");
        assertEquals(home.getSteps(), 100);
        assertEquals(home.getTotalSeconds(), 10);
        assertEquals(home.getTotalMiles(), 5.5);
        assertEquals(home.getNote(), "Gotta go home");

        home.updateSeconds(5);
        home.updateMiles(500.1);
        home.updateSteps(1000);

        assertEquals(home.getTotalSeconds(), 5);
        assertEquals(home.getTotalMiles(), 500.1);
        assertEquals(home.getSteps(), 1000);
    }

}
