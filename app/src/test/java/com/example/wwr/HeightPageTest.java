package com.example.wwr;

import static junit.framework.TestCase.assertEquals;

public class HeightPageTest {
    public void tesHeightPage() {
        Route myRoute = new Route("La Jolla", "Fox", 3,
                60, 100, "Hello", true, 0);

        assertEquals(myRoute.getName(), "La Jolla");
    }
}