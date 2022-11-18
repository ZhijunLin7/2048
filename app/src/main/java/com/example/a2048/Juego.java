package com.example.a2048;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Juego extends AppCompatActivity {

    private GridLayout gridfondo;
    private GestureDetectorCompat gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());


        gridfondo = (GridLayout) findViewById(R.id.Gridfondo);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        gridfondo.getLayoutParams().height = width - 40;
        for (int i = 0; i < 16; i++) {
            this.añadirTextview(gridfondo);
        }
    }

    //----------------------------------------------------------------------------------------------

    //Gesture detector
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (diffX > 0) {
                        Toast.makeText(Juego.this, "Derecha", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Juego.this, "Izquierda", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (diffY > 0) {
                        Toast.makeText(Juego.this, "Abajo", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Juego.this, "Arriba", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //----------------------------------------------------------------------------------------------

    //Añadir textview al Gridlayout
    public void añadirTextview(GridLayout gridJuego) {

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f));

        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.carta2048);
        textView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));


        textView.setText("0");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextColor(Color.WHITE);





        gridJuego.addView(textView, layoutParams);
    }

}