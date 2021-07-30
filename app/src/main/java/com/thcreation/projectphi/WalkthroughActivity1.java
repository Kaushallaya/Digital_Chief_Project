package com.thcreation.projectphi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WalkthroughActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough1);
    }

    public void next(View view) {
        Intent intent = new Intent(this,WalkthroughActivity2.class);
        startActivity(intent);
        finish();
    }

    public void skip(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}