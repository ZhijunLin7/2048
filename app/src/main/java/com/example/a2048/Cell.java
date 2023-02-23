package com.example.a2048;

import android.content.Context;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

public class Cell extends androidx.appcompat.widget.AppCompatImageButton {

    private boolean solucion;
    private boolean encendido;

    public Cell(@NonNull Context context,boolean solucion, boolean encendido) {
        super(context);
        this.solucion=solucion;
        this.encendido = encendido;
    }

    public boolean isEncendido() {
        return encendido;
    }

    public void setEncendido(boolean encendido) {
        this.encendido = encendido;
    }

    public boolean isSolucion() {
        return solucion;
    }

    public void setSolucion(boolean solucion) {
        this.solucion = solucion;
    }
}
