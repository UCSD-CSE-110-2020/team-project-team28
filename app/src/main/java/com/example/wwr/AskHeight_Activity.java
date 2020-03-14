package com.example.wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AskHeight_Activity extends AppCompatActivity {
    private EditText feet;
    private EditText inches;
    private EditText enterName;
    private EditText enterEmail;
    private Button enter;
    private static final String TAG = "Upload User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_height_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UserInfo.createDataBase(this);

        enter = (Button)findViewById(R.id.enter_button);
        feet = (EditText) findViewById(R.id.feet_input);
        inches = (EditText) findViewById(R.id.inches_input);
        enterName = findViewById(R.id.enter_name);
        enterEmail = findViewById(R.id.enter_email);

        feet.addTextChangedListener(heightWatcher);
        inches.addTextChangedListener(heightWatcher);
        enterName.addTextChangedListener(heightWatcher);
        enterEmail.addTextChangedListener(heightWatcher);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Save the inches, feet, userName, and the userEmail.
                int in = Integer.valueOf(inches.getText().toString());
                int ft = Integer.valueOf(feet.getText().toString());
                String userName = enterName.getText().toString();
                String userEmail = enterEmail.getText().toString();
                int totalInches = (ft * 12) + in;

                SharedPreferences sharedPreferences2 = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putInt("totalInches", totalInches);
                editor2.putString("userName", userName);
                editor2.putString("userEmail", userEmail);
                editor2.apply();
                Log.d("saveHeight", "Height has been saved.");
                finish();

            }
        });
    }

    public TextWatcher heightWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Get the user fields when the text is changed.
            String feetInput = feet.getText().toString().trim();
            String inchInput = inches.getText().toString().trim();
            String nameInput = enterName.getText().toString().trim();
            String emailInput = enterEmail.getText().toString().trim();
            enter.setEnabled(!feetInput.isEmpty() && !inchInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
