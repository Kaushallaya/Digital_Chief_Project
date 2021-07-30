package com.thcreation.projectphi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WalkthroughActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough2);
    }

    public void next(View view) {
        Intent intent = new Intent(this,WalkthroughActivity3.class);
        startActivity(intent);
        finish();
    }

    public void skip(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}