package com.example.a2048;

public class Data2048 {

    private int user;
    private String name;
    private int dimension;
    private int puntos;

    public Data2048(int user, int dimension, int puntos) {
        this.user = user;
        this.dimension = dimension;
        this.puntos = puntos;
    }

    public Data2048() {

    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
