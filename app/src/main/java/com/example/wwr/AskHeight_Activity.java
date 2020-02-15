package com.example.wwr;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
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

        enter = (Button)findViewById(R.id.enter_button);
        feet = (EditText) findViewById(R.id.feet_input);
        feet.addTextChangedListener(heightWatcher);
        inches = (EditText) findViewById(R.id.inches_input);
        inches.addTextChangedListener(heightWatcher);


        enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Feet input

                SharedPreferences sharedPreferences = getSharedPreferences("feet",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("feet_string", feet.getText().toString());
                editor.apply();

                // Inches input

                SharedPreferences sharedPreferences1 = getSharedPreferences("inches",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("inches_string", inches.getText().toString());
                editor1.apply();
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
