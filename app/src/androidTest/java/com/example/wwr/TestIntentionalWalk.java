package com.example.wwr;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestIntentionalWalk {

    @Rule
    public ActivityTestRule<WalkScreenActivity> mActivityTestRule = new ActivityTestRule<>(WalkScreenActivity.class);

    @Test
    public void testIntentionalWalk() {
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.end_button), withText("End"),
                        isDisplayed()));
        appCompatButton3.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.steps), withText("Steps"),
                        isDisplayed()));
        textView.check(matches(withText("Steps")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.distance), withText("Distance"),
                        isDisplayed()));
        textView2.check(matches(withText("Distance")));
    }

}
