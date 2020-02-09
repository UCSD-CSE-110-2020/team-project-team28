package com.example.wwr;

import android.widget.EditText;

import org.junit.Test;
import static org.junit.Assert.*;

public class HeightDistanceTest {

    @Test
    public void testHeight() {
        Route myRoute = new Route("La Jolla","Fox",3,
                60,100, "Hello", true, 0);
        assertEquals(myRoute.getName(), "La Jolla");
    }
}
