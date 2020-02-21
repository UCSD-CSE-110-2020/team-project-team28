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
public class RoutesDetailTest {

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
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

    @Test
    public void routesDetailTest() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new RoutesDetailTest.TestFitnessService(stepCountActivity);
            }
        });

        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);

        ViewInteraction appCompatButton100 = onView(
                allOf(withId(R.id.startWWRButton),
                        isDisplayed()));
        appCompatButton100.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.dailyActivityToRoutes), withText("ROUTES"),
                        childAtPosition(
                                allOf(withId(R.id.include),
                                        childAtPosition(
                                                withId(R.id.coordinatorLayout),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.addRouteButton), withText("Add Route"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.routeNamePage),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Route"), closeSoftKeyboard());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.radio_flat), withText("Flat"),
                        childAtPosition(
                                allOf(withId(R.id.groupFlat),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                2),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.radio_trail), withText("Trail"),
                        childAtPosition(
                                allOf(withId(R.id.groupStreet),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                5)),
                                2),
                        isDisplayed()));
        appCompatRadioButton2.perform(click());

        ViewInteraction appCompatRadioButton3 = onView(
                allOf(withId(R.id.radio_moderate), withText("Moderate"),
                        childAtPosition(
                                allOf(withId(R.id.groupDifficulty),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                7)),
                                2),
                        isDisplayed()));
        appCompatRadioButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.button_ok), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction cardView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.routeScreen),
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0)),
                        0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.route_detail_features), withText("Features: \nFlat\nLoop\nTrail\nEven Surface\nModerate"),
                        childAtPosition(
                                allOf(withId(R.id.route_information_page),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                0)),
                                5),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));
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
