package com.melgar;

public class EstadoGlobal {
    private static EstadoGlobal instance = null;
    private boolean rutasCargadas = false;

    private EstadoGlobal() {}

    public static EstadoGlobal getInstance() {
        if (instance == null) {
            instance = new EstadoGlobal();
        }
        return instance;
    }

    public void setRutasCargadas(boolean rutasCargadas) {
        this.rutasCargadas = rutasCargadas;
    }

    public boolean getRutasCargadas() {
        return rutasCargadas;
    }
}