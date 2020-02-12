package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        // set info
        editor.putString("routeName", routeName.getText().toString());
        editor.putString("startLocation", startLocation.getText().toString());
        editor.putString("flatOrHilly", flatButton.getText().toString());
        editor.putString("loopOrOut", loopButton.getText().toString());
        editor.putString("streetOrTrail", streetButton.getText().toString());
        editor.putString("surface", surfaceButton.getText().toString());
        editor.putString("difficulty", difficultyButton.getText().toString());
        editor.putString("notes", notes.getText().toString());

        editor.apply();

        RouteScreen.addToRouteList(routeName.getText().toString(),
                startLocation.getText().toString(), 0, 0, 0,
                notes.getText().toString(), false);

        RouteScreen.notifyInsert();

        Gson gson = new Gson();
        String json = gson.toJson(RouteScreen.routeList);
        editor.putString("route list", json);
        editor.apply();

        finish();
    }

} // end RoutesActivity
