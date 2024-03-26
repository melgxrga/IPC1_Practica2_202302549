package com.melgar;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ViajesIniciadosController {
    @FXML
    private TableView<EstadoViaje> tablaViajes;
    @FXML
    private TableColumn<EstadoViaje, String> columnaNombre;
    @FXML
    private TableColumn<EstadoViaje, Float> columnaDistancia;
    @FXML
    private TableColumn<EstadoViaje, Float> columnaCapacidad;

    @FXML
    public void initialize() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreVehiculo"));
        columnaDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        columnaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("estadoViajes.bin"))) {
            List<EstadoViaje> estadosViaje = (List<EstadoViaje>) ois.readObject();
            tablaViajes.getItems().addAll(estadosViaje);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}