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
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class test extends AppCompatActivity {

    private GridLayout gridfondo;
    private GridLayout gridjuego;
    private GestureDetectorCompat gestureDetector;
    private TextView[][] textViews;
    private int[][] numTextViews;
    boolean activado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        gridfondo = findViewById(R.id.Gridfondo);
        gridjuego = findViewById(R.id.Gridjuego);
        this.configurarGrid(gridfondo, gridjuego, 4);

        numTextViews = new int[4][4];
        textViews = new TextView[4][4];
        this.crear2dArray(gridjuego);

        this.añadirNumRandom();
        this.añadirNumRandom();

        for (TextView[] textView : textViews) {
            for (int j = 0; j < textView.length; j++) {
                repintar(textView[j]);
            }
        }

    }

    //----------------------------------------------------------------------------------------------

    //Gesture detector
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            activado = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (diffX > 0) {

                        Toast.makeText(test.this, "Derecha", Toast.LENGTH_SHORT).show();
                        moverDerecha(numTextViews);
                    } else {
                        Toast.makeText(test.this, "Izquierda", Toast.LENGTH_SHORT).show();
                        moverIzquierda(numTextViews);
                    }
                } else {
                    if (diffY > 0) {
                        Toast.makeText(test.this, "Abajo", Toast.LENGTH_SHORT).show();
                        moverAbajo(numTextViews);
                    } else {
                        Toast.makeText(test.this, "Arriba", Toast.LENGTH_SHORT).show();
                        moverArriba(numTextViews);
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
    public void anadirTextview(GridLayout gridfondo, GridLayout gridjuego) {

        //Aqui le pongo que el hijos de grid va tener misma peso horizontal y vertical
        //Luego es para que no tenga un tamaño especifica para que se pueda funcionar lo anterior
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
        layoutParams.width = 0;
        layoutParams.height = 0;

        //Añadir textview con el color y shape al gridfondo
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.carta2048);
        gridfondo.addView(textView, layoutParams);


        //Añadir textview al grid juego pero con los parametros de texto
        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.carta2048);

        textView2.setText("0");
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextSize(24);
        textView2.setTypeface(Typeface.DEFAULT_BOLD);


        gridjuego.addView(textView2, layoutParams);
    }

    // Añadir los text view al los dos grids
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
            this.anadirTextview(gridfondo, gridjuego);
        }

    }

    //Busca los textiew que esta vacio "0" , lo mete en array y mete numero un 2 o 4 a los vacios
    public void añadirNumRandom() {

        //Ver si el posicion es cero y lo guarda el i y j en un arraylist;
        ArrayList<Integer> numI = new ArrayList();
        ArrayList<Integer> numJ = new ArrayList();
        for (int i = 0; i < this.numTextViews.length; i++) {
            for (int j = 0; j < this.numTextViews[i].length; j++) {
                if (this.numTextViews[i][j] == 0) {
                    numI.add(i);
                    numJ.add(j);
                }
            }
        }
        //Coge un aleatorio de lo que hay en el arraylist
        int ranNum = (int) (Math.random() * numI.size());

        // 1/5 pondra un 4 y 4/5 pondra un 2 y lo coloca al array y al textview
        int numeroAleatorio = (int) (Math.random() * 5);
        if (numeroAleatorio == 1) {
            this.numTextViews[numI.get(ranNum)][numJ.get(ranNum)] = 4;
            this.textViews[numI.get(ranNum)][numJ.get(ranNum)].setText("4");
            this.repintar(textViews[numI.get(ranNum)][numJ.get(ranNum)]);
        } else {
            this.numTextViews[numI.get(ranNum)][numJ.get(ranNum)] = 2;
            this.textViews[numI.get(ranNum)][numJ.get(ranNum)].setText("2");
            this.repintar(textViews[numI.get(ranNum)][numJ.get(ranNum)]);
        }

        Log.d("test", "activado");
        Log.d("test", Arrays.deepToString(numTextViews));
    }

    //Pinta el color depende de numero
    public void repintar(TextView textView) {
        String text = textView.getText().toString();
        GradientDrawable bgShape = (GradientDrawable) textView.getBackground();

        if (!Objects.equals(text, "0")) {
            textView.setTextColor(Color.BLACK);
            textView.setVisibility(View.VISIBLE);
        }
        if (Objects.equals(text, "0")) {
            textView.setVisibility(View.INVISIBLE);
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

    public void moverArriba(int[][] numTextViews) {
        for (int i = 0; i < numTextViews.length; i++) {
            boolean juntar = true;
            for (int j = 1; j < numTextViews[i].length; j++) {
                if (numTextViews[j][i] != 0) {
                    for (int y = j - 1; y >= 0; y--) {
                        if (numTextViews[y][i] != 0) {
                            if (numTextViews[y][i] == numTextViews[j][i] && juntar) {
                                this.juntar(y, i, j, i, false);
                                juntar = false;
                            } else if (y + 1 != j) {
                                this.mover(y + 1, i, j, i, false);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == 0) {
                            this.mover(y, i, j, i, false);
                        }
                    }
                }
            }
        }
    }

    public void moverAbajo(int[][] numTextViews) {
        for (int i = 0; i < numTextViews.length; i++) {
            boolean juntar = true;
            for (int j = numTextViews[i].length - 2; j >= 0; j--) {
                if (numTextViews[j][i] != 0) {
                    for (int y = j + 1; y < numTextViews[i].length; y++) {
                        if (numTextViews[y][i] != 0) {
                            if (numTextViews[y][i] == numTextViews[j][i] && juntar) {
                                this.juntar(y, i, j, i, false);
                                juntar = false;
                            } else if (y - 1 != j) {
                                this.mover(y - 1, i, j, i, false);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == numTextViews[i].length - 1) {
                            this.mover(y, i, j, i, false);
                        }
                    }
                }
            }
        }
    }

    public void moverIzquierda(int[][] numTextViews) {
        for (int i = 0; i < numTextViews.length; i++) {
            boolean juntar = true;
            for (int j = 1; j < numTextViews[i].length; j++) {
                if (numTextViews[i][j] != 0) {
                    for (int y = j - 1; y >= 0; y--) {
                        if (numTextViews[i][y] != 0) {
                            if (numTextViews[i][y] == numTextViews[i][j] && juntar) {
                                this.juntar(i, y, i, j, true);
                                juntar = false;
                            } else if (y + 1 != j) {
                                this.mover(i, y + 1, i, j, true);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == 0) {
                            this.mover(i, y, i, j, true);
                        }
                    }
                }
            }
        }
    }

    public void moverDerecha(int[][] numTextViews) {
        for (int i = 0; i < numTextViews.length; i++) {
            boolean juntar = true;
            for (int j = numTextViews[i].length - 2; j >= 0; j--) {
                if (numTextViews[i][j] != 0) {
                    for (int y = j + 1; y < numTextViews[i].length; y++) {
                        if (numTextViews[i][y] != 0) {
                            if (numTextViews[i][y] == numTextViews[i][j] && juntar) {
                                this.juntar(i, y, i, j, true);
                                juntar = false;
                            } else if (y - 1 != j) {
                                this.mover(i, y - 1, i, j, true);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == numTextViews[i].length - 1) {
                            this.mover(i, y, i, j, true);
                        }
                    }
                }
            }
        }
    }

    public void animacionMover(TextView fin, TextView mover, int numFinal, int numImover, int numJmover, boolean horizontal) {
        float distancia;
        String direccion;
        // Hace calculo depende de direccion y incica al animacion que direccion hay que mover
        if (horizontal) {
            distancia = fin.getX() - mover.getX();
            direccion = "translationX";
        } else {
            distancia = fin.getY() - mover.getY();
            direccion = "translationY";
        }

        // Dedino la animacion
        ObjectAnimator animation = ObjectAnimator.ofFloat(mover, direccion, distancia);
        animation.setDuration(200);

        ObjectAnimator animation2 = ObjectAnimator.ofFloat(mover, direccion, 0);
        animation2.setDuration(0);

        // Cuando el primer animacion acabe vuelve a su sitio
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                /*
                 * Al acabar la animacion pone el textview destino al numero pasado por parametro numFinal.
                 * Y al textview inicial no pongo 0 sino pongo el numero del mismo lugar que el array nuntextview
                 * porque al generar un numero random lo va a pintar, pero esto ejecuta mas lento va reemplazar el numero a 0;
                 */
                int num = numTextViews[numImover][numJmover];
                mover.setText(String.valueOf(num));
                fin.setText(String.valueOf(numFinal));
                repintar(fin);
                repintar(mover);

                animation2.start();

                // Activa una vez para añadir un numero luego de acabar de mover
                if (!activado) {
                    añadirNumRandom();
                    activado = true;
                }

            }
        });
        animation.start();
    }

    // Junatar los numeros
    public void juntar(int numIfin, int numJfin, int numImover, int numJmover, boolean horizontal) {
        this.numTextViews[numIfin][numJfin] = numTextViews[numImover][numJmover] * 2;
        this.numTextViews[numImover][numJmover] = 0;

        this.animacionMover(textViews[numIfin][numJfin], textViews[numImover][numJmover],
                this.numTextViews[numIfin][numJfin], numImover, numJmover, horizontal);
    }

    //Hace mover los numeros
    public void mover(int numIfin, int numJfin, int numImover, int numJmover, boolean horizontal) {
        int num = numTextViews[numImover][numJmover];
        this.numTextViews[numImover][numJmover] = 0;
        this.numTextViews[numIfin][numJfin] = num;

        this.animacionMover(textViews[numIfin][numJfin], textViews[numImover][numJmover],
                this.numTextViews[numIfin][numJfin], numImover, numJmover, horizontal);
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