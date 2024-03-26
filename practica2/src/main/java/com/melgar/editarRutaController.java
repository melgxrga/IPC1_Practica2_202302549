package com.melgar;

import java.util.Optional;

import com.melgar.models.Rutas;
import com.melgar.service.RutasService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editarRutaController {
    @FXML
    private TextField idTextField;
    @FXML
    private TextField distanciaTextField;
    private CargaDeRutasController cargarRutasController;

    @FXML
private void aceptarCambios() {
    try {
        int id = Integer.parseInt(idTextField.getText());
        float distancia = Float.parseFloat(distanciaTextField.getText());

        Optional<Rutas> rutaOptional = RutasService.getRouteById(id);

        if (rutaOptional.isPresent()) {
            Rutas ruta = rutaOptional.get();
            ruta.setDistancia(distancia);
            RutasService.updateRoute(ruta);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Ruta actualizada","La ruta ha sido actualizada exitosamente");
            cargarRutasController.actualizarTabla();
            Stage stage = (Stage) idTextField.getScene().getWindow();
            stage.close();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se ha encontrado la ruta con el id especificado");
        }
    } catch (NumberFormatException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Error", "ID y distancia deben ser números");
    }
}
    public void setCargaDeRutasController(CargaDeRutasController controller) {
        this.cargarRutasController = controller;
    }
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
