package com.melgar.service;
import com.melgar.models.*;
import java.util.List;
public class VehiculosService {
    public static List<Vehiculos> vehiculos = List.of(
    new Vehiculos("Motocicleta 1", 0.1f, 6),
    new Vehiculos("Motocicleta 2", 0.1f, 6),
    new Vehiculos("Motocicleta 3", 0.1f, 6),
    new Vehiculos("Vehículo Estándar 1", 0.3f, 10),
    new Vehiculos("Vehículo Estándar 2", 0.3f, 10),
    new Vehiculos("Vehículo Estándar 3", 0.3f, 10),
    new Vehiculos("Vehículo Premium 1", 0.45f, 12),
    new Vehiculos("Vehículo Premium 2", 0.45f, 12),
    new Vehiculos("Vehículo Premium 3", 0.45f, 12));


    public List<Vehiculos> getVehiculos() {
        return vehiculos;
    }
}
