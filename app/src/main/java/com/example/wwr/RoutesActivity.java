package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import java.text.DecimalFormat;

public class RoutesActivity extends AppCompatActivity {
    private final String EMPTY_STRING = "";
    private UserInfo user;
    private boolean manuallyAdded;
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";

    RadioGroup flatGroup, loopGroup, streetGroup, surfaceGroup, difficultyGroup;
    RadioButton flatButton, loopButton, streetButton, surfaceButton, difficultyButton;
    EditText routeName, startLocation, notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new UserInfo(this);
        setContentView(R.layout.activity_routes);

        // Initialize all group variables.
        flatGroup = findViewById(R.id.groupFlat);
        loopGroup = findViewById(R.id.groupLoop);
        streetGroup = findViewById(R.id.groupStreet);
        surfaceGroup = findViewById(R.id.groupSurface);
        difficultyGroup = findViewById(R.id.groupDifficulty);

        routeName = (EditText)findViewById(R.id.routeNamePage);
        startLocation = (EditText)findViewById(R.id.startLocationName);
        notes = (EditText)findViewById(R.id.routeNotes);

        routeName.setText(EMPTY_STRING);
        startLocation.setText(EMPTY_STRING);
        notes.setText(EMPTY_STRING);

        // Create the route.
        Button ok_button = findViewById(R.id.button_ok);
        ok_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                pressOK(view);
            }
        });
    }

    public void pressOK (View view) {
        // grab radio button selections
        int radioId = flatGroup.getCheckedRadioButtonId();
        flatButton = findViewById(radioId);

        radioId = loopGroup.getCheckedRadioButtonId();
        loopButton = findViewById(radioId);

        radioId = streetGroup.getCheckedRadioButtonId();
        streetButton = findViewById(radioId);

        radioId = surfaceGroup.getCheckedRadioButtonId();
        surfaceButton = findViewById(radioId);

        radioId = difficultyGroup.getCheckedRadioButtonId();
        difficultyButton = findViewById(radioId);

        // grab user inputted text
        routeName = (EditText) findViewById(R.id.routeNamePage);
        startLocation = (EditText) findViewById(R.id.startLocationName);
        notes = (EditText) findViewById(R.id.routeNotes);

        // now save values
        SharedPreferences userPref = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();

        String flatOrHilly = flatButton.getText().toString();
        String loopOrOut = loopButton.getText().toString();
        String streetOrTrail = streetButton.getText().toString();
        String surface = surfaceButton.getText().toString();
        String difficulty = difficultyButton.getText().toString();

        // Save the information about the route.
        editor.putString("routeName", routeName.getText().toString());
        editor.putString("startLocation", startLocation.getText().toString());
        editor.putString("flatOrHilly", flatOrHilly);
        editor.putString("loopOrOut", loopOrOut);
        editor.putString("streetOrTrail", streetOrTrail);
        editor.putString("surface", surface);
        editor.putString("difficulty", difficulty);
        editor.putString("notes", notes.getText().toString());

        editor.apply();
        Gson gson = new Gson();

        long totalSteps = user.getLastIntentSteps();
        long seconds = user.getLastIntentionalTime();

        // the case of when you're manually adding the route
        if (getIntent().getBooleanExtra("addNewRoute", false)) {
            totalSteps = 0;
            manuallyAdded = true;
        } else {
            manuallyAdded = false;
        }

        double miles = user.getLastIntentMiles();
        String strMiles = new DecimalFormat("#.##").format(miles);
        Double formattedMiles = Double.valueOf(strMiles);

        SharedPreferences sharedPreferences2 = getSharedPreferences("prefs", MODE_PRIVATE);
        String userName = sharedPreferences2.getString("userName", "");
        String userEmail = sharedPreferences2.getString("userEmail", "");

        // Check to make sure user added a name to the route before saving and add the route to the routeList.
        if (!routeName.getText().toString().equals(EMPTY_STRING) ) {
            if (manuallyAdded) {
                RouteScreen.addToRouteList(userName, userEmail, routeName.getText().toString(),
                        startLocation.getText().toString(), totalSteps, formattedMiles, seconds, flatOrHilly,
                        loopOrOut, streetOrTrail, surface, difficulty,
                        notes.getText().toString(), false, manuallyAdded);
            } else {
                RouteScreen.addToRouteList(userName, userEmail, routeName.getText().toString(),
                        startLocation.getText().toString(), totalSteps, formattedMiles, seconds, flatOrHilly,
                        loopOrOut, streetOrTrail, surface, difficulty,
                        notes.getText().toString(), false, manuallyAdded);
            }
            String json = gson.toJson(RouteScreen.routeList);
            editor.putString("route list", json);
            editor.apply();
            Log.d("createdNewRouteList", "New route has been added");

        }
        finish();
    }
}
