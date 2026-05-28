package com.carpinteria.model;

public class Carpintero {

    public enum Estado { LIBRE, OCUPADO, BLOQUEADO }

    private int id;
    private Estado estado;
    private Mueble muebleActual;
    private double rndFabricacion;
    private double tiempoFabricacion;
    private double relojFinFabricacion;
    private double inicioBloqueo;
    private double finBloqueo;
    private double acumTiempoBloqueo;

    public Carpintero(int id) {
        this.id = id;
        this.estado = Estado.LIBRE;
        this.muebleActual = null;
        this.rndFabricacion = 0;
        this.tiempoFabricacion = 0;
        this.relojFinFabricacion = 0;
        this.inicioBloqueo = 0;
        this.finBloqueo = 0;
        this.acumTiempoBloqueo = 0;
    }

    public void iniciarFabricacion(Mueble mueble, double rnd, double tiempoFab, double relojActual) {
        this.estado = Estado.OCUPADO;
        this.muebleActual = mueble;
        this.rndFabricacion = rnd;
        this.tiempoFabricacion = tiempoFab;
        this.relojFinFabricacion = relojActual + tiempoFab;
    }

    public void bloquear(double relojActual) {
        this.estado = Estado.BLOQUEADO;
        this.inicioBloqueo = relojActual;
        this.finBloqueo = 0;
    }

    public void setInicioBloqueo(double inicioBloqueo) {
        this.inicioBloqueo = inicioBloqueo;
    }

    public void desbloquear(double relojActual) {
        this.finBloqueo = relojActual;
        this.acumTiempoBloqueo += (relojActual - this.inicioBloqueo);
        this.estado = Estado.LIBRE;
        this.muebleActual = null;
        this.rndFabricacion = 0;
        this.tiempoFabricacion = 0;
        this.relojFinFabricacion = 0;
        this.inicioBloqueo = 0;
    }

    public void liberarSinBloqueo() {
        this.estado = Estado.LIBRE;
        this.muebleActual = null;
        this.rndFabricacion = 0;
        this.tiempoFabricacion = 0;
        this.relojFinFabricacion = 0;
    }

    // Getters
    public int getId() { return id; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Mueble getMuebleActual() { return muebleActual; }
    public double getRndFabricacion() { return rndFabricacion; }
    public double getTiempoFabricacion() { return tiempoFabricacion; }
    public double getRelojFinFabricacion() { return relojFinFabricacion; }
    public void setRelojFinFabricacion(double t) { this.relojFinFabricacion = t; }
    public double getInicioBloqueo() { return inicioBloqueo; }
    public double getFinBloqueo() { return finBloqueo; }
    public double getAcumTiempoBloqueo() { return acumTiempoBloqueo; }

    public void setFinBloqueo(double finBloqueo) {
        this.finBloqueo = finBloqueo;
    }

    public String getEstadoStr() {
        switch (estado) {
            case LIBRE:     return "Libre";
            case OCUPADO:   return "Ocupado";
            case BLOQUEADO: return "Bloqueado";
            default: return "";
        }
    }
}
