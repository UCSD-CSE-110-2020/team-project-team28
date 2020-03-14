package com.example.wwr;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.wwr.chat.ChatMessage;
import com.example.wwr.chat.ChatService;
import com.example.wwr.chat.ChatServiceFactory;
import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Scenario7Test {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class, false, false);

    Intent intent;

    SharedPreferences.Editor teditor;

    @Before
    public void start(){



        //ShadowLog.stream = System.out;
        intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        intent.putExtra(MainActivity.CHAT_MESSAGE_SERVICE_EXTRA, "TEST_CHAT_SERVICE");

        ChatServiceFactory chatServiceFactory = MyApplication.getChatServiceFactory();
        chatServiceFactory.put("TEST_CHAT_SERVICE", chatId -> {
            // assertThat(chatId).isEqualTo("chat1");
            return new TestChatService();
        });

        mActivityTestRule.launchActivity(intent);

        Activity activity = mActivityTestRule.getActivity();
        SharedPreferences prefs = activity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.apply();

        SharedPreferences tprefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        teditor = prefs.edit();
        teditor.clear();
        teditor.apply();


    }


    @After
    public void end(){
        teditor.clear();
        teditor.apply();
    }

    @Test
    public void addTeamMemberMockTest() {

        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new Scenario7Test.TestFitnessService(stepCountActivity);
            }
        });

        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);



        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startWWRButton),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enter_name),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Sarah"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enter_email),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("sekaireb@ucsd.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.feet_input),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.inches_input),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.enter_button), withText("CONFIRM"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.team_page_button), withText("Team Page"),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.addMemberButton), withText("Add Member"),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.teamName), withText("Team Name"),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.teamName), withText("Team Name"),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.teamName), withText("Team Name"),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("Green Team"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.teamName), withText("Green Team"),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.teamEmail), withText("Insert Email"),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("ashley@ucsd.edu"));

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.teamEmail), withText("ashley@ucsd.edu"),
                        isDisplayed()));
        appCompatEditText11.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.addMember)));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.exitAddMember), withText("Exit"),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.backToMainFromTeamPage), withText("Go Back To Main Menu"),
                        isDisplayed()));
        appCompatButton7.perform(click());


        //create test hashmap
        HashMap<String, String> testHashMap = new HashMap<String, String>();
        testHashMap.put("Ashley", "ashley@ucsd.edu");

        //convert to string using gson
        Gson gson = new Gson();
        String hashMapString = gson.toJson(testHashMap);

        teditor.putString("team_members", hashMapString);
        teditor.apply();



        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.team_page_button),
                        isDisplayed()));
        appCompatButton8.perform(click());



        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.addMemberButton), withText("Add Member"),
                        isDisplayed()));
        appCompatButton14.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.teamName),
                        isDisplayed()));
        appCompatEditText15.perform(click());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.teamName),
                        isDisplayed()));
        appCompatEditText16.perform(click());

        /*ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.teamName), withText("Team Name"),
                        isDisplayed()));
        appCompatEditText18.perform(replaceText("Ashley"));*/

        /*ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.teamName), withText("Ashley"),
                        isDisplayed()));
        appCompatEditText19.perform(closeSoftKeyboard());*/

        ViewInteraction appCompatEditText110 = onView(
                allOf(withId(R.id.teamEmail), withText("Insert Email"),
                        isDisplayed()));
        appCompatEditText110.perform(replaceText("matthew@ucsd.edu"));

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.addMember)));
        appCompatButton15.perform(click());


        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.exitAddMember), withText("Exit"),
                        isDisplayed()));
        appCompatButton16.perform(click());




        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.backToMainFromTeamPage), withText("Go Back To Main Menu"),
                        isDisplayed()));
        appCompatButton9.perform(click());


        testHashMap.put("Matthew", "matthew@ucsd.edu");

        hashMapString = gson.toJson(testHashMap);

        teditor.putString("team_members", hashMapString);
        teditor.apply();


        ViewInteraction appCompatButton28 = onView(
                allOf(withId(R.id.team_page_button),
                        isDisplayed()));
        appCompatButton28.perform(click());

        ViewInteraction appCompatButton29 = onView(
                allOf(withId(R.id.backToMainFromTeamPage), withText("Go Back To Main Menu"),
                        isDisplayed()));
        appCompatButton29.perform(click());
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

    class TestChatService implements ChatService {
        @Override
        public Task<?> addMessage(Map<String, String> message) {
            return new Task<Object>() {
                @Override
                public boolean isComplete() {
                    return false;
                }

                @Override
                public boolean isSuccessful() {
                    return false;
                }

                @Override
                public boolean isCanceled() {
                    return false;
                }

                @Nullable
                @Override
                public Object getResult() {
                    return null;
                }

                @Nullable
                @Override
                public <X extends Throwable> Object getResult(@NonNull Class<X> aClass) throws X {
                    return null;
                }

                @Nullable
                @Override
                public Exception getException() {
                    return null;
                }

                @NonNull
                @Override
                public Task<Object> addOnSuccessListener(@NonNull OnSuccessListener<? super Object> onSuccessListener) {
                    return null;
                }

                @NonNull
                @Override
                public Task<Object> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Object> onSuccessListener) {
                    return null;
                }

                @NonNull
                @Override
                public Task<Object> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Object> onSuccessListener) {
                    return null;
                }

                @NonNull
                @Override
                public Task<Object> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                    return null;
                }

                @NonNull
                @Override
                public Task<Object> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                    return null;
                }

                @NonNull
                @Override
                public Task<Object> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                    return null;
                }
            };
        }

        @Override
        public void subscribeToMessages(Consumer<List<ChatMessage>> listener) {

        }


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
