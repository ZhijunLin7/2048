package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a2048.databinding.ActivityLightoutBinding;


public class Juegolightout extends AppCompatActivity {

    private ActivityLightoutBinding binding;
    private SqlData sql;
    private String usuario;
    private GridLayout gridJuego;
    private Cell[][] cells;
    private int pasos;
    private int dimension;
    private CountDownTimer countDownTimer;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLightoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        dimension=intent.getIntExtra("Dimension",0);
        usuario=intent.getStringExtra("Usuario");

        sql = new SqlData(this);
        // Configurar el entorno de juego
        gridJuego = binding.Gridjuego;

        cells= new Cell[dimension][dimension];
        this.configurarGrid(gridJuego, dimension);
        this.anadirOnclickListener(cells,dimension);

        // Comenzar juego
        this.hacerJuego(cells);
    }

    //----------------------------------------------------------------------------------------------

    //Añadir textview al Gridlayout
    public void anadirCell(GridLayout gridJuego, int numColumnaFila) {

        for (int i = 0; i < numColumnaFila; i++) {
            for (int j = 0; j < numColumnaFila; j++) {
                // Poner el row and colum weight
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)
                );
                layoutParams.width = 0;
                layoutParams.height = 0;


                Cell cell= new Cell(this,false,false);

                cell.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                gridJuego.addView(cell, layoutParams);

                //Anadir los textview al array 2d
                cells[i][j] = cell;
            }
        }
    }

    public void configurarGrid(GridLayout gridjuego, int numColumnaFila) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        //Configuracion del gridjuego
        gridjuego.getLayoutParams().height = width - 40;
        gridjuego.setColumnCount(numColumnaFila);
        gridjuego.setRowCount(numColumnaFila);

        this.anadirCell(gridjuego, numColumnaFila);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void anadirOnclickListener(ImageButton[][] imageButtons,int numColumnaFila) {

        binding.home.setOnClickListener(view -> {
            Intent intent = new Intent(Juegolightout.this,MainActivity.class);
            intent.putExtra("Usuario",usuario);
            startActivity(intent);
        });

        binding.solution.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pintarRespuesta(cells);
                    break;
                case MotionEvent.ACTION_UP:
                    pintar(cells);
                    break;
            }
            return false;
        });

        binding.newgame.setOnClickListener(view -> hacerJuego(cells));

        ImageButton imageButton;
        for (int i = 0; i < numColumnaFila; i++) {
            for (int j = 0; j < numColumnaFila; j++) {
                imageButton = imageButtons[i][j];
                int finalI = i;
                int finalJ = j;
                imageButton.setOnClickListener(view -> apagarEncender(finalI,finalJ));
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    public void  hacerJuego(Cell [][] cells){
        //Reiniciar pasos al 20 y poner al text view
        this.pasos=100;
        binding.numpasos.setText(String.valueOf(this.pasos));
        this.time=100;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer= new CountDownTimer(time* 1000L,1000) {
            @Override
            public void onTick(long l) {
                time= (int) l/1000;
                binding.tiempoRestante.setText(String.valueOf(time));
            }

            @Override
            public void onFinish() {
                checkGanado(cells);
            }
        }.start();

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                // Poner enable al los image buttons cells
                cells[i][j].setEnabled(true);

                // 50% que este encendido
                int num = (int) (Math.random()*2);
                if (num == 0) {
                    this.cambiarEstadoSolucion(cells[i][j]);
                    this.cambiarEstado(cells[i][j]);
                    if (i > 0) {
                        this.cambiarEstado(cells[i-1][j]);
                    }
                    if (i < cells.length-1) {
                        this.cambiarEstado(cells[i+1][j]);
                    }
                    if (j > 0) {
                        this.cambiarEstado(cells[i][j-1]);
                    }
                    if (j < cells.length-1) {
                        this.cambiarEstado(cells[i][j+1]);
                    }
                }
            }
        }
        this.pintar(cells);

    }

    public void apagarEncender(int i,int j){

        this.cambiarEstadoSolucion(cells[i][j]);
        this.cambiarEstadoYImagen(cells[i][j]);
        this.animacionScale(cells[i][j]);

        if (i > 0) {
            this.cambiarEstadoYImagen(cells[i-1][j]);
            this.animacionScale(cells[i-1][j]);
        }
        if (i < cells.length-1) {
            this.cambiarEstadoYImagen(cells[i+1][j]);
            this.animacionScale(cells[i+1][j]);
        }
        if (j > 0) {
            this.cambiarEstadoYImagen(cells[i][j-1]);
            this.animacionScale(cells[i][j-1]);
        }
        if (j < cells.length-1) {
            this.cambiarEstadoYImagen(cells[i][j+1]);
            this.animacionScale(cells[i][j+1]);
        }
        this.pasos-=1;
        binding.numpasos.setText(String.valueOf(this.pasos));

        this.checkGanado(cells);
    }

    public void cambiarEstadoYImagen(Cell cell) {
        if (cell.isEncendido()) {
            cell.setEncendido(false);
            cell.setBackgroundResource(R.drawable.zombi_sin_color);
        }else {
            cell.setEncendido(true);
            cell.setBackgroundResource(R.drawable.zombi);
        }
    }

    public void cambiarEstadoSolucion(Cell cell) {
        cell.setSolucion(!cell.isSolucion());
    }

    public void cambiarEstado(Cell cell) {
        cell.setEncendido(!cell.isEncendido());
    }

    public void pintarRespuesta(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].isSolucion()) {
                    cells[i][j].setBackgroundResource(R.drawable.zombi_mano);
                    this.animacionScale(cells[i][j]);
                }
            }
        }
    }

    public void pintar(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].isEncendido()) {
                    cells[i][j].setBackgroundResource(R.drawable.zombi);
                    this.animacionScale(cells[i][j]);
                }else{
                    cells[i][j].setBackgroundResource(R.drawable.zombi_sin_color);
                    this.animacionScale(cells[i][j]);
                }
            }
        }
    }

    public void checkGanado(Cell[][] cells){
        boolean ganado=true;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].isEncendido()) {
                    ganado= false;
                }
            }
        }
        if (this.pasos > 0 && this.time>0) {
            if (ganado) {
                sql.adddatalightout(usuario,dimension,pasos,time);
                this.gameDialog(true);
                this.disableCell(cells);
            }
        }else if(this.pasos == 0 || this.time==0){
            this.gameDialog(false);
            this.disableCell(cells);
        }
    }

    public void gameDialog(boolean ganado){
        String msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (ganado) {
            msg="Has ganado!!! ¿quieres jugar otra?";
        }else{
            msg="Has perdido T-T ¿quieres jugar otra?";
            builder.setNeutralButton("solucion",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    pintarRespuesta(cells);
                }
            });
        }
        builder.setMessage(msg)
                .setPositiveButton("Jugar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        hacerJuego(cells);
                    }
                })
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Juegolightout.this,MainActivity.class);
                        intent.putExtra("Usuario",usuario);
                        startActivity(intent);
                    }
                });

        AlertDialog a=builder.create();
        a.setCancelable(false);
        a.show();
    }

    public void animacionScale(ImageButton imageButton) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, 100, 100);
        scaleAnimation.setDuration(100);
        imageButton.startAnimation(scaleAnimation);
    }

    public void disableCell(Cell[][] cells){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
               cells[i][j].setEnabled(false);
            }
        }
    }




}