package com.example.wwr;

import android.content.Intent;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ProposedWalkDetailsTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ProposedWalkDetails.class);
        intent.putExtra("TEST", "TEST");
    }

    @Test
    public void testProposedWalkDetails() {
        ActivityScenario<ProposedWalkDetails> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView routeDetails = activity.findViewById(R.id.proposed_walk_details);
            assertNotNull(routeDetails);

            TextView memberStatus = activity.findViewById(R.id.proposed_walk_team_details);
            assertNotNull(memberStatus);
            assertEquals(memberStatus.getText(), "No team members have updated their status");

            RadioButton declineBadRoute = activity.findViewById(R.id.decline_bad_route);
            assertNotNull(declineBadRoute);
            declineBadRoute.performClick();

            RadioButton declineBadTime = activity.findViewById(R.id.decline_bad_time);
            assertNotNull(declineBadTime);
            declineBadTime.performClick();

            RadioButton accept = activity.findViewById(R.id.accept);
            assertNotNull(accept);
            accept.performClick();

            Button exit = activity.findViewById(R.id.proposed_walk_exit_button);
            assertNotNull(exit);
        });
    }
}
