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
import static androidx.test.espresso.action.ViewActions.longClick;
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
public class AddTeamMemberTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<TeamPageScreen> mActivityTestRule = new ActivityTestRule<>(TeamPageScreen.class);

    @Before
    public void start() {
        Activity activity = mActivityTestRule.getActivity();
        SharedPreferences prefs = activity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

    @Test
    public void addTeamMemberTest() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new TestFitnessService(stepCountActivity);
            }
        });

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.addMemberButton), withText("Add Member"),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.teamEmail), withText("Insert Email"),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("Karen"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.teamEmail), withText("Karen"),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.addMember), withText("Add"),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.teamEmail), withText("Karen"),
                        isDisplayed()));
        editText.check(matches(withText("Karen")));
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
