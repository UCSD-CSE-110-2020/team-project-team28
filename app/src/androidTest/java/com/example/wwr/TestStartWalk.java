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
public class TestStartWalk {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testStartWalk() {
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.enter_button), withText("Enter"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
//                                        1),
//                                2),
//                        isDisplayed()));
//        appCompatButton.perform(click());

        ViewInteraction appCompatButton = onView(withId(R.id.enter_button)).check(matches(isDisplayed()));

        appCompatButton.perform(click());

//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(R.id.start_button), withText("START"),
//                        childAtPosition(
//                                allOf(withId(R.id.include),
//                                        childAtPosition(
//                                                withId(R.id.coordinatorLayout),
//                                                1)),
//                                1),
//                        isDisplayed()));
//        appCompatButton2.perform(click());
//
//        ViewInteraction appCompatButton3 = onView(
//                allOf(withId(R.id.end_button), withText("End"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
//                                        1),
//                                0),
//                        isDisplayed()));
//        appCompatButton3.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
