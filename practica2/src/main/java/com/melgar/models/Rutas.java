package com.melgar.models;

public class Rutas {
    private int id;
    private String inicio;
    private String fin;
    public float distancia;

    public int getId() {
        return id;
    }



    public String getInicio() {
        return inicio;
    }

    public void setInicio(String start) {
        this.inicio = start;
    }



    public String getFin() {
        return fin;
    }

    public void setFin(String end) {
        this.fin = end;
    }


    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public Rutas(String inicio, String end, float distancia) {
        this.id = generateId();
        this.inicio = inicio;
        this.fin = end;
        this.distancia = distancia;
    }

    private int generateId() {
        return (int) (Math.random() * 100);
    }

}
