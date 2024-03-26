package com.melgar;
import java.io.IOException; 
import com.melgar.models.Viajes;
import java.io.Serializable;
import javafx.util.Duration;
public class EstadoViaje implements Serializable {
    private String nombreVehiculo;
    private float distancia;
    private float capacidad;
    // En la clase EstadoViaje
    private double animacionX; // La posición X de la animación
    private double animacionY; // La posición Y de la animación
    private Duration animacionDuracion; // La duración de la animación
    private Viajes viaje;
    // Constructor
    public EstadoViaje(String nombreVehiculo, float distancia, float capacidad) {
        this.nombreVehiculo = nombreVehiculo;
        this.distancia = distancia;
        this.capacidad = capacidad;
    }
    public Viajes getViaje() {
        return this.viaje;
    }
    @Override
    public String toString() {
        return "EstadoViaje{" +
                "animacionX=" + animacionX +
                ", animacionY=" + animacionY +
                ", animacionDuracion=" + animacionDuracion +
                '}';
    }
    public void setAnimacionDuracion(Duration animacionDuracion) {
        this.animacionDuracion = animacionDuracion;
    }   
    public Duration getAnimacionDuracion() {
        return animacionDuracion;
    }
    public EstadoViaje() {

    }

    public double getAnimacionX() {
        return animacionX;
    }

    public void setAnimacionX(double animacionX) {
        this.animacionX = animacionX;
    }

    public double getAnimacionY() {
        return animacionY;
    }

    public void setAnimacionY(double animacionY) {
        this.animacionY = animacionY;
    }

    // Getters
    public String getNombreVehiculo() {
        return nombreVehiculo;
    }

    public float getDistancia() {
        return distancia;
    }

    public float getCapacidad() {
        return capacidad;
    }

    // Setters
    public void setNombreVehiculo(String nombreVehiculo) {
        this.nombreVehiculo = nombreVehiculo;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public void setCapacidad(float capacidad) {
        this.capacidad = capacidad;
    }
}