package com.thcreation.projectphi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WalkthroughActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough3);
    }

    public void goNext(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}