package com.example.wwr;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RouteUnitTest {



    @Test
    public void testRoute() {
        Route myRoute = new Route("La Jolla","Fox",3,
                60,100, "Hello", true, 0);
        assertEquals(myRoute.getName(), "La Jolla");
        assertEquals(myRoute.getMiles(), 60, 0.0);
    }

    @Test
    public void testUpdateSteps() {
        Route myRoute = new Route("La Jolla","Fox", 3,
                60,100, "Kyrie", false, 0);
        assertEquals(myRoute.getName(), "La Jolla");
        assertEquals(myRoute.getStartLocation(), "Fox");
        myRoute.updateMinutes(1000);
        assertEquals(myRoute.getTotalMinutes(), 1000);
        assertEquals(myRoute.getNote(), "Kyrie");
    }
}