package com.example.wwr;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Scenario2Test {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void start() {

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        Activity activity = mActivityTestRule.getActivity();
        SharedPreferences prefs = activity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor =prefs.edit();
        //editor.clear();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }
    @Test
    public void scenario2Test() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new Scenario2Test.TestFitnessService(stepCountActivity);
            }
        });
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);
        /*ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.feet_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.inches_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.enter_button), withText("CONFIRM"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        1),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

         */

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.startTimeEdit), withText("0000"),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("1000"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.startTimeEdit), withText("1000"),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.submitStartTime), withText("Submit"),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.startWalk), withText("Start walk"),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.addMockSteps), withText("AddSteps"),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.addMockSteps), withText("AddSteps"),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.endTimeEdit), withText("0000"),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("1200"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.endTimeEdit), withText("1200"),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.submitEndTime), withText("Submit"),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.end_button), withText("End walk"),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.routeNamePage),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("Trail"), closeSoftKeyboard());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.button_ok), withText("OK"),
                        isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.route_name), withText("Trail"),
                        isDisplayed()));
        textView.check(matches(withText("Trail")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.totalTime), withText("Total Time: 7200s"),
                        isDisplayed()));
        textView2.check(matches(withText("Total Time: 7200s")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.totalSteps), withText("Total Steps: 1000 steps"),
                        isDisplayed()));
        textView3.check(matches(withText("Total Steps: 1000 steps")));
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
    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity stepCountActivity;

        public TestFitnessService(MainActivity stepCountActivity) {
            this.stepCountActivity = stepCountActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            //stepCountActivity.setStepCount(1337);
        }

        @Override
        public void setFinalStepCount(){}

    }
}