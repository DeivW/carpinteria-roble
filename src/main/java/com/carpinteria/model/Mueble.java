package com.carpinteria.model;

public class Mueble {

    public enum Tipo { ESTANDAR, A_MEDIDA }

    public enum Estado {
        ESPERANDO_CARPINTERO,
        SIENDO_FABRICADO,
        ESPERANDO_BARNIZ,
        SIENDO_BARNIZADO,
        TERMINADO
    }

    private int id;
    private Tipo tipo;
    private Estado estado;
    private double tiempoLlegada;
    private double tiempoInicioAtencion; // cuando un carpintero lo toma
    private double tiempoEsperaEnCola;   // tiempoInicioAtencion - tiempoLlegada
    private int jornadaIngreso;

    public Mueble(int id, Tipo tipo, double tiempoLlegada) {
        this.id = id;
        this.tipo = tipo;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoInicioAtencion = -1;
        this.tiempoEsperaEnCola = -1;
        this.estado = Estado.ESPERANDO_CARPINTERO;
    }

    // Getters y Setters
    public int getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public Estado getEstado() { return estado; }

    public int getJornadaIngreso() {
        return jornadaIngreso;
    }

    public void setJornadaIngreso(int jornadaIngreso) {
        this.jornadaIngreso = jornadaIngreso;
    }

    public void setEstado(Estado estado) { this.estado = estado; }
    public double getTiempoLlegada() { return tiempoLlegada; }
    public double getTiempoInicioAtencion() { return tiempoInicioAtencion; }
    public void setTiempoInicioAtencion(double t) {
        this.tiempoInicioAtencion = t;
        this.tiempoEsperaEnCola = t - this.tiempoLlegada;
    }
    public double getTiempoEsperaEnCola() { return tiempoEsperaEnCola; }

    public String getTipoStr() {
        return tipo == Tipo.ESTANDAR ? "Estandar" : "A Medida";
    }

    public String getEstadoStr() {
        switch (estado) {
            case ESPERANDO_CARPINTERO: return "Esperando carpintero";
            case SIENDO_FABRICADO:    return "Siendo fabricado";
            case ESPERANDO_BARNIZ:    return "Esperando barniz";
            case SIENDO_BARNIZADO:    return "Siendo barnizado";
            case TERMINADO:           return "Terminado";
            default: return "";
        }
    }
}
