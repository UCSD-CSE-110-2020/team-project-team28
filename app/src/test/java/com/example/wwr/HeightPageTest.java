package com.example.wwr;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertEquals;

public class HeightPageTest {

    SharedPreferences spfs;


    public void tesHeightPage() {


        Route myRoute = new Route("La Jolla", "Fox", 3,
                60, 100, "Hello", true, 0);

        assertEquals(myRoute.getName(), "La Jolla");
    }
}