package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class RoutesActivity extends AppCompatActivity {
    private final String EMPTY_STRING = "";

    RadioGroup flatGroup, loopGroup, streetGroup, surfaceGroup, difficultyGroup;
    RadioButton flatButton, loopButton, streetButton, surfaceButton, difficultyButton;
    EditText routeName, startLocation, notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // Initialize all group variables.
        flatGroup = findViewById(R.id.groupFlat);
        loopGroup = findViewById(R.id.groupLoop);
        streetGroup = findViewById(R.id.groupStreet);
        surfaceGroup = findViewById(R.id.groupSurface);
        difficultyGroup = findViewById(R.id.groupDifficulty);
        routeName = findViewById(R.id.routeNamePage);
        startLocation = findViewById(R.id.startLocationName);
        notes = findViewById(R.id.routeNotes);

        routeName.setText(EMPTY_STRING);
        startLocation.setText(EMPTY_STRING);
        notes.setText(EMPTY_STRING);


        Button ok_button = findViewById(R.id.button_ok);
        ok_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                pressOK(view);
            }
        });




    } // end onCreate()

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

        // set info
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

        long steps = MainActivity.finalSteps - MainActivity.startSteps;
        long seconds = (int) getIntent().getLongExtra("newTime", 0) / 1000;

        if (getIntent().getBooleanExtra("addNewRoute", false)) {
            steps = 0;
        }

        if (!routeName.getText().toString().equals(EMPTY_STRING) ) {
            RouteScreen.addToRouteList(routeName.getText().toString(),
                    startLocation.getText().toString(), steps, 0,
                    seconds, flatOrHilly, loopOrOut, streetOrTrail, surface, difficulty,
                    notes.getText().toString(), false);

            String json = gson.toJson(RouteScreen.routeList);
            editor.putString("route list", json);
            editor.apply();
        }
        finish();
    }

} // end RoutesActivity
