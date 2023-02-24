package com.example.a2048;

public class Usuario {

    private int id;
    private String usuario;
    private String contra;

    public Usuario(String usuario, String contra) {
        this.usuario = usuario;
        this.contra = contra;
    }
    public Usuario() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }
}
