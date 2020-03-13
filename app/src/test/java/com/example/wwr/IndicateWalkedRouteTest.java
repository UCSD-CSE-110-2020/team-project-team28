package com.example.wwr;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wwr.Firebase.CollectionService;
import com.example.wwr.chat.ChatServiceFactory;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class IndicateWalkedRouteTest {

    @Test
    public void testManuallyAddedWalk() {
        Route blacks_beach = new Route("bigAl", "alwilley@ucsd.edu",
                "Blacks Beach", "Blacks Beach head trail", 10000,
                5.5, 10,"flat", "hilly",
                "trail", "even", "hard", "watch out for corona",
                true, 0, true);

        CollectionService firebaseDB = new CollectionService(blacks_beach);
        assertEquals(blacks_beach.hasWalked(), !firebaseDB.getInstance());
        blacks_beach.setWalked();
        assertEquals(firebaseDB.getInstance(), true);
        assertEquals(blacks_beach.hasWalked(), true);
        assertEquals(blacks_beach.getName(), "Blacks Beach");
        assertEquals(blacks_beach.getSteps(), 10000);
        assertEquals(blacks_beach.getTotalSeconds(), 10);
        assertEquals(blacks_beach.getTotalMiles(), 5.5);
        assertEquals(blacks_beach.getNote(), "watch out for corona");

        blacks_beach.updateSeconds(5);
        blacks_beach.updateMiles(500.1);
        blacks_beach.updateSteps(1000);

        assertEquals(blacks_beach.getTotalSeconds(), 5);
        assertEquals(blacks_beach.getTotalMiles(), 500.1);
        assertEquals(blacks_beach.getSteps(), 1000);
    }



    @Test
    public void testWalkIndicateBeforeAndAfter() {
        Route LaJollaShores = new Route("bigAl", "alwilley@ucsd.edu",
                "La Jolla Shores", "La Jolla", 100,
                5.5, 10,"flat", "hilly",
                "trail", "even", "hard", "Gotta go home",
                true, 0, true);

        CollectionService firebaseDB = new CollectionService(LaJollaShores);
        assertEquals(LaJollaShores.hasWalked(), !firebaseDB.getInstance());
        LaJollaShores.setWalked();
        assertEquals(firebaseDB.getInstance(), true);
        assertEquals(LaJollaShores.hasWalked(), true);
        assertEquals(LaJollaShores.getName(), "La Jolla Shores");
        assertEquals(LaJollaShores.getSteps(), 100);
        assertEquals(LaJollaShores.getTotalSeconds(), 10);
        assertEquals(LaJollaShores.getTotalMiles(), 5.5);
        assertEquals(LaJollaShores.getNote(), "Gotta go home");

        LaJollaShores.updateSeconds(5);
        LaJollaShores.updateMiles(500.1);
        LaJollaShores.updateSteps(1000);

        assertEquals(LaJollaShores.getTotalSeconds(), 5);
        assertEquals(LaJollaShores.getTotalMiles(), 500.1);
        assertEquals(LaJollaShores.getSteps(), 1000);
    }
}