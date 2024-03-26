package com.melgar;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import com.melgar.models.*;
import java.util.List;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.melgar.service.RutasService;

// import com.melgar.service.RutasService;
public class CargaDeRutasController implements Initializable {
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn inicioColumn;
    @FXML
    private TableColumn finColumn;
    @FXML
    private TableColumn distanciaColumn;
    @FXML
    private Button volverAlMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            idColumn.setPrefWidth(tableWidth * 0.25);
            inicioColumn.setPrefWidth(tableWidth * 0.25);
            finColumn.setPrefWidth(tableWidth * 0.25);
            distanciaColumn.setPrefWidth(tableWidth * 0.25);
            tableView.getItems().addAll(RutasService.getRoutes());
        });
    }

    public void actualizarTabla() {
        tableView.getItems().clear();
        tableView.getItems().addAll(RutasService.getRoutes());
    }
    @FXML
    private void handleVolverAlMenu() throws IOException {
        App.setRoot("primary");
    }

    @FXML
private void handleCargarRutas(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    File file = fileChooser.showOpenDialog(null);
    if (file != null) {
        List<Rutas> rutas = new ArrayList<Rutas>();
        tableView.getItems().clear();

        try {
            CSVReader reader = new CSVReaderBuilder(new java.io.FileReader(file)).withSkipLines(1).build();
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                Rutas ruta = new Rutas(nextLine[0], nextLine[1], Float.parseFloat(nextLine[2]));

                rutas.add(ruta); // Corregido aquí

                RutasService.addRoute(ruta); // Asumiendo que quieres añadir 'ruta' a tu servicio

                tableView.getItems().add(ruta); // Asumiendo que quieres añadir 'ruta' a tu tableView
            }

            // Después de cargar los datos en la tabla, establece rutasCargadas a true
            EstadoGlobal estadoGlobal = EstadoGlobal.getInstance();
            estadoGlobal.setRutasCargadas(true);

        } catch (Exception e) {
            // Considera manejar esta excepción
        }
    }
}

    @FXML
    private void handleEditarRuta(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/melgar/vistaEditarRuta.fxml"));
            Parent root = fxmlLoader.load();
    
            // Obtén la referencia al controlador
            editarRutaController controller = fxmlLoader.getController();
            controller.setCargaDeRutasController(this);
    
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("No se puede cargar la vista vistaEditarRuta.fxml");
            e.printStackTrace();
        }
    }

}
