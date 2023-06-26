package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


    ImageView ivHeart;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay=200;
    private static int SPLASH_TIME_OUT=3000;

    Handler handler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ivHeart = findViewById(R.id.iv_heart);
        textView=findViewById(R.id.text_view);
        //set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //INITIALIZE
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(ivHeart,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));

        //set duration
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.REVERSE);

        // start animation
        objectAnimator.start();
        //set animated text
        animatText("Welcome to Ninaad");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                SharedPreferences sharedPreferences= getSharedPreferences(LoginActivity.PREFS_NAME,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);

                if(hasLoggedIn){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class );
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, 6000);

    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            textView.setText(charSequence.subSequence(0, index++));
            if (index<=charSequence.length()){
                handler.postDelayed(runnable, delay);
            }

        }
    } ;


    public void animatText(CharSequence cs){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        charSequence=cs;
        index=0;
        textView.setText("");

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
    }

}