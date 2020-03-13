package com.example.wwr;


import android.app.Activity;
import android.content.Context;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IndicateWalkedTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class);

    @Before
    public void start() {
        Activity activity = mActivityTestRule.getActivity();
        SharedPreferences prefs = activity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor =prefs.edit();
        editor.clear();
        editor.apply();
    }

    @Test
    public void indicateWalkedTest() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new TestFitnessService(stepCountActivity);
            }
        });

        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startWWRButton), withText("Start wwr!"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enter_name),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("alan"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enter_email),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("alwillu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.enter_email), withText("alwillu"),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.enter_email), withText("alwillu"),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("alwilley@"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.enter_email), withText("alwilley@"),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.enter_email), withText("alwilley@"),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.enter_email), withText("alwilley@"),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.enter_email), withText("alwilley@"),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("alwilley@ucsd.edu"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.enter_email), withText("alwilley@ucsd.edu"),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.feet_input),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.inches_input),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatButton25 = onView(
                allOf(withId(R.id.enter_button),
                        isDisplayed()));
        appCompatButton25.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.startWalk), withText("Start walk"),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.addMockSteps), withText("AddSteps"),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.end_button), withText("End walk"),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.routeNamePage)));
        appCompatEditText12.perform(scrollTo(), replaceText("la jolla"), closeSoftKeyboard());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.button_ok), withText("OK")));
        appCompatButton6.perform(scrollTo(), click());
    }

//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }

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

    }
}
