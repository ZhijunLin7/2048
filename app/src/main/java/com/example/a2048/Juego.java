package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.graphics.Color;
import android.graphics.Typeface;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class Juego extends AppCompatActivity {

    private GridLayout gridfondo;
    private GridLayout gridjuego;
    private GestureDetectorCompat gestureDetector;
    private TextView[][] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        gridfondo = (GridLayout) findViewById(R.id.Gridfondo);
        gridjuego = (GridLayout) findViewById(R.id.Gridjuego);
        this.configurarGrid(gridfondo, gridjuego, 4);

        textViews = new TextView[4][4];
        this.crear2dArray(gridjuego);

        this.añadirNumRandom(gridjuego, 4);


        this.moverArriba(textViews, gridjuego);
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
                        moverArriba(getTextViews(), getGridjuego());
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

    //Añadir textview al Gridlayout de fondo y al carta de numero en grid juego
    public void añadirTextview(GridLayout gridfondo, GridLayout gridjuego) {

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f));

        //Añadir textview con el color y shape al gridfondo
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.carta2048);
        textView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        gridfondo.addView(textView, layoutParams);

        //Añadir textview al grid juego pero con los parametros de texto
        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.carta2048);
        textView2.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        textView2.setText("0");
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextSize(30);
        textView2.setTypeface(Typeface.DEFAULT_BOLD);

        gridjuego.addView(textView2, layoutParams);
    }

    public void configurarGrid(GridLayout gridfondo, GridLayout gridjuego, int numColumnaFila) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        //Configuracion del fondo de gridfondo
        gridfondo.getLayoutParams().height = width - 40;
        gridfondo.setColumnCount(numColumnaFila);
        gridfondo.setRowCount(numColumnaFila);

        //Configuracion del gridjuego
        gridjuego.getLayoutParams().height = width - 40;
        gridjuego.setColumnCount(numColumnaFila);
        gridjuego.setRowCount(numColumnaFila);

        for (int i = 0; i < (numColumnaFila * numColumnaFila); i++) {
            this.añadirTextview(gridfondo, gridjuego);
        }

    }

    //Busca los textiew que esta vacio "0" , lo mete en un array y mete numero un 2 o 4 a los vacios
    public void añadirNumRandom(GridLayout gridjuego, int numColumnaFila) {

        TextView[] textViews = new TextView[numColumnaFila * numColumnaFila];
        for (int i = 0; i < gridjuego.getChildCount(); i++) {
            if (((TextView) gridjuego.getChildAt(i)).getText() == "0") {
                textViews[i] = (TextView) gridjuego.getChildAt(i);
            }
        }

        int textviewAleatorio = (int) (Math.random() * textViews.length);
        int numeroAleatorio = (int) (Math.random() * 5);

        if (numeroAleatorio == 1) {
            textViews[textviewAleatorio].setText("4");
        } else {
            textViews[textviewAleatorio].setText("2");
        }
        this.repintar(gridjuego);
    }

    //Pinta el color depende de numero
    public void repintar(GridLayout gridjuego) {
        for (int i = 0; i < gridjuego.getChildCount(); i++) {
            String text = (String) ((TextView) gridjuego.getChildAt(i)).getText();
            GradientDrawable bgShape = (GradientDrawable) gridjuego.getChildAt(i).getBackground();

            if (text != "0") {
                ((TextView) gridjuego.getChildAt(i)).setTextColor(Color.BLACK);
                ((TextView) gridjuego.getChildAt(i)).setVisibility(View.VISIBLE);
            }
            if (text == "0") {
                ((TextView) gridjuego.getChildAt(i)).setVisibility(View.INVISIBLE);
            } else if (text == "2") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color2));
            } else if (text == "4") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color4));
            } else if (text == "8") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color8));
            } else if (text == "16") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color16));
            } else if (text == "32") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color32));
            } else if (text == "64") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color64));
            } else if (text == "128") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color128));
            } else if (text == "256") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color256));
            } else if (text == "512") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color512));
            } else if (text == "1024") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color1024));
            } else if (text == "2048") {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color2048));
            }

        }


    }

    public void crear2dArray(GridLayout gridJuego) {
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                textViews[i][j] = (TextView) gridJuego.getChildAt(k);
                k++;
            }
        }
    }

    public void moverArriba(TextView[][] textViews, GridLayout gridJuego) {

        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                TextView carta = textViews[j][i];
                if (carta.getText() != "0" || textViews[j - 1][i].getText() == "0") {
                    textViews[j - 1][i].setText(carta.getText());
                }
                Log.d("testeo", "moverArriba: " + textViews[j][i].getText());
                this.repintar(gridJuego);
            }
        }
    }

    //Getter y Setters
    public GridLayout getGridfondo() {
        return gridfondo;
    }

    public void setGridfondo(GridLayout gridfondo) {
        this.gridfondo = gridfondo;
    }

    public GridLayout getGridjuego() {
        return gridjuego;
    }

    public void setGridjuego(GridLayout gridjuego) {
        this.gridjuego = gridjuego;
    }

    public GestureDetectorCompat getGestureDetector() {
        return gestureDetector;
    }

    public void setGestureDetector(GestureDetectorCompat gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public TextView[][] getTextViews() {
        return textViews;
    }

    public void setTextViews(TextView[][] textViews) {
        this.textViews = textViews;
    }
}