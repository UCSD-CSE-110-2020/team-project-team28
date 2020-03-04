package com.example.wwr;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AddTeamMemberTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), TeamPageScreen.class);
    }

    @Test
    public void addTeamMember() {
        ActivityScenario<TeamPageScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button addMemberButton = activity.findViewById(R.id.addMemberButton);
            assertNotNull(addMemberButton);

            Button backToMainScreenButton = activity.findViewById(R.id.backToMainFromTeamPage);
            assertNotNull(backToMainScreenButton);

            assertEquals(TeamPageScreen.routeList.size(), 0);
            TeamPageScreen.routeList.add(
                    new Route("Tim Jang", "w1jang@ucsd.edu", 100, 5.5, 10,
                    "flat", "hilly", "trail", "even", "hard",
                    "Gotta go home", true, 0));
            addMemberButton.performClick();

            assertEquals(TeamPageScreen.routeList.size(), 1);
            assertEquals(TeamPageScreen.routeList.get(TeamPageScreen.routeList.size() - 1).getName(), "Tim Jang");
            assertEquals(TeamPageScreen.routeList.get(TeamPageScreen.routeList.size() - 1).getStartLocation(), "w1jang@ucsd.edu");

            backToMainScreenButton.performClick();
        });
    }
}
