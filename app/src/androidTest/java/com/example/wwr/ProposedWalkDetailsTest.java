package com.example.wwr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProposedWalkDetailsTest {
    @Rule
    public ActivityTestRule<ProposedWalkDetails> mActivityTestRule = new ActivityTestRule<>(ProposedWalkDetails.class);

    @Before
    public void start(){
        Activity activity = mActivityTestRule.getActivity();
        SharedPreferences prefs = activity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    @Test
    public void acceptOrDeclineWalk() {
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.decline_bad_route),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.accept),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.decline_bad_time),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.proposed_walk_details),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.proposed_walk_exit_button),
                        isDisplayed()));
        appCompatButton3.perform(click());
    }
}
