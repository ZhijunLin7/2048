package com.example.a2048;

public class Data2048 {

    private String user;
    private int dimension;
    private int puntos;

    public Data2048(String user, int dimension, int puntos) {
        this.user = user;
        this.dimension = dimension;
        this.puntos = puntos;
    }

    public Data2048() {

    }

    public String getUser_id() {
        return user;
    }

    public void setUser_id(String user) {
        this.user = user;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
