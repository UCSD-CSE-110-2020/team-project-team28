package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RouteDetail extends AppCompatActivity {
    TextView name;
    TextView note;
    TextView steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        name = (TextView) findViewById(R.id.route_detail_title);
        note = (TextView) findViewById(R.id.route_detail_note);
        steps = (TextView) findViewById(R.id.route_detail_steps);

        name.setText(getIntent().getStringExtra("routeName"));
        note.setText(getIntent().getStringExtra("note"));
        note.setText(getIntent().getStringExtra("steps"));
    }
}
