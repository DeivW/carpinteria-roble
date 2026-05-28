package com.carpinteria.model;

// tiempoMaximo, iteracionesAMostrar, horaDesde, mediaEst, mediaMed, fabEstDesde, fabEstHasta, fabMedDesde, fabMedHasta, mediaBarn
public class ParametrosSimulacion {
    private double tiempoMaximo;      // X: tiempo máximo en minutos
    private int iteracionesAMostrar; // i: cuántas filas mostrar
    private double horaDesde;        // j: desde qué hora mostrar (en minutos)
    private double mediaEst;
    private double mediaMed;
    private double fabEstDesde;
    private double fabEstHasta;
    private double fabMedDesde;
    private double fabMedHasta;
    private double mediaBarn;


    public double getMediaEst() {
        return mediaEst;
    }

    public void setMediaEst(double mediaEst) {
        this.mediaEst = mediaEst;
    }

    public double getMediaMed() {
        return mediaMed;
    }

    public void setMediaMed(double mediaMed) {
        this.mediaMed = mediaMed;
    }

    public double getFabEstDesde() {
        return fabEstDesde;
    }

    public void setFabEstDesde(double fabEstDesde) {
        this.fabEstDesde = fabEstDesde;
    }

    public double getFabEstHasta() {
        return fabEstHasta;
    }

    public void setFabEstHasta(double fabEstHasta) {
        this.fabEstHasta = fabEstHasta;
    }

    public double getFabMedDesde() {
        return fabMedDesde;
    }

    public void setFabMedDesde(double fabMedDesde) {
        this.fabMedDesde = fabMedDesde;
    }

    public double getFabMedHasta() {
        return fabMedHasta;
    }

    public void setFabMedHasta(double fabMedHasta) {
        this.fabMedHasta = fabMedHasta;
    }

    public double getMediaBarn() {
        return mediaBarn;
    }

    public void setMediaBarn(double mediaBarn) {
        this.mediaBarn = mediaBarn;
    }
    public double getTiempoMaximo() { return tiempoMaximo; }
    public void setTiempoMaximo(double tiempoMaximo) { this.tiempoMaximo = tiempoMaximo; }

    public int getIteracionesAMostrar() { return iteracionesAMostrar; }
    public void setIteracionesAMostrar(int iteracionesAMostrar) { this.iteracionesAMostrar = iteracionesAMostrar; }

    public double getHoraDesde() { return horaDesde; }
    public void setHoraDesde(double horaDesde) { this.horaDesde = horaDesde; }
}
