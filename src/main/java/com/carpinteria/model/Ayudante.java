package com.carpinteria.model;

public class Ayudante {

    public enum Estado { LIBRE, OCUPADO }

    private Estado estado;
    private Mueble muebleActual;
    private double rndBarnizado;
    private double tiempoBarnizado;
    private double relojFinBarnizado;

    public Ayudante() {
        this.estado = Estado.LIBRE;
        this.muebleActual = null;
        this.rndBarnizado = 0;
        this.tiempoBarnizado = 0;
        this.relojFinBarnizado = 0;
    }

    public void iniciarBarnizado(Mueble mueble, double rnd, double tiempoBarn, double relojActual) {
        this.estado = Estado.OCUPADO;
        this.muebleActual = mueble;
        this.rndBarnizado = rnd;
        this.tiempoBarnizado = tiempoBarn;
        this.relojFinBarnizado = relojActual + tiempoBarn;
    }

    public void liberar() {
        this.estado = Estado.LIBRE;
        this.muebleActual = null;
        this.rndBarnizado = 0;
        this.tiempoBarnizado = 0;
        this.relojFinBarnizado = 0;
    }

    // Getters
    public Estado getEstado() { return estado; }
    public Mueble getMuebleActual() { return muebleActual; }
    public double getRndBarnizado() { return rndBarnizado; }
    public double getTiempoBarnizado() { return tiempoBarnizado; }
    public double getRelojFinBarnizado() { return relojFinBarnizado; }

    public String getEstadoStr() {
        return estado == Estado.LIBRE ? "Libre" : "Ocupado";
    }
}
