package com.example.wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;

public class LogInActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("previousActivity") != null &&
                getIntent().getStringExtra("previousActivity").equals("Route Detail")) {
            launchMainActivity(true);
        }

        /*if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            for (String key : bundle.keySet()){
                Toast.makeText(getApplicationContext(), bundle.get(key) + "", Toast.LENGTH_SHORT).show();
            }
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Toast.makeText(getApplicationContext(), "Key: " + key + " Value: " + value, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
            launchMainActivity((false));
        }*/

        if (getIntent().getExtras() != null && getIntent().getExtras().get("mtype").equals("TeamInvite")){
            goToTeamPage();
        }else if (getIntent().getExtras() != null && getIntent().getExtras().get("mtype").equals("TeamWalk")){
            switchToTeamRouteScreen();
        }

        setContentView(R.layout.activity_log_in);

        Button startWWR = findViewById(R.id.startWWRButton);
        startWWR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(false);
            }
        });

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });
    }

    public void launchMainActivity(boolean startPreviousWalk) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        if (startPreviousWalk) {
            intent.putExtra("previousActivity", "Route Detail");
        }
        startActivity(intent);
    }

    public void goToTeamPage() {
        Intent intent = new Intent(this, TeamPageScreen.class);
        startActivity(intent);
    }
    public void switchToTeamRouteScreen() {
        Intent intent = new Intent(this, TeamRouteScreen.class);
        startActivity(intent);
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }
}

