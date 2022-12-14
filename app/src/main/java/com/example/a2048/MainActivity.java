package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button botonComenzar;
    private ImageButton botonSiguiente;
    private ImageButton BotonAnterior;
    private String[] numColumnaYFila = {"3x3", "4x4", "5x5", "6x6"};
    private TextView verDimension;

    private int numPosicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verDimension = (TextView) findViewById(R.id.verDimension);
        verDimension.setText(numColumnaYFila[numPosicion]);

        botonComenzar = (Button) findViewById(R.id.comenzar);
        botonComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, test.class);
                startActivity(intent);
            }
        });

        BotonAnterior = (ImageButton) findViewById(R.id.anterior);
        BotonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numPosicion == 0) {
                    numPosicion = numColumnaYFila.length - 1;
                    verDimension.setText(numColumnaYFila[numPosicion]);
                } else {
                    numPosicion = (numPosicion - 1) % numColumnaYFila.length;
                    verDimension.setText(numColumnaYFila[numPosicion]);
                }
            }
        });

        botonSiguiente = (ImageButton) findViewById(R.id.siguiente);
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPosicion = (numPosicion + 1) % numColumnaYFila.length;
                verDimension.setText(numColumnaYFila[numPosicion]);

            }
        });
    }
}