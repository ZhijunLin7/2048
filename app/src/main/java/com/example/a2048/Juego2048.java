package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.a2048.databinding.ActivityA2048Binding;


import java.util.ArrayList;
import java.util.Objects;

public class Juego2048 extends AppCompatActivity {

    private ActivityA2048Binding binding;
    private GridLayout gridfondo;
    private GridLayout gridjuego;
    private GestureDetectorCompat gestureDetector;

    private TextView[][] textViews;
    private int[][] numTextViews;
    private TextView puntosTextview;
    private TextView maxPuntosTextview;
    private int[][] pasoPrevio;
    private String puntoPrevio;
    boolean activado;
    boolean siguienteMovimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityA2048Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        puntosTextview = binding.Puntos;
        maxPuntosTextview = binding.MaxPuntos;

        gridfondo = binding.Gridfondo;
        gridjuego = binding.Gridjuego;
        this.configurarGrid(gridfondo, gridjuego, 4);

        textViews = new TextView[4][4];
        this.crear2dArray(gridjuego, 4);

        binding.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasoPrevio();
            }
        });

        binding.newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });
        siguienteMovimiento =true;
        this.newGame();

    }

    //----------------------------------------------------------------------------------------------

    public void newGame() {
        this.pasoPrevio = null;
        this.puntosTextview.setText("0");
        this.numTextViews = new int[4][4];
        for (TextView[] textView : textViews) {
            for (TextView view : textView) {
                view.setVisibility(View.INVISIBLE);
            }
        }
        this.anadirNumRandom();
        this.anadirNumRandom();
    }

    public void pasoPrevio() {
        if (pasoPrevio != null) {
            this.puntosTextview.setText(puntoPrevio);
            this.numTextViews = this.pasoPrevio;
            for (int i = 0; i < numTextViews.length; i++) {
                for (int j = 0; j < numTextViews[i].length; j++) {
                    textViews[i][j].setText(String.valueOf(numTextViews[i][j]));
                    this.repintar(textViews[i][j]);
                }
            }
            pasoPrevio = null;
        }
    }

    //Gesture detector
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            activado = false;
            pasoPrevio = copiarArray(numTextViews);
            puntoPrevio = puntosTextview.getText().toString();
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (siguienteMovimiento) {
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (diffX > 0) {
                            moverDerecha(numTextViews);
                        } else {
                            moverIzquierda(numTextViews);
                        }
                    } else {
                        if (diffY > 0) {
                            moverAbajo(numTextViews);
                        } else {
                            moverArriba(numTextViews);
                        }
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
        textView2.setTextColor(Color.BLACK);

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

    // Asigna al matriz de textviews los matrices de gridlayout
    public void crear2dArray(GridLayout gridJuego, int numColumnaFila) {
        int k = 0;
        for (int i = 0; i < numColumnaFila; i++) {
            for (int j = 0; j < numColumnaFila; j++) {
                textViews[i][j] = (TextView) gridJuego.getChildAt(k);
                k++;
            }
        }
    }

    //Busca los textiew que esta vacio "0" , lo mete en array y mete numero un 2 o 4 a los vacios
    public void anadirNumRandom() {

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

        this.animacionScale(textViews[numI.get(ranNum)][numJ.get(ranNum)]);

    }

    //Pinta el color depende de numero
    public void repintar(TextView textView) {
        String text = textView.getText().toString();
        GradientDrawable bgShape = (GradientDrawable) textView.getBackground();

        if (!Objects.equals(text, "0")) {
            textView.setVisibility(View.VISIBLE);
        }
        if (Objects.equals(text, "0")) {
            textView.setVisibility(View.INVISIBLE);
        }

        switch (text) {
            case "2":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color2));
                break;
            case "4":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color4));
                break;
            case "8":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color8));
                break;
            case "16":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color16));
                break;
            case "32":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color32));
                break;
            case "64":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color64));
                break;
            case "128":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color128));
                break;
            case "256":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color256));
                break;
            case "1024":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color1024));
                break;
            case "2048":
                bgShape.setColor(ContextCompat.getColor(this, R.color.color2048));
                break;
        }

    }

    public int[][] copiarArray(int[][] verdad) {
        int[][] copia = new int[verdad.length][verdad.length];
        for (int i = 0; i < verdad.length; i++) {
            for (int j = 0; j < verdad.length; j++) {
                copia[i][j] = verdad[i][j];
            }
        }
        return copia;
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
                                this.juntar(y, i, j, i, false, true);
                                juntar = false;
                            } else if (y + 1 != j) {
                                this.mover(y + 1, i, j, i, false, false);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == 0) {
                            this.mover(y, i, j, i, false, false);
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
                                this.juntar(y, i, j, i, false, true);
                                juntar = false;
                            } else if (y - 1 != j) {
                                this.mover(y - 1, i, j, i, false, false);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == numTextViews[i].length - 1) {
                            this.mover(y, i, j, i, false, false);
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
                                this.juntar(i, y, i, j, true, true);
                                juntar = false;
                            } else if (y + 1 != j) {
                                this.mover(i, y + 1, i, j, true, false);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == 0) {
                            this.mover(i, y, i, j, true, false);
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
                                this.juntar(i, y, i, j, true, true);
                                juntar = false;
                            } else if (y - 1 != j) {
                                this.mover(i, y - 1, i, j, true, false);
                                juntar = true;
                            }
                            break;
                        }
                        if (y == numTextViews[i].length - 1) {
                            this.mover(i, y, i, j, true, false);
                        }
                    }
                }
            }
        }
    }

    public void animacionMover(TextView fin, TextView mover, int numFinal, int numImover, int numJmover, boolean horizontal, boolean juntar) {
        siguienteMovimiento =false;

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
        animation.setDuration(100);

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

                //Hace la animacion de juntar
                if (juntar) {
                    animacionScale(fin);

                }
                // Hace que el mover vuelve a su lugar
                animation2.start();

                // Activa una vez para añadir un numero luego de acabar de mover
                if (!activado) {
                    anadirNumRandom();
                    activado = true;
                    siguienteMovimiento =true;

                    // Luego de añadir un numero mira si ha perdido
                    if (verPerdido()){
                        gameDialog(false);
                    }
                }
            }
        });
        animation.start();
    }

    public void animacionScale(TextView fin) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, 100, 100);
        scaleAnimation.setDuration(100);
        fin.startAnimation(scaleAnimation);
    }

    // Junatar los numeros
    public void juntar(int numIfin, int numJfin, int numImover, int numJmover, boolean horizontal, boolean juntar) {
        this.numTextViews[numIfin][numJfin] = numTextViews[numImover][numJmover] * 2;
        this.numTextViews[numImover][numJmover] = 0;

        // Al juntar anade puntos
        int puntosTotal = Integer.parseInt(this.puntosTextview.getText().toString()) + this.numTextViews[numIfin][numJfin];
        this.puntosTextview.setText(String.valueOf(puntosTotal));

        // Lanza la animacion de mover
        this.animacionMover(textViews[numIfin][numJfin], textViews[numImover][numJmover],
                this.numTextViews[numIfin][numJfin], numImover, numJmover, horizontal, juntar);
    }

    //Hace mover los numeros
    public void mover(int numIfin, int numJfin, int numImover, int numJmover, boolean horizontal, boolean juntar) {
        int num = numTextViews[numImover][numJmover];
        this.numTextViews[numImover][numJmover] = 0;
        this.numTextViews[numIfin][numJfin] = num;
        // Lanza la animacion de mover
        this.animacionMover(textViews[numIfin][numJfin], textViews[numImover][numJmover],
                this.numTextViews[numIfin][numJfin], numImover, numJmover, horizontal, juntar);
    }

    public boolean verPerdido(){
        for (int i = 0; i < this.numTextViews.length; i++) {
            for (int j = 0; j < this.numTextViews[i].length; j++) {
                if (this.numTextViews[i][j] == 0) {
                    return false;
                }
                if (i>0 && this.numTextViews[i][j] == this.numTextViews[i-1][j]) {
                    return false;
                }
                if (i<this.numTextViews.length-1 && this.numTextViews[i][j] == this.numTextViews[i+1][j]) {
                    return false;
                }
                if (j>0 && this.numTextViews[i][j] == this.numTextViews[i][j-1]) {
                    return false;
                }
                if (j<this.numTextViews.length-1 && this.numTextViews[i][j] == this.numTextViews[i][j+1]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void gameDialog(boolean ganado){
        String msg="Has perdido T-T ¿quieres jugar otra?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg)
                .setPositiveButton("Jugar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                    }
                })
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Juego2048.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog a=builder.create();
        a.setCancelable(false);
        a.show();
    }

}