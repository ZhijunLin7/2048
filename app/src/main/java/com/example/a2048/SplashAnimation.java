package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.a2048.databinding.ActivitySplashAnimationBinding;

public class SplashAnimation extends AppCompatActivity {

    private ActivitySplashAnimationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashAnimationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AnimationDrawable animationDrawable = (AnimationDrawable)binding.allScreen.getBackground();
        animationDrawable.setEnterFadeDuration(500);
        animationDrawable.setExitFadeDuration(500);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_move_in);

        binding.SplashImage.startAnimation(animation);
        binding.SplashText.startAnimation(animation);
        animationDrawable.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation2 = AnimationUtils.loadAnimation(SplashAnimation.this, R.anim.splash_move_out);
                binding.SplashImage.startAnimation(animation2);
                animation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.SplashImage.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(SplashAnimation.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}