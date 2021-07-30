package com.thcreation.projectphi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    ImageView botmImg;

    TextView name;

    LottieAnimationView lotti;

    DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botmImg = findViewById(R.id.img_botm);
        name = findViewById(R.id.appname);
        lotti = findViewById(R.id.lottie);
        drawerLayout = findViewById(R.id.drawer_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                name.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.text_in));
                name.setVisibility(View.VISIBLE);
                botmImg.setVisibility(View.VISIBLE);
                lotti.setVisibility(View.VISIBLE);
            }
        },1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                name.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.txt_out));
                botmImg.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.image_out));
                botmImg.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
            }
        },4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences = getSharedPreferences("onBoardScreen",MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);

                if(isFirstTime){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this,WalkthroughActivity1.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },5000);

    }
}