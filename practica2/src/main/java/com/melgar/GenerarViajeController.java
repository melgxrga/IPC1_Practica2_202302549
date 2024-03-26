package com.melgar;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.melgar.models.Vehiculos;
import com.melgar.models.Viajes;
import com.melgar.service.VehiculosService;
import com.melgar.service.ViajesService;
import com.melgar.IniciarViajesController;

public class GenerarViajeController {
    private int contadorViajes = 0;
    private Set<String> vehiculosUsados = new HashSet<>();
    @FXML
    private ComboBox<Vehiculos> tipoTransporteComboBox;
    @FXML
    private ComboBox<String> puntoInicialComboBox, puntoFinalComboBox;
    private ViajesService viajesService;
    private VehiculosService vehiculosService;
    private IniciarViajesController iniciarViajesController;
    private boolean rutasCargadas = false;
    private List<String> inicios = new ArrayList<>();
    private List<String> fines = new ArrayList<>();
    private String distancia;
    List<String> ubicaciones = new ArrayList<>();
    public void cargarRutas() {
  
        leerRutasDesdeCSV("C:\\Users\\melga\\OneDrive\\Documentos\\Practica2\\practica2\\test.csv", ubicaciones);
        if (!ubicaciones.isEmpty()) {
            rutasCargadas = true;
        }
    }

    @FXML
    private void regresarMenu() throws IOException {
        App.setRoot("primary");
    }
    @FXML
    public void initialize() {
        File file = new File("viajes.bin");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Viajes> viajesExistentes = (List<Viajes>) ois.readObject();
                contadorViajes = viajesExistentes.size();
                for (Viajes viaje : viajesExistentes) {
                    vehiculosUsados.add(viaje.getNombreTransporte());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        cargarRutas();
        puntoInicialComboBox.setItems(FXCollections.observableArrayList(ubicaciones));
        puntoFinalComboBox.setItems(FXCollections.observableArrayList(ubicaciones));
        viajesService = ViajesService.getInstance();
        vehiculosService = new VehiculosService();
        List<Vehiculos> vehiculos = vehiculosService.getVehiculos();
        tipoTransporteComboBox.setItems(FXCollections.observableArrayList(vehiculos));
    }

    public String getInicio() {
        return puntoInicialComboBox.getValue();
    }
    public String getFin(){
        return puntoFinalComboBox.getValue();
    }

    public GenerarViajeController() {
        viajesService = ViajesService.getInstance();
    }

    @FXML
    private void handleAgregarViaje() {
        if (contadorViajes >= 3) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("No se pueden agregar más de 3 viajes.");
            alert.showAndWait();
            return;
        }
    
        Vehiculos vehiculoSeleccionado = tipoTransporteComboBox.getSelectionModel().getSelectedItem();
        String inicio = puntoInicialComboBox.getSelectionModel().getSelectedItem();
        String fin = puntoFinalComboBox.getSelectionModel().getSelectedItem();
        String distancia = "";
    
        try (Stream<String> lines = Files.lines(Paths.get("C:\\Users\\melga\\OneDrive\\Documentos\\Practica2\\practica2\\test.csv"))) {
            Optional<String> line = lines
                .filter(l -> l.startsWith(inicio) && l.contains(fin))
                .findFirst();
        
            if (line.isPresent()) {
                String[] parts = line.get().split(",");
                distancia = parts[2].trim(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (vehiculoSeleccionado != null && inicio != null && fin != null && !distancia.isEmpty()) {
            if (vehiculosUsados.contains(vehiculoSeleccionado.getNombre())) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("El tipo de vehículo ya ha sido usado para otro viaje.");
                alert.showAndWait();
                return;
            }
    
            Viajes nuevoViaje = new Viajes(inicio, fin, vehiculoSeleccionado.getNombre());
            nuevoViaje.iniciarViaje();
            viajesService.addViaje(nuevoViaje);
            vehiculosUsados.add(vehiculoSeleccionado.getNombre());
            contadorViajes++;
            System.out.println("Contador de viajes: " + contadorViajes);
    
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("El viaje se agregó con éxito.");
            alert.showAndWait();
    
            File file = new File("viajes.bin");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(viajesService.getViajes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("POR FAVOR LLENE TODOS LOS DATOS");
            alert.showAndWait();
        }
    }

    public void leerRutasDesdeCSV(String rutaArchivo, List<String> ubicaciones) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            br.readLine(); 
    
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    String inicio = partes[0];
                    String fin = partes[1];
    
                    if (!ubicaciones.contains(inicio)) {
                        ubicaciones.add(inicio);
                    }
    
                    if (!ubicaciones.contains(fin)) {
                        ubicaciones.add(fin);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
