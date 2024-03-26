package com.melgar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.melgar.models.Viajes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PrimaryController {
    private GenerarViajeController generarViajeController;
    private IniciarViajesController iniciarViajesController;
    // @FXML
    // public void initialize() {
    // FXMLLoader loader = new
    // FXMLLoader(getClass().getResource("/com/melgar/generarViaje.fxml"));
    // try {
    // loader.load();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // this.generarViajeController = loader.getController();

    // loader = new
    // FXMLLoader(getClass().getResource("/com/melgar/iniciarViajes.fxml"));
    // try {
    // loader.load();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // this.iniciarViajesController = loader.getController();
    // }
    // Métodos para establecer los controladores
    // public void setGenerarViajeController(GenerarViajeController
    // generarViajeController) {
    // this.generarViajeController = generarViajeController;
    // }

    // public void setIniciarViajesController(IniciarViajesController
    // iniciarViajesController) {
    // this.iniciarViajesController = iniciarViajesController;
    // }
    @FXML
    public void initialize() {
        this.iniciarViajesController = new IniciarViajesController();
        // Resto de tu código de inicialización...
    }
    public void iniciarViaje() throws IOException {
        cambiarIniciarViaje();
    }
    @FXML
    private void cambiarIniciarViaje() throws IOException {
        String inicio = "";
        String fin = "";
        String distancia = "";

        // Leer los valores de inicio y fin desde el archivo binario
        File file = new File("viajes.bin");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Viajes> viajesExistentes = (List<Viajes>) ois.readObject();
                if (!viajesExistentes.isEmpty()) {
                    Viajes ultimoViaje = viajesExistentes.get(viajesExistentes.size() - 1);
                    inicio = ultimoViaje.getInicio();
                    fin = ultimoViaje.getFin();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        final String inicioFinal = inicio;
        final String finFinal = fin;

        try (Stream<String> lines = Files
                .lines(Paths.get("C:\\Users\\melga\\OneDrive\\Documentos\\Practica2\\practica2\\test.csv"))) {
            Optional<String> line = lines
                    .filter(l -> l.startsWith(inicioFinal) && l.contains(finFinal))
                    .findFirst();

            if (line.isPresent()) {
                System.out.println("Line found: " + line.get()); // Imprimir la línea encontrada
                String[] parts = line.get().split(",");
                if (parts.length > 2) {
                    System.out.println("Third part: " + parts[2]); // Imprimir la tercera parte
                    distancia = parts[2].trim();
                } else {
                    System.out.println("Line does not have three parts"); // Imprimir un mensaje si la línea no tiene
                                                                          // tres partes
                }
            } else {
                System.out.println("No line found"); // Imprimir un mensaje si no se encontró ninguna línea
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.setRoot("iniciarViajes");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void cambiarCarga() throws IOException {
        App.setRoot("cargarRuta");
    }

    @FXML
    private void cambiarGeViaje() throws IOException {
        App.setRoot("generarViaje");
    }

}
