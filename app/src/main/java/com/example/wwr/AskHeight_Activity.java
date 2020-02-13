package com.example.wwr;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AskHeight_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_height_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button enter = (Button)findViewById(R.id.enter_button);
        enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText height = (EditText) findViewById(R.id.height_input);
                SharedPreferences sharedPreferences = getSharedPreferences("height",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("height_string", height.getText().toString());
                editor.apply();
                finish();
            }
        });

    }

}
