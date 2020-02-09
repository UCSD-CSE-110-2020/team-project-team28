package com.example.wwr;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntentionalWalkTest {
    public void testIntentionalWalk() {
        Route myRoute = new Route("La Jolla", "Fox", 3,
                60, 100, "Hello", true, 0);
        assertEquals(myRoute.getName(), "La Jolla");
    }
}
