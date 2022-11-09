package com.example.a2048;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

public class Juego extends AppCompatActivity {

    private GridLayout gridJuego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        gridJuego=(GridLayout)findViewById(R.id.Gridjuego);
        for (int i = 0; i < 12; i++) {
            this.añadirTextview(gridJuego);
        }
    }

    public void  añadirTextview(GridLayout gridJuego){

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
        );

        TextView textView = new TextView(this);
        textView.setText("0");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new ViewGroup.LayoutParams(0,0));
        textView.setTextColor(Color.WHITE);

        gridJuego.addView(textView,layoutParams);
    }

}