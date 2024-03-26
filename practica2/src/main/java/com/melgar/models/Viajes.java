package com.melgar.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Viajes implements Serializable {

    private int id;
    private String inicio;
    private String fin;
    public float distancia =0;
    public boolean status ;
    private String nombreTransporte;

    public Viajes( String inicio, String fin, float distancia, boolean status, String nombreTransporte) {
        this.id = generarId();
        this.inicio = inicio;
        this.fin = fin;
        this.distancia = distancia;
        this.status = status;
        this.nombreTransporte = nombreTransporte;
    }
    public void iniciarViaje(){
        this.status = true;
    }
    @Override
    public String toString() {
        return "Viajes{" +
                "id=" + id +
                ", inicio='" + inicio + '\'' +
                ", fin='" + fin + '\'' +
                ", distancia=" + distancia +
                ", status=" + status +
                ", nombreTransporte='" + nombreTransporte + '\'' +
                '}';
    }


    public Viajes( String inicio, String fin, String nombreTransporte) {
        this.id = generarId();
        this.inicio = inicio;
        this.fin = fin;
        this.distancia = distancia;
        this.status = status;
        this.nombreTransporte = nombreTransporte;
    }
    

    public int getId() {
        return id;
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public float getDistancia() {
        return distancia;
    }

    public boolean getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getNombreTransporte() {
        return nombreTransporte;
    }

    public void setNombreTransporte(String nombreTransporte) {
        this.nombreTransporte = nombreTransporte;
    }

    private int generarId() {
        return (int) (Math.random() * 1000);
    }
}