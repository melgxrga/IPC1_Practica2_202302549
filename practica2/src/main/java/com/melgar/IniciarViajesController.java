package com.melgar;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.melgar.models.Vehiculos;
import com.melgar.models.Viajes;
import com.melgar.service.VehiculosService;
import com.melgar.service.ViajesService;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import com.melgar.service.ViajesService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class IniciarViajesController {
    private PrimaryController primaryController;
    private ViajesService viajesService;
    @FXML
    private Button recargar;
    @FXML
    private ListView<Viajes> listaViajes;
    @FXML
    private Button iniciarTodosLosViajes; // Asegúrate de conectar este botón en tu archivo FXML

    private VBox contenedor;
    private TranslateTransition tt;
    private AtomicReference<Float> capacidad;
    private Vehiculos vehiculoSeleccionado;
    private boolean gasolinaAgotada;
    private float distanciaRecorrida = 0.0f; // Inicializa la distancia recorrida
    private Label distanciaRecorridaLabel = new Label(); // Inicializa el label de la distancia recorrida
    private Map<String, Float> distanciasRecorridas = new HashMap<>();
    private Label capacidadLabel;
    private Label distanciaLabel;
    private List<TranslateTransition> transitions = new ArrayList<>();
    List<TranslateTransition> todasLasTransiciones = new ArrayList<>();
    private Map<String, AtomicReference<Float>> capacidades = new HashMap<>();

    @FXML
    public void initialize() {
        distanciaLabel = new Label();
        ViajeListCellFactory factory = new ViajeListCellFactory(distanciaLabel);
        listaViajes.setCellFactory(factory);
        VBox.setVgrow(listaViajes, Priority.ALWAYS);
        actualizarListaViajes();
        cargarDistanciasDesdeCSV(); // Llama al nuevo método aquí
        if (primaryController != null) {
            try {
                primaryController.iniciarViaje();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        iniciarTodosLosViajes.setOnAction(event -> iniciarTodosLosViajes());

        // Cargar el estado de los viajes desde el archivo
        // Cargar el estado de los viajes desde el archivo
        // Cargar el estado de los viajes desde el archivo
        File file = new File("estadoViajes.bin");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("estadoViajes.bin"))) {
                List<EstadoViaje> estadosViajeLeidos = (List<EstadoViaje>) ois.readObject();
            
                for (EstadoViaje estadoViaje : estadosViajeLeidos) {
                    System.out.println("Nombre del vehículo: " + estadoViaje.getNombreVehiculo());
                    System.out.println("Distancia: " + estadoViaje.getDistancia());
                    System.out.println("Capacidad de gasolina: " + estadoViaje.getCapacidad());
                    System.out.println("-----");
                    System.out.println(estadoViaje);
                    // Obtiene la TranslateTransition para este viaje
                    TranslateTransition tt = getTranslateTransitionForViaje(estadoViaje.getViaje());

                    // Asegúrate de que tt no es null antes de intentar acceder a sus métodos
                    if (tt != null && tt.getNode() != null) {
                        // Restaura el estado de las animaciones
                        tt.getNode().setTranslateX(estadoViaje.getAnimacionX());
                        tt.getNode().setTranslateY(estadoViaje.getAnimacionY());
                        tt.setDuration(estadoViaje.getAnimacionDuracion());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void verViajesIniciados() {
    try {
        // Carga la nueva vista
        Parent root = FXMLLoader.load(getClass().getResource("cargarTablaViajes.fxml"));

        // Crea una nueva escena y la muestra en una nueva ventana
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public IniciarViajesController() {
        // Inicializa tt en el constructor
        this.tt = new TranslateTransition();
    }

    private List<Button> todosLosBotonesIniciarViaje = new ArrayList<>();

    public void iniciarTodosLosViajes() {
        for (Button iniciarViaje : todosLosBotonesIniciarViaje) {
            iniciarViaje.fire();
        }
    }

    private Map<Viajes, TranslateTransition> viajeTransitions = new HashMap<>();

    public TranslateTransition getTranslateTransitionForViaje(Viajes viaje) {
        return viajeTransitions.get(viaje);
    }

    protected Timeline createTimeline(AtomicReference<Float> capacidad, Button recargar, float consumo,
            String nombreVehiculo, Label capacidadLabel, Label distanciaLabel, float velocidad) {
        return new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (capacidad.get() > 0) { // Solo realiza los cálculos si la capacidad de gasolina es mayor que 0
                float nuevaCapacidad = capacidad.get() - consumo;
                capacidad.set(Math.max(nuevaCapacidad, 0)); // Asegura que la capacidad no sea menor que 0

                // Verifica si distanciasRecorridas contiene la clave
                if (!distanciasRecorridas.containsKey(nombreVehiculo)) {
                    distanciasRecorridas.put(nombreVehiculo, 0f); // Agrega la clave con un valor inicial de 0
                }

                // Actualiza la distancia recorrida
                float distanciaRecorrida = distanciasRecorridas.get(nombreVehiculo) + velocidad;
                distanciasRecorridas.put(nombreVehiculo, distanciaRecorrida);
            } else {
                // Hace visible el botón de recarga
                recargar.setVisible(true);
            }

            // Establece el texto de las etiquetas
            capacidadLabel.setText("Capacidad de gasolina: " + capacidad.get());
            distanciaLabel.setText("Distancia recorrida: " + distanciasRecorridas.get(nombreVehiculo));
        }));
    }

    @FXML
    private Pane pane;

    private void iniciarViaje(Viajes viaje) {
        System.out.println("Iniciando el viaje: " + viaje); // Mensaje de depuración

        VehiculosService vehiculosService = new VehiculosService();
        List<Vehiculos> listaDeVehiculos = vehiculosService.getVehiculos();
        Map<String, Vehiculos> mapaDeVehiculos = new HashMap<>();
        for (Vehiculos vehiculo : listaDeVehiculos) {
            mapaDeVehiculos.put(vehiculo.getNombre(), vehiculo);
        }
        Vehiculos vehiculoSeleccionado = mapaDeVehiculos.get(viaje.getNombreTransporte());

        if (vehiculoSeleccionado != null) {
            float distancia = viaje.getDistancia(); // La distancia entre los puntos
            float consumo = vehiculoSeleccionado.getConsumo();

            System.out.println("Distancia: " + distancia); // Imprime la distancia
            System.out.println("Consumo: " + consumo); // Imprime el consumo

            float duracion = vehiculoSeleccionado.getCapacidad() / consumo;
            duracion = Math.max(duracion, 1);
            ImageView imageView = getImageViewForTransporte(viaje.getNombreTransporte());

            // Crea un AtomicReference para la capacidad de gasolina del vehículo
            AtomicReference<Float> capacidad = new AtomicReference<>(vehiculoSeleccionado.getCapacidad());

            // Inicializa la distancia recorrida para este vehículo si no existe
            distanciasRecorridas.putIfAbsent(vehiculoSeleccionado.getNombre(), 0f);
            float velocidad = vehiculoSeleccionado.getVelocidad(); // Obtiene la velocidad del vehículo
            // Crea el botón recargar aquí
            Button recargar = new Button("Recargar");
            recargar.setVisible(false); // inicialmente el botón no es visible

            // Crea un Timeline para hacer un seguimiento del consumo de gasolina
            Timeline timeline = createTimeline(capacidad, recargar, consumo, vehiculoSeleccionado.getNombre(),
                    new Label(), new Label(), velocidad);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    public void cargarDistanciasDesdeCSV() {
        String rutaArchivo = "C:\\Users\\melga\\OneDrive\\Documentos\\Practica2\\practica2\\test.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // Ignora la primera línea
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String inicio = partes[0].trim();
                    String fin = partes[1].trim();
                    float distancia = Float.parseFloat(partes[2].trim()); // Cambia a Float.parseFloat

                    for (Viajes viaje : listaViajes.getItems()) { // Itera sobre los viajes en la lista
                        if (viaje.getInicio().equals(inicio) && viaje.getFin().equals(fin)) {
                            viaje.setDistancia(distancia);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    private class ViajeListCellFactory implements Callback<ListView<Viajes>, ListCell<Viajes>> {
        private Label distanciaLabel;

        public ViajeListCellFactory(Label distanciaLabel) {
            this.distanciaLabel = distanciaLabel;
        }

        private Timeline createTimeline(AtomicReference<Float> capacidad, Button recargar, float consumo,
                String nombreVehiculo, Label capacidadLabel, Label distanciaLabel) {
            return new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (capacidad.get() > 0) { // Solo realiza los cálculos si la capacidad de gasolina es mayor que 0
                    float nuevaCapacidad = capacidad.get() - consumo;
                    capacidad.set(Math.max(nuevaCapacidad, 0)); // Asegura que la capacidad no sea menor que 0

                    // Verifica si distanciasRecorridas contiene la clave
                    if (!distanciasRecorridas.containsKey(nombreVehiculo)) {
                        distanciasRecorridas.put(nombreVehiculo, 0f); // Agrega la clave con un valor inicial de 0
                    }

                    // Actualiza la distancia recorrida
                    float distanciaRecorrida = distanciasRecorridas.get(nombreVehiculo) + consumo;
                    distanciasRecorridas.put(nombreVehiculo, distanciaRecorrida);

                    // Establece el texto de las etiquetas
                    capacidadLabel.setText("Capacidad de gasolina: " + capacidad.get());
                    distanciaLabel.setText("Distancia recorrida: " + distanciaRecorrida);
                } else {
                    // Hace visible el botón de recarga
                    recargar.setVisible(true);
                }
            })); // Cierra la llave de new KeyFrame
        } // Cierra la llave de createTimeline

        HashMap<String, AtomicReference<Float>> capacidades = new HashMap<>();
        HashMap<String, Float> distanciasRecorridas = new HashMap<>();

        @Override
        public ListCell<Viajes> call(ListView<Viajes> param) {
            VehiculosService vehiculosService = new VehiculosService();
            List<Vehiculos> listaDeVehiculos = vehiculosService.getVehiculos();

            Map<String, Vehiculos> mapaDeVehiculos = new HashMap<>();
            for (Vehiculos vehiculo : listaDeVehiculos) {
                mapaDeVehiculos.put(vehiculo.getNombre(), vehiculo);
            }
            return new ListCell<Viajes>() {
                HashMap<String, AtomicReference<Float>> capacidades = new HashMap<>();
                HashMap<String, Float> distanciasRecorridas = new HashMap<>();
                TranslateTransition tt = null;
                private ImageView imageView;
                Vehiculos vehiculoSeleccionado = null;

                @Override
                protected void updateItem(Viajes item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label inicio = new Label("Inicio: " + item.getInicio());
                        Label vehiculo = new Label("Vehículo: " + item.getNombreTransporte());
                        Label distanciaEntrePuntosLabel = new Label("Distancia Entre Puntos: " + item.getDistancia());

                        // Crea nuevas etiquetas para la capacidad de gasolina y la distancia recorrida
                        Label capacidadLabel = new Label("Capacidad de gasolina: 0");
                        Label distanciaLabel = new Label("Distancia recorrida: 0");

                        VBox detalles = new VBox(inicio, vehiculo, distanciaEntrePuntosLabel, capacidadLabel,
                                distanciaLabel);

                        Separator separator = new Separator();
                        Button iniciarViaje = new Button("Iniciar viaje");
                        todosLosBotonesIniciarViaje.add(iniciarViaje); // Agrega el botón a la lista

                        Label fin = new Label("Fin: " + item.getFin());
                        fin.setMaxWidth(Double.MAX_VALUE);
                        fin.setAlignment(Pos.CENTER_RIGHT);

                        // MOTOCICLETA
                        if (item.getNombreTransporte().startsWith("Motocicleta")) {
                            this.imageView = new ImageView(new Image(
                                    getClass().getResource("/com/melgar/images/motocicleta.png").toExternalForm()));
                        } else if (item.getNombreTransporte().startsWith("Vehículo Estándar")) {
                            this.imageView = new ImageView(new Image(
                                    getClass().getResource("/com/melgar/images/vehiculo.png").toExternalForm()));
                        } else {
                            this.imageView = new ImageView(new Image(
                                    getClass().getResource("/com/melgar/images/vehiculoPremiun.png").toExternalForm()));
                        }

                        // Crea un Pane para contener la ImageView
                        Pane imagePane = new Pane();
                        imagePane.setPrefSize(100, 100); // Establece el tamaño del Pane según tus necesidades

                        // Añade la ImageView al Pane
                        imagePane.getChildren().add(imageView);

                        Button recargar = new Button("Recargar");
                        recargar.setVisible(false); // inicialmente el botón no es visible

                        recargar.setOnAction(event -> {
                            if (vehiculoSeleccionado != null) {
                                // Restablece la capacidad de gasolina del vehículo
                                AtomicReference<Float> capacidad = capacidades.get(vehiculoSeleccionado.getNombre());
                                if (capacidad != null) {
                                    capacidad.set(vehiculoSeleccionado.getCapacidad());
                                    capacidadLabel.setText("Capacidad de gasolina: " + capacidad.get()); // Actualiza el
                                                                                                         // texto de la
                                                                                                         // etiqueta
                                }

                                // Reanuda la animación si está pausada
                                if (tt != null && tt.getStatus() == Animation.Status.PAUSED) {
                                    tt.play();
                                }

                                // Oculta el botón de recarga
                                recargar.setVisible(false);
                            }
                        });
                        iniciarViaje.setOnAction(event -> {
                            if (item != null) {
                                vehiculoSeleccionado = mapaDeVehiculos.get(item.getNombreTransporte());
                        
                                if (vehiculoSeleccionado != null) {
                                    float distancia = item.getDistancia(); // La distancia entre los puntos
                                    float consumo = vehiculoSeleccionado.getConsumo();
                        
                                    System.out.println("Distancia: " + distancia); // Imprime la distancia
                                    System.out.println("Consumo: " + consumo); // Imprime el consumo
                        
                                    float duracion = vehiculoSeleccionado.getCapacidad() / consumo;
                                    duracion = Math.max(duracion, 1);
                                    tt = new TranslateTransition(Duration.seconds(duracion), imageView);
                        
                                    // Añade la TranslateTransition a la lista
                                    todasLasTransiciones.add(tt);
                        
                                    // Inicia la animación
                                    tt.play();
                        
                                    // Crea un AtomicReference para la capacidad de gasolina del vehículo
                                    AtomicReference<Float> capacidad = capacidades.computeIfAbsent(vehiculoSeleccionado.getNombre(), k -> new AtomicReference<>(vehiculoSeleccionado.getCapacidad()));
                        
                                    // Inicializa la distancia recorrida para este vehículo si no existe
                                    distanciasRecorridas.putIfAbsent(vehiculoSeleccionado.getNombre(), 0f);
                        
                                    Timeline timeline = createTimeline(capacidad, recargar, consumo, vehiculoSeleccionado.getNombre(), capacidadLabel, distanciaLabel);
                                    timeline.setCycleCount(Animation.INDEFINITE);
                                    timeline.play();
                        
                                    // Crea un nuevo objeto EstadoViaje
                                    EstadoViaje estadoViaje = new EstadoViaje();
                                    estadoViaje.setNombreVehiculo(vehiculoSeleccionado.getNombre());
                        
                                    // Crea una lista para almacenar los objetos EstadoViaje
                                    List<EstadoViaje> estadosViaje = new ArrayList<>();
                        
                                    tt.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                                        double currentX = tt.getNode().getTranslateX();
                                        estadoViaje.setAnimacionX(currentX);
                                    
                                        // Actualiza la distancia y la capacidad en estadoViaje
                                        estadoViaje.setDistancia(distanciasRecorridas.get(vehiculoSeleccionado.getNombre()));
                                        estadoViaje.setCapacidad(capacidad.get());
                                    });
                                    
                                    tt.setOnFinished(e -> {
                                        estadoViaje.setAnimacionY(tt.getNode().getTranslateY());
                                        estadoViaje.setAnimacionDuracion(tt.getDuration());
                                    
                                        // Agrega estadoViaje a la lista
                                        estadosViaje.add(estadoViaje);
                                    
                                        // Escribe la lista de objetos EstadoViaje en el archivo
                                        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("estadoViajes.bin"))) {
                                            oos.writeObject(estadosViaje);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    });
                                }
                            }
                        });
                        VBox celda = new VBox(iniciarViaje, detalles, separator, imagePane, recargar); // Añade
                                                                                                       // imagePane en
                                                                                                       // lugar de
                                                                                                       // imageView
                        BorderPane borderPane = new BorderPane();
                        borderPane.setLeft(celda);
                        borderPane.setRight(fin);

                        setGraphic(borderPane);

                    } // Cierra la llave de else
                } // Cierra la llave de updateItem
            }; // Cierra la llave de new ListCell
        } // Cierra la llave de call
    } // Cierra la llave de ViajeListCellFactory

    private ImageView getImageViewForTransporte(String nombreTransporte) {
        ImageView imageView;
        if (nombreTransporte.startsWith("Motocicleta")) {
            imageView = new ImageView(
                    new Image(getClass().getResource("/com/melgar/images/motocicleta.png").toExternalForm()));
        } else if (nombreTransporte.startsWith("Vehículo Estándar")) {
            imageView = new ImageView(
                    new Image(getClass().getResource("/com/melgar/images/vehiculo.png").toExternalForm()));
        } else {
            imageView = new ImageView(
                    new Image(getClass().getResource("/com/melgar/images/vehiculoPremiun.png").toExternalForm()));
        }
        return imageView;
    }

    @FXML
    private void regresarMenu() throws IOException {
        App.setRoot("primary");
    }

    public void actualizarListaViajes() {
        if (viajesService == null) {
            viajesService = ViajesService.getInstance();
        }
        List<Viajes> viajes = viajesService.getViajes();
        System.out.println("Viajes cargados: " + viajes); // Línea de depuración
        List<Viajes> viajesIniciados = viajes.stream()
                .filter(Viajes::getStatus)
                .collect(Collectors.toList());
        listaViajes.getItems().setAll(viajesIniciados);
    }

    private String obtenerDistancia(String inicio, String fin) {
        String distancia = "";

        try (Stream<String> lines = Files
                .lines(Paths.get("C:\\Users\\melga\\OneDrive\\Documentos\\Practica2\\practica2\\test.csv"))) {
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

        return distancia;
    }
}