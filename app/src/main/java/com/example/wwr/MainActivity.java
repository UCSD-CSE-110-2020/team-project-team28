package com.example.wwr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wwr.chat.ChatService;
import com.example.wwr.chat.FirebaseFirestoreAdapter;
import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "MainActivity";

    public static FitnessService fitnessService;
    final int BOOST_STEPS = 500;
    public static long startSteps;
    public static long finalSteps;
    public static int inches;
    private UserInfo user;
    DistanceCalculator walkingDistanceMiles = new WalkingDistanceMiles();

    //new
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";
    private static final String FIRESTORE_CHAT_SERVICE = "FIRESTORE_CHAT_SERVICE";

    String COLLECTION_KEY = "chats";
    String CHAT_ID = "chat1";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    CollectionReference walk;
    ChatService notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new UserInfo(this);

        if (getIntent().getStringExtra("previousActivity") != null &&
                getIntent().getStringExtra("previousActivity").equals("Route Detail")) {
            String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
            fitnessService = MyApplication.getFitnessServiceFactory().create(fitnessServiceKey, this);
            fitnessService.setup();
            fitnessService.updateStepCount();
            GoogleFitSingleton.setFitnessService(fitnessService);

            try {
                fitnessService.updateStepCount();
                wait(1000);
            } catch (Exception e) {
            }

            Intent intent = new Intent(getApplicationContext(), WalkScreenActivity.class);
            intent.putExtra("previousScreen", "Route Detail");
            startActivity(intent);
        }

        /*chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);*/
        //new
        /*if (getIntent().hasExtra(CHAT_MESSAGE_SERVICE_EXTRA)) {
            MyApplication.getChatServiceFactory().put(FIRESTORE_CHAT_SERVICE, (chatId ->
                    new FirebaseFirestoreAdapter(COLLECTION_KEY, CHAT_ID, MESSAGES_KEY, FROM_KEY, TEXT_KEY, TIMESTAMP_KEY)));

            String chatServiceKey = getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA);
            if (chatServiceKey == null) {
                chatServiceKey = FIRESTORE_CHAT_SERVICE;
            }
            notifications = MyApplication.getChatServiceFactory().create(chatServiceKey, CHAT_ID);
        } else {

            notifications = MyApplication
                    .getChatServiceFactory()
                    .createFirebaseFirestoreChatService(COLLECTION_KEY, CHAT_ID, MESSAGES_KEY, FROM_KEY, TEXT_KEY, TIMESTAMP_KEY);

        }
        // end

        /*walk = FirebaseFirestore.getInstance()
                .collection("notifications")
                .document("proposed")
                .collection("team_walk");*/

        // new
        if (getIntent().hasExtra(CHAT_MESSAGE_SERVICE_EXTRA)) {
            MyApplication.getChatServiceFactory().put(FIRESTORE_CHAT_SERVICE, (chatId ->
                    new FirebaseFirestoreAdapter(COLLECTION_KEY, CHAT_ID, MESSAGES_KEY, FROM_KEY, TEXT_KEY, TIMESTAMP_KEY)));

            String chatServiceKey = getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA);
            if (chatServiceKey == null) {
                chatServiceKey = FIRESTORE_CHAT_SERVICE;
            }
            notifications = MyApplication.getChatServiceFactory().create(chatServiceKey, CHAT_ID);
        } else {

            notifications = MyApplication
                    .getChatServiceFactory()
                    .createFirebaseFirestoreChatService(COLLECTION_KEY, CHAT_ID, MESSAGES_KEY, FROM_KEY, TEXT_KEY, TIMESTAMP_KEY);

        }

        subscribeToNotificationsTopic();
        //subscribeToNotificationsTopic2();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = MyApplication.getFitnessServiceFactory().create(fitnessServiceKey, this);
        fitnessService.setup();
        fitnessService.updateStepCount();
        GoogleFitSingleton.setFitnessService(fitnessService);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            Log.d("firstLogin", "Height input because of first installation of the app.");
            heightActivity();
        }

        Button teamButton = findViewById(R.id.team_page_button);
        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTeamPage();
            }
        });

        Button myRouteButton = (Button) findViewById(R.id.dailyActivityToRoutes);
        myRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRouteScreen();
            }
        });

        Button teamRouteButton = (Button) findViewById(R.id.teamRoutesButton);
        teamRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToTeamRouteScreen();
            }
        });

        Button proposeWalkButton = (Button) findViewById(R.id.propose_walk_button);
        proposeWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proposeWalkActivity();
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    fitnessService.updateStepCount();
                    wait(1000);
                } catch (Exception e) {
                }
                TextView steps = (TextView) findViewById(R.id.last_steps_num);
                String step = startSteps + "";
                steps.setText(step);
                SharedPreferences sharedPreferences = getSharedPreferences("total_inches", MODE_PRIVATE);
                MainActivity.inches = sharedPreferences.getInt("total_inch", 0);
                walkActivity();
            }
        });
    }

    public void heightActivity() {
        Intent intent = new Intent(this,AskHeight_Activity.class);
        startActivity(intent);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor =prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

    public void proposeWalkActivity(){
        Intent intent= new Intent(this, ProposeWalkActivity.class);
        startActivity(intent);
    }

    public void walkActivity() {
        Intent intent = new Intent(getApplicationContext(), WalkScreenActivity.class);
        intent.putExtra("previousClass", "MainActivity");
        startActivity(intent);
    }

    public void switchToRouteScreen() {
        Intent intent = new Intent(this, RouteScreen.class);
        startActivity(intent);
    }

    public void switchToTeamRouteScreen() {
        Intent intent = new Intent(this, TeamRouteScreen.class);
        startActivity(intent);
    }

    public void goToTeamPage() {
        Intent intent = new Intent(this, TeamPageScreen.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        try {
            fitnessService.updateStepCount();

            wait(1000);
        } catch (Exception e) {
        }

        // update daily steps and daily miles
        TextView t = findViewById(R.id.daily_steps_num);
        TextView d = findViewById(R.id.daily_distance_num);
        double miles = user.getDailyMiles();
        t.setText(String.valueOf(user.getDailySteps()));
        d.setText(String.format("%.2f", miles) + " miles");

        // Update the time
        TextView displayTime = (TextView) findViewById(R.id.last_time_num);
        TextView lastIntentSteps = (TextView) findViewById(R.id.last_steps_num);
        TextView lastDistance = findViewById(R.id.last_distance_num);

        long totalTime = user.getLastIntentionalTime();
        long totalInt = totalTime / 1000;
        String hours = totalInt / 3600 + "hrs ";
        String minutes = (totalInt / 60) % 60 + "min ";
        String seconds = totalInt % 60 + "s";

        lastIntentSteps.setText(String.valueOf(user.getLastIntentSteps()));
        displayTime.setText(hours + minutes + seconds);
        lastDistance.setText(String.format("%.2f",user.getLastIntentMiles()) + " miles");

        Log.d("updateIntentionalWalk", "Last intentional walk details have been updated.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateSteps() {
        fitnessService.updateStepCount();
    }

    private void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(CHAT_ID)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                );
    }

    private void subscribeToNotificationsTopic2() {
        FirebaseMessaging.getInstance().subscribeToTopic("proposed")
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications for team walk";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                );
    }

}
