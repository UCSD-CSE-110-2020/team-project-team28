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
        else if (getIntent().getExtras() != null && getIntent().getExtras().get("mtype") != null &&
                getIntent().getExtras().get("mtype").equals("TeamInvite")){
            String inviteFrom = getIntent().getExtras().get("mfrom").toString();
            String team = getIntent().getExtras().get("mteam").toString();
            goToInvitePage(team, inviteFrom);
        }else if (getIntent().getExtras() != null && getIntent().getExtras().get("mtype") != null &&
                getIntent().getExtras().get("mtype").equals("TeamWalk")){
            switchToProposedRouteScreen();
        }else if(getIntent().getExtras() != null && getIntent().getExtras().get("mtype") != null &&
                getIntent().getExtras().get("mtype").equals("TeamResponse")){
            switchToTeamPage();
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

    public void goToInvitePage(String teamName, String inviteFrom) {
        Intent intent = new Intent(this, InviteScreenActivity.class);
        intent.putExtra("TEAM", teamName);
        intent.putExtra("FROM", inviteFrom);
        startActivity(intent);
    }
    public void switchToProposedRouteScreen() {
        Intent intent = new Intent(this, ProposedWalkDetails.class);
        startActivity(intent);
    }

    public void switchToTeamPage() {
        Intent intent = new Intent(this, TeamPageScreen.class);
        startActivity(intent);
    }


    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }
}

