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
public class ProposeWalkTest {
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
    public void proposeWalkTest() {
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
        appCompatEditText.perform(replaceText("wons"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enter_email),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("janf"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.feet_input),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.inches_input),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("4"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.enter_button), withText("CONFIRM"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.dailyActivityToRoutes), withText("MY ROUTES"),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.addRouteButton), withText("Add Route"),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.routeNamePage),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatEditText5.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.startLocationName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        appCompatEditText6.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.button_ok), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                9)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction cardView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.routeScreen),
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0)),
                        0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.route_info_propose_button), withText("Propose Walk"),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.propose_walk_time), withText("Propose Time"),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("12:00"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.propose_walk_time), withText("12:00"),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.propose_walk_date), withText("Propose Date"),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("1/1/19"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.propose_walk_date), withText("1/1/19"),
                        isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.propose_walk_propose_button), withText("Propose"),
                        isDisplayed()));
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

    }
}
