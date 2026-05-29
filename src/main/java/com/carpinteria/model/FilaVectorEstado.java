package com.carpinteria.model;

import java.util.List;
import java.util.ArrayList;

/**
 Representa una fila completa del vector de estados.
 Cada campo se corresponde con una columna de la planilla.
 */
public class FilaVectorEstado {

    // PRICIPAL
    private int iteracion;
    private String evento;
    private double reloj;
    private Integer idMueble;
    private Integer idProximoMueble;

    // --- LLEGADA ESTANDAR ---
    private Double rndEstandar;
    private Double tiempoEntreEstandar;
    private Double proximaLlegadaEstandar;

    // --- LLEGADA A MEDIDA ---
    private Double rndMedida;
    private Double tiempoEntreMedida;
    private Double proximaLlegadaMedida;

    // --- CARPINTERO 1 ---
    private String estadoCarpintero1;
    private Integer muebleCarpintero1;
    private Double rndFabCarpintero1;
    private Double tiempoFabCarpintero1;
    private Double finFabCarpintero1;
    private Double inicioBloqueoC1;
    private Double finBloqueoC1;

    // --- CARPINTERO 2 ---
    private String estadoCarpintero2;
    private Integer muebleCarpintero2;
    private Double rndFabCarpintero2;
    private Double tiempoFabCarpintero2;
    private Double finFabCarpintero2;
    private Double inicioBloqueoC2;
    private Double finBloqueoC2;

    // --- COLAS ---
    private int colaEstandarCantidad;
    private int colaMedidaCantidad;

    // --- AYUDANTE ---
    private String estadoAyudante;
    private Double rndBarnizado;
    private Double tiempoBarnizado;
    private Double finBarnizado;

    // --- ESTADISTICAS ---
    private int contadorMueblesEstandar;
    private double tiempoEsperaEstandarActual;   // espera del mueble actual
    private double acumEsperaEstandar;
    private int contadorMueblesMedida;
    private double tiempoEsperaMedidaActual;     // espera del mueble actual
    private double acumEsperaMedida;
    private double tiempoBloqueoActual;          // bloqueo en este evento
    private double acumTiempoBloqueo;            // acum total de ambos carpinteros
    private int mueblesEnSistemaAlFinal;         // solo se usa en la ultima fila

    // --- OBJETOS TEMPORALES (muebles vivos) ---
    private List<MuebleSnapshot> mueblesVivos;

    public FilaVectorEstado() {
        this.mueblesVivos = new ArrayList<>();
    }


    // Clase interna para snapshot de un mueble vivo

    public static class MuebleSnapshot {
        private int id;
        private String tipo;
        private String estado;
        private double tiempoLlegada;

        public MuebleSnapshot(int id, String tipo, String estado, double tiempoLlegada) {
            this.id = id;
            this.tipo = tipo;
            this.estado = estado;
            this.tiempoLlegada = tiempoLlegada;
        }

        public int getId() { return id; }
        public String getTipo() { return tipo; }
        public String getEstado() { return estado; }
        public double getTiempoLlegada() { return tiempoLlegada; }
    }

    public int getIteracion() {
        return iteracion;
    }

    public void setIteracion(int num) {
        this.iteracion = num;
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public double getReloj() { return reloj; }
    public void setReloj(double reloj) { this.reloj = reloj; }

    public Integer getIdMueble() { return idMueble; }
    public void setIdMueble(Integer idMueble) { this.idMueble = idMueble; }

    public Integer getIdProximoMueble() { return idProximoMueble; }
    public void setIdProximoMueble(Integer idProximoMueble) { this.idProximoMueble = idProximoMueble; }

    public Double getRndEstandar() { return rndEstandar; }
    public void setRndEstandar(Double rndEstandar) { this.rndEstandar = rndEstandar; }

    public Double getTiempoEntreEstandar() { return tiempoEntreEstandar; }
    public void setTiempoEntreEstandar(Double tiempoEntreEstandar) { this.tiempoEntreEstandar = tiempoEntreEstandar; }

    public Double getProximaLlegadaEstandar() { return proximaLlegadaEstandar; }
    public void setProximaLlegadaEstandar(Double proximaLlegadaEstandar) { this.proximaLlegadaEstandar = proximaLlegadaEstandar; }

    public Double getRndMedida() { return rndMedida; }
    public void setRndMedida(Double rndMedida) { this.rndMedida = rndMedida; }

    public Double getTiempoEntreMedida() { return tiempoEntreMedida; }
    public void setTiempoEntreMedida(Double tiempoEntreMedida) { this.tiempoEntreMedida = tiempoEntreMedida; }

    public Double getProximaLlegadaMedida() { return proximaLlegadaMedida; }
    public void setProximaLlegadaMedida(Double proximaLlegadaMedida) { this.proximaLlegadaMedida = proximaLlegadaMedida; }

    public String getEstadoCarpintero1() { return estadoCarpintero1; }
    public void setEstadoCarpintero1(String estadoCarpintero1) { this.estadoCarpintero1 = estadoCarpintero1; }

    public Integer getMuebleCarpintero1() { return muebleCarpintero1; }
    public void setMuebleCarpintero1(Integer muebleCarpintero1) { this.muebleCarpintero1 = muebleCarpintero1; }

    public Double getRndFabCarpintero1() { return rndFabCarpintero1; }
    public void setRndFabCarpintero1(Double rndFabCarpintero1) { this.rndFabCarpintero1 = rndFabCarpintero1; }

    public Double getTiempoFabCarpintero1() { return tiempoFabCarpintero1; }
    public void setTiempoFabCarpintero1(Double tiempoFabCarpintero1) { this.tiempoFabCarpintero1 = tiempoFabCarpintero1; }

    public Double getFinFabCarpintero1() { return finFabCarpintero1; }
    public void setFinFabCarpintero1(Double finFabCarpintero1) { this.finFabCarpintero1 = finFabCarpintero1; }

    public Double getInicioBloqueoC1() { return inicioBloqueoC1; }
    public void setInicioBloqueoC1(Double inicioBloqueoC1) { this.inicioBloqueoC1 = inicioBloqueoC1; }

    public Double getFinBloqueoC1() { return finBloqueoC1; }
    public void setFinBloqueoC1(Double finBloqueoC1) { this.finBloqueoC1 = finBloqueoC1; }

    public String getEstadoCarpintero2() { return estadoCarpintero2; }
    public void setEstadoCarpintero2(String estadoCarpintero2) { this.estadoCarpintero2 = estadoCarpintero2; }

    public Integer getMuebleCarpintero2() { return muebleCarpintero2; }
    public void setMuebleCarpintero2(Integer muebleCarpintero2) { this.muebleCarpintero2 = muebleCarpintero2; }

    public Double getRndFabCarpintero2() { return rndFabCarpintero2; }
    public void setRndFabCarpintero2(Double rndFabCarpintero2) { this.rndFabCarpintero2 = rndFabCarpintero2; }

    public Double getTiempoFabCarpintero2() { return tiempoFabCarpintero2; }
    public void setTiempoFabCarpintero2(Double tiempoFabCarpintero2) { this.tiempoFabCarpintero2 = tiempoFabCarpintero2; }

    public Double getFinFabCarpintero2() { return finFabCarpintero2; }
    public void setFinFabCarpintero2(Double finFabCarpintero2) { this.finFabCarpintero2 = finFabCarpintero2; }

    public Double getInicioBloqueoC2() { return inicioBloqueoC2; }
    public void setInicioBloqueoC2(Double inicioBloqueoC2) { this.inicioBloqueoC2 = inicioBloqueoC2; }

    public Double getFinBloqueoC2() { return finBloqueoC2; }
    public void setFinBloqueoC2(Double finBloqueoC2) { this.finBloqueoC2 = finBloqueoC2; }

    public int getColaEstandarCantidad() { return colaEstandarCantidad; }
    public void setColaEstandarCantidad(int colaEstandarCantidad) { this.colaEstandarCantidad = colaEstandarCantidad; }

    public int getColaMedidaCantidad() { return colaMedidaCantidad; }
    public void setColaMedidaCantidad(int colaMedidaCantidad) { this.colaMedidaCantidad = colaMedidaCantidad; }

    public String getEstadoAyudante() { return estadoAyudante; }
    public void setEstadoAyudante(String estadoAyudante) { this.estadoAyudante = estadoAyudante; }

    public Double getRndBarnizado() { return rndBarnizado; }
    public void setRndBarnizado(Double rndBarnizado) { this.rndBarnizado = rndBarnizado; }

    public Double getTiempoBarnizado() { return tiempoBarnizado; }
    public void setTiempoBarnizado(Double tiempoBarnizado) { this.tiempoBarnizado = tiempoBarnizado; }

    public Double getFinBarnizado() { return finBarnizado; }
    public void setFinBarnizado(Double finBarnizado) { this.finBarnizado = finBarnizado; }

    public int getContadorMueblesEstandar() { return contadorMueblesEstandar; }
    public void setContadorMueblesEstandar(int contadorMueblesEstandar) { this.contadorMueblesEstandar = contadorMueblesEstandar; }

    public double getTiempoEsperaEstandarActual() { return tiempoEsperaEstandarActual; }
    public void setTiempoEsperaEstandarActual(double tiempoEsperaEstandarActual) { this.tiempoEsperaEstandarActual = tiempoEsperaEstandarActual; }

    public double getAcumEsperaEstandar() { return acumEsperaEstandar; }
    public void setAcumEsperaEstandar(double acumEsperaEstandar) { this.acumEsperaEstandar = acumEsperaEstandar; }

    public int getContadorMueblesMedida() { return contadorMueblesMedida; }
    public void setContadorMueblesMedida(int contadorMueblesMedida) { this.contadorMueblesMedida = contadorMueblesMedida; }

    public double getTiempoEsperaMedidaActual() { return tiempoEsperaMedidaActual; }
    public void setTiempoEsperaMedidaActual(double tiempoEsperaMedidaActual) { this.tiempoEsperaMedidaActual = tiempoEsperaMedidaActual; }

    public double getAcumEsperaMedida() { return acumEsperaMedida; }
    public void setAcumEsperaMedida(double acumEsperaMedida) { this.acumEsperaMedida = acumEsperaMedida; }

    public double getTiempoBloqueoActual() { return tiempoBloqueoActual; }
    public void setTiempoBloqueoActual(double tiempoBloqueoActual) { this.tiempoBloqueoActual = tiempoBloqueoActual; }

    public double getAcumTiempoBloqueo() { return acumTiempoBloqueo; }
    public void setAcumTiempoBloqueo(double acumTiempoBloqueo) { this.acumTiempoBloqueo = acumTiempoBloqueo; }

    public int getMueblesEnSistemaAlFinal() { return mueblesEnSistemaAlFinal; }
    public void setMueblesEnSistemaAlFinal(int mueblesEnSistemaAlFinal) { this.mueblesEnSistemaAlFinal = mueblesEnSistemaAlFinal; }

    public List<MuebleSnapshot> getMueblesVivos() { return mueblesVivos; }
    public void setMueblesVivos(List<MuebleSnapshot> mueblesVivos) { this.mueblesVivos = mueblesVivos; }
}
