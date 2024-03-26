package com.melgar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.melgar.models.Rutas;

public class RutasService {
    private static List<Rutas> rutas = new ArrayList<>();

    public static List<Rutas> getRoutes() {
        return rutas;
    }

    public static void addRoute(Rutas ruta) {
        Optional.ofNullable(ruta).ifPresent(rutas::add);
    }

    public static void removeRoute(Rutas ruta) {
        rutas = rutas.stream()
                     .filter(r -> !r.equals(ruta))
                     .collect(Collectors.toList());
    }

    public static Optional<Rutas> getRouteById(int id) {
        return rutas.stream()
                     .filter(r -> r.getId() == id)
                     .findFirst();
    }

    
    public static void updateRoute(Rutas ruta) {
        for (Rutas l : rutas) {
            if (l.getId() == ruta.getId()) {
                l.setInicio(ruta.getInicio());
                l.setFin(ruta.getFin());
                l.setDistancia(ruta.getDistancia());
            }
        }
    }
}