package com.melgar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.List;
import com.melgar.service.ViajesService;
import com.melgar.models.Viajes;

public class App extends Application {
    private ViajesService viajesService = ViajesService.getInstance();
    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setWidth(800); 
        stage.setHeight(600); 
        stage.show();
        File file = new File("viajes.bin");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Viajes> viajes = (List<Viajes>) ois.readObject();
                viajesService.setViajes(viajes);
                System.out.println("Viajes cargados al iniciar la aplicación: " + viajes);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}