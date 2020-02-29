package com.example.wwr;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AskHeight_Activity extends AppCompatActivity {
    private EditText feet;
    private EditText inches;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_height_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UserInfo.createDataBase(this);

        enter = (Button)findViewById(R.id.enter_button);
        feet = (EditText) findViewById(R.id.feet_input);
        feet.addTextChangedListener(heightWatcher);
        inches = (EditText) findViewById(R.id.inches_input);
        inches.addTextChangedListener(heightWatcher);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                int in = Integer.valueOf(inches.getText().toString());
                int ft = Integer.valueOf(feet.getText().toString());
                int totalInches = (ft * 12) + in;

                SharedPreferences sharedPreferences2 = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putInt("totalInches", totalInches);
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
            String feetInput = feet.getText().toString().trim();
            String inchInput = inches.getText().toString().trim();
            enter.setEnabled(!feetInput.isEmpty() && !inchInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
