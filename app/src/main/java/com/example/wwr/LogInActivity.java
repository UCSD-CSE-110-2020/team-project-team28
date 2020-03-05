package com.example.wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wwr.fitness.FitnessService;
import com.example.wwr.fitness.FitnessServiceFactory;
import com.example.wwr.fitness.GoogleFitAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";

    private static final String TAG = "LogInActivity";

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("previousActivity") != null &&
                getIntent().getStringExtra("previousActivity").equals("Route Detail")) {
            launchMainActivity(true);
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


        //Map<String, Object> user = new HashMap<>();
        //user.put("Karen", "Trash");
        //user.put("Will", "MVP");

        //// Add a new document with a generated ID
        //db.collection("users")
        //        .add(user)
        //        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        //            @Override
        //            public void onSuccess(DocumentReference documentReference) {
        //                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        //            }
        //        })
        //        .addOnFailureListener(new OnFailureListener() {

        //            @Override
        //            public void onFailure(@NonNull Exception e) {
        //                Log.w(TAG, "Error adding document", e);
        //            }
        //        });
    }

    public void launchMainActivity(boolean startPreviousWalk) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        if (startPreviousWalk) {
            intent.putExtra("previousActivity", "Route Detail");
        }
        startActivity(intent);
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }
}

