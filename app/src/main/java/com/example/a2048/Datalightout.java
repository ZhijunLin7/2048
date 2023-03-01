package com.example.a2048;

public class Datalightout {

    private int user_id;
    private String name;
    private int dimension;
    private int pasos_restante;
    private int tiempo_restante;

    public Datalightout(int user_id, int dimension, int pasos_restante, int tiempo_restante) {
        this.user_id = user_id;
        this.dimension = dimension;
        this.pasos_restante = pasos_restante;
        this.tiempo_restante = tiempo_restante;
    }

    public Datalightout() {

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
