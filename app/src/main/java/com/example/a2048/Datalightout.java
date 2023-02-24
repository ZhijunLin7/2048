package com.example.a2048;

public class Datalightout {

    private String user;
    private int dimension;
    private int pasos_restante;
    private int tiempo_restante;

    public Datalightout(String user, int dimension, int pasos_restante, int tiempo_restante) {
        this.user = user;
        this.dimension = dimension;
        this.pasos_restante = pasos_restante;
        this.tiempo_restante = tiempo_restante;
    }

    public Datalightout() {

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

    public int getPasos_restante() {
        return pasos_restante;
    }

    public void setPasos_restante(int pasos_restante) {
        this.pasos_restante = pasos_restante;
    }

    public int getTiempo_restante() {
        return tiempo_restante;
    }

    public void setTiempo_restante(int tiempo_restante) {
        this.tiempo_restante = tiempo_restante;
    }
}
