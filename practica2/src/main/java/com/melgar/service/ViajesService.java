package com.melgar.service;

import java.util.ArrayList;
import java.util.List;

import com.melgar.models.Vehiculos;
import com.melgar.models.Viajes;
public class ViajesService {
    private static ViajesService instance;
    private List<Viajes> viajes;

    private ViajesService() {
        viajes = new ArrayList<>();
    }

    public static ViajesService getInstance() {
        if (instance == null) {
            instance = new ViajesService();
        }
        return instance;
    }

    public List<Viajes> getViajes() {
        return viajes;
    }

    public void setViajes(List<Viajes> viajes) {
        this.viajes = viajes;
    }

    public void addViaje(Viajes viaje) {
        viajes.add(viaje);
    }
    
}