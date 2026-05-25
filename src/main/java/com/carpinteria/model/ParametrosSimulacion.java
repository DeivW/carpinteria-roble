package com.carpinteria.model;

public class ParametrosSimulacion {
    private double tiempoMaximo;      // X: tiempo máximo en minutos
    private int iteracionesAMostrar; // i: cuántas filas mostrar
    private double horaDesde;        // j: desde qué hora mostrar (en minutos)

    public double getTiempoMaximo() { return tiempoMaximo; }
    public void setTiempoMaximo(double tiempoMaximo) { this.tiempoMaximo = tiempoMaximo; }

    public int getIteracionesAMostrar() { return iteracionesAMostrar; }
    public void setIteracionesAMostrar(int iteracionesAMostrar) { this.iteracionesAMostrar = iteracionesAMostrar; }

    public double getHoraDesde() { return horaDesde; }
    public void setHoraDesde(double horaDesde) { this.horaDesde = horaDesde; }
}
