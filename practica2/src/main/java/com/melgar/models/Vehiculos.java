package com.melgar.models;

public class Vehiculos {
    private int id;
    private String nombre;
    private float consumo;
    private float capacidad;
    private float velocidad;
    public Vehiculos(String nombre, float consumo, float capacidad) {
        this.id = generarId();
        this.nombre = nombre;
        this.consumo = consumo;
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public float getConsumo() {
        return consumo;
    }

    public float getCapacidad() {
        return capacidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setConsumo(float consumo) {
        this.consumo = consumo;
    }

    public void setCapacidad(float capacidad) {
        this.capacidad = capacidad;
    }

    private int generarId() {
        return (int) (Math.random() * 1000);
    }
}