package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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

import java.util.ArrayList;
import java.util.Objects;

public class test extends AppCompatActivity {

    private GridLayout gridfondo;
    private GridLayout gridjuego;
    private GestureDetectorCompat gestureDetector;
    private TextView[][] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        gridfondo =findViewById(R.id.Gridfondo);
        gridjuego =findViewById(R.id.Gridjuego);
        this.configurarGrid(gridfondo, gridjuego, 4);

        textViews = new TextView[4][4];
        this.crear2dArray(gridjuego);

        this.añadirNumRandom(gridjuego);
        this.añadirNumRandom(gridjuego);



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
                        Toast.makeText(test.this, "Derecha", Toast.LENGTH_SHORT).show();
                        moverDerecha(textViews);
                    }
                    else {
                        Toast.makeText(test.this, "Izquierda", Toast.LENGTH_SHORT).show();
                        moverIzquierda(textViews);
                    }
                }
                else {
                    if (diffY > 0) {
                        Toast.makeText(test.this, "Abajo", Toast.LENGTH_SHORT).show();
                        moverAbajo(textViews);
                    }
                    else {
                        Toast.makeText(test.this, "Arriba", Toast.LENGTH_SHORT).show();
                        moverArriba(textViews);
                    }
                }
                añadirNumRandom(gridjuego);
            }
            catch (Exception exception) {
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

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));

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
        textView2.setTextSize(24);
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
    public void añadirNumRandom(GridLayout gridjuego) {

        ArrayList<TextView> textViews = new ArrayList<>();
        for (int i = 0; i < gridjuego.getChildCount(); i++) {
            if (((TextView) gridjuego.getChildAt(i)).getText() == "0") {
                textViews.add((TextView) gridjuego.getChildAt(i));
            }
        }

        int textviewAleatorio = (int) (Math.random() * textViews.size());
        int numeroAleatorio = (int) (Math.random() * 5);

        if (numeroAleatorio == 1) {
            textViews.get(textviewAleatorio).setText("4");
        }
        {
            textViews.get(textviewAleatorio).setText("2");
        }
        this.repintar(getGridjuego());

    }


    //Pinta el color depende de numero
    public void repintar(GridLayout gridjuego) {
        for (int i = 0; i < gridjuego.getChildCount(); i++) {
            String text = (String) ((TextView) gridjuego.getChildAt(i)).getText();
            GradientDrawable bgShape = (GradientDrawable) gridjuego.getChildAt(i).getBackground();

            if (!Objects.equals(text, "0")) {
                ((TextView) gridjuego.getChildAt(i)).setTextColor(Color.BLACK);
                gridjuego.getChildAt(i).setVisibility(View.VISIBLE);
            }
            if (Objects.equals(text, "0")) {
                ((TextView) gridjuego.getChildAt(i)).setVisibility(View.INVISIBLE);
            }
            if (Objects.equals(text, "2")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color2));
            }
            if (Objects.equals(text, "4")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color4));
            }
            if (Objects.equals(text, "8")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color8));
            }
            if (Objects.equals(text, "16")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color16));
            }
            if (Objects.equals(text, "32")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color32));
            }
            if (Objects.equals(text, "64")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color64));
            }
            if (Objects.equals(text, "128")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color128));
            }
            if (Objects.equals(text, "256")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color256));
            }
            if (Objects.equals(text, "512")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color512));
            }
            if (Objects.equals(text, "1024")) {
                bgShape.setColor(ContextCompat.getColor(this, R.color.color1024));
            }
            if (Objects.equals(text, "2048")) {
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
//----------------------------------------------------------------------------------------------

    public void moverArriba(TextView[][] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            boolean juntar= true;
            for (int j = 1; j < textViews[i].length; j++) {
                if (textViews[j][i].getText()!="0") {
                    for (int y = j-1; y > -1; y--) {
                        if (textViews[y][i].getText()!="0" ) {
                            if (textViews[y][i].getText().equals(textViews[j][i].getText()) && juntar) {
                                textViews[y][i].setText(String.valueOf((Integer.parseInt((String) textViews[j][i].getText()) * 2)));
                                textViews[j][i].setText("0");
                                juntar=false;
                            }else{
                                String num = (String) textViews[j][i].getText();
                                textViews[j][i].setText("0");
                                textViews[y+1][i].setText(num);
                                juntar= true;
                            }
                            break;
                        }
                        if (y == 0) {
                            textViews[y][i].setText(textViews[j][i].getText());
                            textViews[j][i].setText("0");
                        }
                    }
                }
            }
        }
    }

    public void moverAbajo(TextView[][] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            for (int j = textViews[i].length-1; j >=0 ; j--) {
                for (int y1 = j-1; y1 >=0; y1--) {
                    if (textViews[y1][i].getText()!="0") {
                        if (textViews[j][i].getText()=="0") {
                            textViews[j][i].setText(textViews[y1][i].getText());
                            textViews[y1][i].setText("0");
                            j++;
                        }else if (textViews[j][i].getText().equals(textViews[y1][i].getText())) {
                            textViews[j][i].setText(String.valueOf((Integer.parseInt((String) textViews[j][i].getText()) * 2)));
                            textViews[y1][i].setText("0");
                        }

                        break;
                    }
                }
            }
        }
    }
    public void moverIzquierda(TextView[][] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            for (int j = 0; j < textViews[i].length; j++) {
                for (int y1 = j+1; y1 < 4; y1++) {
                    if (textViews[i][y1].getText()!="0") {
                        if (textViews[i][j].getText()=="0") {
                            textViews[i][j].setText(textViews[i][y1].getText());
                            textViews[i][y1].setText("0");
                            j--;
                        }else if (textViews[i][j].getText().equals(textViews[i][y1].getText())) {
                            textViews[i][j].setText(String.valueOf((Integer.parseInt((String) textViews[i][j].getText()) * 2)));
                            textViews[i][y1].setText("0");
                        }

                        break;
                    }
                }
            }
        }
    }
    public void moverDerecha(TextView[][] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            for (int j = textViews[i].length-1; j >=0 ; j--) {
                for (int y1 = j-1; y1 >=0; y1--) {
                    if (textViews[i][y1].getText()!="0") {
                        if (textViews[i][j].getText()=="0") {
                            textViews[i][j].setText(textViews[i][y1].getText());
                            textViews[i][y1].setText("0");
                            j++;
                        }else if (textViews[i][j].getText().equals(textViews[i][y1].getText())) {
                            textViews[i][j].setText(String.valueOf((Integer.parseInt((String) textViews[i][j].getText()) * 2)));
                            textViews[i][y1].setText("0");
                        }

                        break;
                    }
                }
            }
        }
    }
    public void animacion(TextView fin,TextView mover){
        float x= fin.getX()-mover.getX();

        ObjectAnimator animation = ObjectAnimator.ofFloat(mover, "translationX", x);
        animation.setDuration(2000);
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animation = ObjectAnimator.ofFloat(mover, "translationX", 0);
                animation.setDuration(0);
                animation.start();
                animation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        añadirNumRandom(getGridjuego());

                    }
                });

            }

        });

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