package com.carpinteria.model;

import java.util.List;

public class ResultadoSimulacion {

    // Filas a mostrar (entre hora j y j+i iteraciones)
    private List<FilaVectorEstado> filasAMostrar;

    // Ultima fila (instante X, sin objetos temporales)
    private FilaVectorEstado ultimaFila;

    // Estadísticas finales
    private double promedioEsperaEstandar;
    private double promedioEsperaMedida;
    private double porcentajeBloqueoCarpintero1;
    private double porcentajeBloqueoCarpintero2;
    private int mueblesEnSistemaAlFinal;

    // Info de la simulación
    private int totalIteraciones;
    private double tiempoFinalSimulacion;

    // Getters y Setters
    public List<FilaVectorEstado> getFilasAMostrar() { return filasAMostrar; }
    public void setFilasAMostrar(List<FilaVectorEstado> filasAMostrar) { this.filasAMostrar = filasAMostrar; }

    public FilaVectorEstado getUltimaFila() { return ultimaFila; }
    public void setUltimaFila(FilaVectorEstado ultimaFila) { this.ultimaFila = ultimaFila; }

    public double getPromedioEsperaEstandar() { return promedioEsperaEstandar; }
    public void setPromedioEsperaEstandar(double promedioEsperaEstandar) { this.promedioEsperaEstandar = promedioEsperaEstandar; }

    public double getPromedioEsperaMedida() { return promedioEsperaMedida; }
    public void setPromedioEsperaMedida(double promedioEsperaMedida) { this.promedioEsperaMedida = promedioEsperaMedida; }

    public double getPorcentajeBloqueoCarpintero1() { return porcentajeBloqueoCarpintero1; }
    public void setPorcentajeBloqueoCarpintero1(double porcentajeBloqueoCarpintero1) { this.porcentajeBloqueoCarpintero1 = porcentajeBloqueoCarpintero1; }

    public double getPorcentajeBloqueoCarpintero2() { return porcentajeBloqueoCarpintero2; }
    public void setPorcentajeBloqueoCarpintero2(double porcentajeBloqueoCarpintero2) { this.porcentajeBloqueoCarpintero2 = porcentajeBloqueoCarpintero2; }

    public int getMueblesEnSistemaAlFinal() { return mueblesEnSistemaAlFinal; }
    public void setMueblesEnSistemaAlFinal(int mueblesEnSistemaAlFinal) { this.mueblesEnSistemaAlFinal = mueblesEnSistemaAlFinal; }

    public int getTotalIteraciones() { return totalIteraciones; }
    public void setTotalIteraciones(int totalIteraciones) { this.totalIteraciones = totalIteraciones; }

    public double getTiempoFinalSimulacion() { return tiempoFinalSimulacion; }
    public void setTiempoFinalSimulacion(double tiempoFinalSimulacion) { this.tiempoFinalSimulacion = tiempoFinalSimulacion; }
}
