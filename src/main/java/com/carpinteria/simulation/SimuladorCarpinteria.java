package com.carpinteria.simulation;

import com.carpinteria.model.*;
import com.carpinteria.model.FilaVectorEstado.MuebleSnapshot;

import java.util.*;

public class SimuladorCarpinteria {

    private static final int MAX_ITERACIONES = 100000;
    private static final double JORNADA = 480.0;// 8 horas en minutos
    private static final double DIA = 1440.0; // 24 horas

    // Estados de los servidores
    private Carpintero carpintero1;
    private Carpintero carpintero2;
    private Ayudante ayudante;

    // Listado de las filas a mostrar
    private List<FilaVectorEstado> filasAMostrar;

    // Dos colas separadas: prioridad a medida
    private Queue<Mueble> colaEstandar;
    private Queue<Mueble> colaMedida;

    // Cola de bloqueo: muebles esperando barniz (en orden de llegada al bloqueo)
    private Queue<Mueble> colaBarnizado;

    // Muebles vivos en el sistema (para objetos temporales)
    private Map<Integer, Mueble> mueblesVivos;

    // Tiempos de próximos eventos
    private double proximaLlegadaEstandar;
    private double proximaLlegadaMedida;
    private int proximoIdMueble;

    // Acumuladores
    private int numIteracion;
    private int contadorEstandar;
    private int contadorMedida;
    private double acumEsperaEstandar;
    private double acumEsperaMedida;
    private double acumBloqueoC1;
    private double acumBloqueoC2;

    //Primera fila
    private double primerRNDest;
    private double primerRNDmed;
    private double primerTiempoEst;
    private double primerTiempoMed;
    // Reloj
    private double reloj;
    private double tiempoMaximo;
    private double horaDesde;

    // Para la siguiente jornada
    private double inicioSiguienteJornada;
    private double finJornada;
    private int jornada;

    // PARAMETROS DE VARIABLES ALEATORIAS
    private double mediaEst;
    private double mediaMed;
    private double fabEstDesde;
    private double fabEstHasta;
    private double fabMedDesde;
    private double fabMedHasta;
    private double mediaBarn;

    private int iteracionesAMostrar;
    private FilaVectorEstado ultimaFila;

    // PARAMETROS: tiempoMaximo, iteracionesAMostrar, horaDesde, mediaEst, mediaMed, fabEstDesde, fabEstHasta, fabMedDesde, fabMedHasta, mediaBarn
    public ResultadoSimulacion simular(ParametrosSimulacion params) {
        tiempoMaximo = params.getTiempoMaximo();
        horaDesde = params.getHoraDesde();
        mediaEst = params.getMediaEst();
        mediaMed = params.getMediaMed();
        fabEstDesde = params.getFabEstDesde();
        fabEstHasta = params.getFabEstHasta();
        fabMedDesde = params.getFabMedDesde();
        fabMedHasta = params.getFabMedHasta();
        mediaBarn = params.getMediaBarn();

        if(params.getIteracionesAMostrar() <= 0){
            iteracionesAMostrar = 200;
        }
        else{ iteracionesAMostrar = params.getIteracionesAMostrar(); }


        // Inicializar todo
        inicializar();

        // Lista donde guardamos TODAS las filas generadas
        List<FilaVectorEstado> todasLasFilas = new ArrayList<>();


        // Generar fila inicial (reloj = 0)
        FilaVectorEstado filaInicial = generarFilaInicial();
        todasLasFilas.add(filaInicial);
        if(filasAMostrar.size()< iteracionesAMostrar && filaInicial.getReloj() >= horaDesde){
            filasAMostrar.add(filaInicial);
        }
        int iteraciones = 1;
        boolean jornada1Terminada = false;

        // Bucle principal
        while (iteraciones < MAX_ITERACIONES) {

            FilaVectorEstado anterior = null;
            FilaVectorEstado actual = null;
            // Determinar el próximo evento
            String proximoEvento = determinarProximoEvento();
            if (proximoEvento == null) break;

            // Avanzar el reloj
            double tiempoEvento = getTiempoProximoEvento(proximoEvento);
/*
            // Verificar fin de jornada
            double finJornada = inicioSiguienteJornada - JORNADA; 
            double finJornadaActual = finJornada + JORNADA;

            if (!jornada1Terminada && tiempoEvento > finJornadaActual) {
                // Procesar fin de jornada
                FilaVectorEstado filaFin = procesarFinJornada(finJornadaActual);
                todasLasFilas.add(filaFin);
                iteraciones++;
                jornada1Terminada = true;

                // Si el tiempo maximo ya paso, cortar
                if (finJornadaActual >= tiempoMaximo) break;
            }
*/
            // Si superamos el tiempo maximo, cortar
            if (tiempoEvento > tiempoMaximo) break;

            // Procesar el evento
            anterior = actual;
            actual = procesarEvento(proximoEvento, tiempoEvento);
            if(filasAMostrar.size() < iteracionesAMostrar+1 && actual.getReloj() > horaDesde){
                filasAMostrar.add(actual);
            }
            //todasLasFilas.add(actual);
            iteraciones++;
            ultimaFila = actual;
        }

        // Construir resultado
        return construirResultado(filasAMostrar, horaDesde, iteracionesAMostrar, tiempoMaximo);
    }

    private void inicializar() {
        reloj = 0;
        numIteracion = 0;
        carpintero1 = new Carpintero(1);
        carpintero2 = new Carpintero(2);
        ayudante = new Ayudante();
        colaEstandar = new LinkedList<>();
        colaMedida = new LinkedList<>();
        colaBarnizado = new LinkedList<>();
        mueblesVivos = new LinkedHashMap<>();
        proximoIdMueble = 1;
        contadorEstandar = 0;
        contadorMedida = 0;
        acumEsperaEstandar = 0;
        acumEsperaMedida = 0;
        acumBloqueoC1 = 0;
        acumBloqueoC2 = 0;
        finJornada = reloj + JORNADA;
        jornada = 1;
        filasAMostrar = new ArrayList<FilaVectorEstado>();

        // Generar primeras llegadas
        // ESTANDAR
        double rndEst = Math.random();
        primerRNDest = rndEst;
        double tiempoEst = generarExponencial(mediaEst, rndEst);
        primerTiempoEst = tiempoEst;

        proximaLlegadaEstandar = tiempoEst;
        // A MEDIDA
        double rndMed = Math.random();
        primerRNDmed = rndMed;
        double tiempoMed = generarExponencial(mediaMed, rndMed);
        primerTiempoMed = tiempoMed;
        proximaLlegadaMedida = tiempoMed;

        inicioSiguienteJornada = reloj + DIA;
    }

    private FilaVectorEstado generarFilaInicial() {
        FilaVectorEstado fila = new FilaVectorEstado();
        fila.setIteracion(numIteracion);
        fila.setRndEstandar(primerRNDest);
        fila.setRndMedida(primerRNDmed);
        fila.setEvento("inicio");
        fila.setReloj(0);
        fila.setIdMueble(null);
        fila.setIdProximoMueble(1);

        fila.setTiempoEntreEstandar(primerTiempoEst);
        fila.setProximaLlegadaEstandar(proximaLlegadaEstandar);

        fila.setTiempoEntreMedida(primerTiempoMed);
        fila.setProximaLlegadaMedida(proximaLlegadaMedida);

        copiarEstadoServidores(fila);
        copiarEstadisticasBloqueo(fila, 0,0, 0);
        copiarEstadisticasEspera(fila, 0, 0);
        copiarMueblesVivos(fila);
        return fila;
    }

    private String determinarProximoEvento() {
        double minTiempo = Double.MAX_VALUE;
        String evento = null;

        if (proximaLlegadaEstandar < minTiempo) {
            minTiempo = proximaLlegadaEstandar;
            evento = "llegada_estandar";
        }
        if (proximaLlegadaMedida < minTiempo) {
            minTiempo = proximaLlegadaMedida;
            evento = "llegada_medida";
        }
        if (carpintero1.getEstado() == Carpintero.Estado.OCUPADO && carpintero1.getRelojFinFabricacion() < minTiempo) {
            minTiempo = carpintero1.getRelojFinFabricacion();
            evento = "fin_fab_c1";
        }
        if (carpintero2.getEstado() == Carpintero.Estado.OCUPADO && carpintero2.getRelojFinFabricacion() < minTiempo) {
            minTiempo = carpintero2.getRelojFinFabricacion();
            evento = "fin_fab_c2";
        }
        if (ayudante.getEstado() == Ayudante.Estado.OCUPADO && ayudante.getRelojFinBarnizado() < minTiempo) {
            minTiempo = ayudante.getRelojFinBarnizado();
            evento = "fin_barniz";
        }
        if(finJornada < minTiempo){
            minTiempo = finJornada;
            evento = "fin_jornada";
        }
        return evento;
    }

    private double getTiempoProximoEvento(String evento) {
        switch (evento) {
            case "llegada_estandar": return proximaLlegadaEstandar;
            case "llegada_medida":   return proximaLlegadaMedida;
            case "fin_fab_c1":       return carpintero1.getRelojFinFabricacion();
            case "fin_fab_c2":       return carpintero2.getRelojFinFabricacion();
            case "fin_barniz":       return ayudante.getRelojFinBarnizado();
            case "fin_jornada":      return finJornada;
            default: return Double.MAX_VALUE;
        }
    }

    private FilaVectorEstado procesarEvento(String tipoEvento, double tiempoEvento) {
        reloj = tiempoEvento;
        FilaVectorEstado fila = new FilaVectorEstado();
        fila.setReloj(reloj);
        numIteracion++;
        fila.setIteracion(numIteracion);
        double bloqueoActualC1 = 0;
        double bloqueoActualC2 = 0;
        double esperaEstandarActual = 0;
        double esperaMedidaActual = 0;

        switch (tipoEvento) {
            case "llegada_estandar": {
                fila.setEvento("llegada mueble estandar");
                Mueble m = new Mueble(proximoIdMueble, Mueble.Tipo.ESTANDAR, reloj);
                m.setJornadaIngreso(jornada);
                fila.setIdMueble(proximoIdMueble);
                mueblesVivos.put(proximoIdMueble, m);
                contadorEstandar++;
                proximoIdMueble++;
                fila.setIdProximoMueble(proximoIdMueble);

                // PROXIMA LLEGADA
                double rnd = Math.random();
                double tiempo = generarExponencial(mediaEst, rnd);
                proximaLlegadaEstandar = reloj + tiempo;
                fila.setRndEstandar(rnd);
                fila.setTiempoEntreEstandar(tiempo);
                fila.setProximaLlegadaEstandar(proximaLlegadaEstandar);
                fila.setProximaLlegadaMedida(proximaLlegadaMedida);

                Carpintero libre = getCarpinteroLibre();
                if (libre != null) {
                    m.setEstado(Mueble.Estado.SIENDO_FABRICADO);
                    m.setTiempoInicioAtencion(reloj);
                    double rndFab = Math.random();
                    double tFab = generarUniforme(fabEstDesde, fabEstHasta, rndFab);
                    libre.iniciarFabricacion(m, rndFab, tFab, reloj);
                } else {
                    colaEstandar.add(m);
                }
                break;
            }
            case "llegada_medida": {
                fila.setEvento("llegada mueble a medida");
                Mueble m = new Mueble(proximoIdMueble, Mueble.Tipo.A_MEDIDA, reloj);
                m.setJornadaIngreso(jornada);
                fila.setIdMueble(proximoIdMueble);
                mueblesVivos.put(proximoIdMueble, m);
                contadorMedida++;
                proximoIdMueble++;
                fila.setIdProximoMueble(proximoIdMueble);

                // PROXIMA LLEGADA
                double rnd = Math.random();
                double tiempo = generarExponencial(mediaMed, rnd);
                proximaLlegadaMedida = reloj + tiempo;
                fila.setRndMedida(rnd);
                fila.setTiempoEntreMedida(tiempo);
                fila.setProximaLlegadaMedida(proximaLlegadaMedida);
                fila.setProximaLlegadaEstandar(proximaLlegadaEstandar);

                Carpintero libre = getCarpinteroLibre();
                if (libre != null) {
                    m.setEstado(Mueble.Estado.SIENDO_FABRICADO);
                    m.setTiempoInicioAtencion(reloj);
                    double rndFab = Math.random();
                    double tFab = generarUniforme(fabMedDesde, fabMedHasta, rndFab);
                    libre.iniciarFabricacion(m, rndFab, tFab, reloj);
                } else {
                    colaMedida.add(m);
                }
                break;
            }
            case "fin_fab_c1": {
                fila.setEvento("fin fabricacion car. 1");
                Mueble m = carpintero1.getMuebleActual();
                fila.setIdMueble(m.getId());

              /* double esperaActual = m.getTiempoEsperaEnCola();
                if (m.getTipo() == Mueble.Tipo.ESTANDAR) {
                    acumEsperaEstandar += esperaActual;
                    esperaEstandarActual = esperaActual; 
                } else {
                    acumEsperaMedida += esperaActual;
                    esperaMedidaActual = esperaActual;   
                }
*/
                if (ayudante.getEstado() == Ayudante.Estado.LIBRE) {
                    m.setEstado(Mueble.Estado.SIENDO_BARNIZADO);
                    double rndBarn = Math.random();
                    double tBarn = generarExponencial(mediaBarn, rndBarn);
                    ayudante.iniciarBarnizado(m, rndBarn, tBarn, reloj);
                    carpintero1.liberarSinBloqueo();
                    asignarSiguienteACarpintero(carpintero1, fila);
                } else {
                    m.setEstado(Mueble.Estado.ESPERANDO_BARNIZ);
                    colaBarnizado.add(m);
                    carpintero1.bloquear(reloj);
                    fila.setContadorMueblesEstandar(contadorEstandar);
                    fila.setAcumEsperaEstandar(acumEsperaEstandar);
                    fila.setContadorMueblesMedida(contadorMedida);
                    fila.setAcumEsperaMedida(acumEsperaMedida);
                }
                break;
            }
            case "fin_fab_c2": {
                fila.setEvento("fin fabricacion car. 2");
                Mueble m = carpintero2.getMuebleActual();
                fila.setIdMueble(m.getId());
/*
                double esperaActual = m.getTiempoEsperaEnCola();
                if (m.getTipo() == Mueble.Tipo.ESTANDAR) {
                    acumEsperaEstandar += esperaActual;
                    esperaEstandarActual = esperaActual; 
                } else {
                    acumEsperaMedida += esperaActual;
                    esperaMedidaActual = esperaActual;   
                }
*/
                if (ayudante.getEstado() == Ayudante.Estado.LIBRE) {
                    m.setEstado(Mueble.Estado.SIENDO_BARNIZADO);
                    double rndBarn = Math.random();
                    double tBarn = generarExponencial(mediaBarn, rndBarn);
                    ayudante.iniciarBarnizado(m, rndBarn, tBarn, reloj);
                    carpintero2.liberarSinBloqueo();
                    asignarSiguienteACarpintero(carpintero2, fila);
                } else {
                    m.setEstado(Mueble.Estado.ESPERANDO_BARNIZ);
                    colaBarnizado.add(m);
                    carpintero2.bloquear(reloj);
                    fila.setContadorMueblesEstandar(contadorEstandar);
                    fila.setAcumEsperaEstandar(acumEsperaEstandar);
                    fila.setContadorMueblesMedida(contadorMedida);
                    fila.setAcumEsperaMedida(acumEsperaMedida);
                }
                break;
            }
            case "fin_barniz": {
                fila.setEvento("fin barniz");
                Mueble mTerminado = ayudante.getMuebleActual();
                fila.setIdMueble(mTerminado.getId());

                mTerminado.setEstado(Mueble.Estado.TERMINADO);
                ayudante.liberar();

                if (!colaBarnizado.isEmpty()) {
                    Mueble siguiente = colaBarnizado.poll();
                    siguiente.setEstado(Mueble.Estado.SIENDO_BARNIZADO);
                    double rndBarn = Math.random();
                    double tBarn = generarExponencial(mediaBarn, rndBarn);
                    ayudante.iniciarBarnizado(siguiente, rndBarn, tBarn, reloj);

                    if (carpintero1.getEstado() == Carpintero.Estado.BLOQUEADO
                            && carpintero1.getMuebleActual() != null
                            && carpintero1.getMuebleActual().getId() == siguiente.getId()) {
                        bloqueoActualC1 = reloj - carpintero1.getInicioBloqueo();
                        acumBloqueoC1 += bloqueoActualC1;
                        carpintero1.desbloquear(reloj);
                        asignarSiguienteACarpintero(carpintero1, fila);
                    } else if (carpintero2.getEstado() == Carpintero.Estado.BLOQUEADO
                            && carpintero2.getMuebleActual() != null
                            && carpintero2.getMuebleActual().getId() == siguiente.getId()) {
                        bloqueoActualC2 = reloj - carpintero2.getInicioBloqueo();
                        acumBloqueoC2 += bloqueoActualC2;
                        carpintero2.desbloquear(reloj);
                        asignarSiguienteACarpintero(carpintero2, fila);
                    }
                }

                mueblesVivos.remove(mTerminado.getId());
                break;
            }
            case "fin_jornada": {
                fila = procesarFinJornada(finJornada);
                finJornada = finJornada + DIA;
                break;
            }

        }

        if (!tipoEvento.startsWith("llegada")) {
            fila.setProximaLlegadaEstandar(proximaLlegadaEstandar);
            fila.setProximaLlegadaMedida(proximaLlegadaMedida);
        }
        if (!tipoEvento.startsWith("fin_fab")) {
            fila.setContadorMueblesEstandar(contadorEstandar);
            fila.setAcumEsperaEstandar(acumEsperaEstandar);

            fila.setContadorMueblesMedida(contadorMedida);
            fila.setAcumEsperaMedida(acumEsperaMedida);
        }


        copiarEstadoServidores(fila);
        // CORRECCIÓN: Ahora pasa de manera segmentada cada métrica de espera a la fila
        copiarEstadisticasBloqueo(fila, bloqueoActualC1, bloqueoActualC2, mueblesVivos.size());
        copiarMueblesVivos(fila);
        return fila;
    }

    private FilaVectorEstado procesarFinJornada(double tiempoFin) {
        reloj = tiempoFin;
        FilaVectorEstado fila = new FilaVectorEstado();
        fila.setEvento("fin jornada");
        fila.setReloj(reloj);
        fila.setIdMueble(null);
        fila.setIteracion(jornada);

        int enSistema = mueblesVivos.size();

        if (carpintero1.getEstado() == Carpintero.Estado.BLOQUEADO) {
            double extra = reloj - carpintero1.getInicioBloqueo();
            carpintero1.setInicioBloqueo(carpintero1.getInicioBloqueo() + (DIA-JORNADA));
            //acumBloqueoC1 += extra;
        }
        if (carpintero2.getEstado() == Carpintero.Estado.BLOQUEADO) {
            double extra = reloj - carpintero2.getInicioBloqueo();
            carpintero2.setInicioBloqueo(carpintero2.getInicioBloqueo() + (DIA-JORNADA));
            //acumBloqueoC2 += extra;
        }
        jornada++;
        inicioSiguienteJornada = reloj + (JORNADA*2);
/*
        if (carpintero1.getEstado() == Carpintero.Estado.OCUPADO) {
            double resto = carpintero1.getRelojFinFabricacion() - reloj;
            carpintero1.setRelojFinFabricacion(inicioSiguienteJornada + resto);
        }
        if (carpintero2.getEstado() == Carpintero.Estado.OCUPADO) {
            double resto = carpintero2.getRelojFinFabricacion() - reloj;
            carpintero2.setRelojFinFabricacion(inicioSiguienteJornada + resto);
        }*/

        if (carpintero1.getEstado() == Carpintero.Estado.OCUPADO) {
            double resto = carpintero1.getRelojFinFabricacion();
            carpintero1.setRelojFinFabricacion(resto + (DIA-JORNADA));
        }
        if (carpintero2.getEstado() == Carpintero.Estado.OCUPADO) {
            double resto = carpintero2.getRelojFinFabricacion();
            carpintero2.setRelojFinFabricacion(resto+(DIA-JORNADA));
        }
        if (ayudante.getEstado() == Ayudante.Estado.OCUPADO) {
            double resto = ayudante.getRelojFinBarnizado();
            ayudante.setRelojFinBarnizado(resto + (DIA-JORNADA));
        }
        proximaLlegadaEstandar += DIA - JORNADA;
        proximaLlegadaMedida += DIA - JORNADA;

        copiarEstadoServidores(fila);
        copiarEstadisticasBloqueo(fila, 0,0, enSistema);
        fila.setIdMueble(((int) inicioSiguienteJornada));
        fila.setMueblesVivos(new ArrayList<>());
        return fila;
    }

    private void asignarSiguienteACarpintero(Carpintero c, FilaVectorEstado fila) {
        Mueble siguiente = null;
        double esperaEstandarActual = 0;
        double esperaMedidaActual = 0;
        if (!colaMedida.isEmpty()) {
            siguiente = colaMedida.poll();
        } else if (!colaEstandar.isEmpty()) {
            siguiente = colaEstandar.poll();
        }

        if (siguiente != null) {
            siguiente.setTiempoInicioAtencion(reloj);
            siguiente.setEstado(Mueble.Estado.SIENDO_FABRICADO);

            double rndFab = Math.random();
            double tFab;
            if (siguiente.getTipo() == Mueble.Tipo.ESTANDAR) {
                tFab = generarUniforme(fabEstDesde, fabEstHasta, rndFab);

                // Manejar en que jornada llego para el tiempo de espera
                if(siguiente.getJornadaIngreso() < jornada){
                    esperaEstandarActual = siguiente.getTiempoEsperaEnCola() - (960*(jornada-siguiente.getJornadaIngreso()));
                    acumEsperaEstandar += esperaEstandarActual;
                }else{
                    esperaEstandarActual = siguiente.getTiempoEsperaEnCola();
                    acumEsperaEstandar += esperaEstandarActual;}
            } else {
                tFab = generarUniforme(fabMedDesde, fabMedHasta, rndFab);
                if(siguiente.getJornadaIngreso() < jornada){
                    esperaMedidaActual = siguiente.getTiempoEsperaEnCola() - (960*(jornada-siguiente.getJornadaIngreso()));
                    acumEsperaMedida += esperaMedidaActual;
                }else{
                    esperaMedidaActual = siguiente.getTiempoEsperaEnCola();
                    acumEsperaMedida += esperaMedidaActual;}

            }
            c.iniciarFabricacion(siguiente, rndFab, tFab, reloj);
            copiarEstadisticasEspera(fila, esperaEstandarActual, esperaMedidaActual);

        }
        else{
            copiarEstadisticasEspera(fila, esperaEstandarActual, esperaMedidaActual);
        }
    }

    private Carpintero getCarpinteroLibre() {
        if (carpintero1.getEstado() == Carpintero.Estado.LIBRE) return carpintero1;
        if (carpintero2.getEstado() == Carpintero.Estado.LIBRE) return carpintero2;
        return null;
    }

    private void copiarEstadoServidores(FilaVectorEstado fila) {
        // Carpintero 1
        fila.setEstadoCarpintero1(carpintero1.getEstadoStr());
        fila.setMuebleCarpintero1(carpintero1.getMuebleActual() != null ? carpintero1.getMuebleActual().getId() : null);
        fila.setRndFabCarpintero1(carpintero1.getEstado() == Carpintero.Estado.OCUPADO ? carpintero1.getRndFabricacion() : null);
        fila.setTiempoFabCarpintero1(carpintero1.getEstado() == Carpintero.Estado.OCUPADO ? carpintero1.getTiempoFabricacion() : null);
        fila.setFinFabCarpintero1(carpintero1.getEstado() == Carpintero.Estado.OCUPADO ? carpintero1.getRelojFinFabricacion() : null);
        fila.setInicioBloqueoC1(carpintero1.getEstado() == Carpintero.Estado.BLOQUEADO ? carpintero1.getInicioBloqueo() : null);
       // fila.setFinBloqueoC1(carpintero1.getFinBloqueo() > 0 ? carpintero1.getFinBloqueo() : null);
        if(carpintero1.getFinBloqueo() > 0 && (carpintero1.getEstado() != Carpintero.Estado.BLOQUEADO)){
            fila.setFinBloqueoC1(carpintero1.getFinBloqueo());
            carpintero1.setFinBloqueo(0);
        }

        // Carpintero 2
        fila.setEstadoCarpintero2(carpintero2.getEstadoStr());
        fila.setMuebleCarpintero2(carpintero2.getMuebleActual() != null ? carpintero2.getMuebleActual().getId() : null);
        fila.setRndFabCarpintero2(carpintero2.getEstado() == Carpintero.Estado.OCUPADO ? carpintero2.getRndFabricacion() : null);
        fila.setTiempoFabCarpintero2(carpintero2.getEstado() == Carpintero.Estado.OCUPADO ? carpintero2.getTiempoFabricacion() : null);
        fila.setFinFabCarpintero2(carpintero2.getEstado() == Carpintero.Estado.OCUPADO ? carpintero2.getRelojFinFabricacion() : null);
        fila.setInicioBloqueoC2(carpintero2.getEstado() == Carpintero.Estado.BLOQUEADO ? carpintero2.getInicioBloqueo() : null);
        //fila.setFinBloqueoC2(carpintero2.getFinBloqueo() > 0 ? carpintero2.getFinBloqueo() : null);
        if(carpintero2.getFinBloqueo() > 0 && (carpintero2.getEstado() != Carpintero.Estado.BLOQUEADO)){
            fila.setFinBloqueoC2(carpintero2.getFinBloqueo());
            carpintero2.setFinBloqueo(0);
        }


        // Colas
        fila.setColaEstandarCantidad(colaEstandar.size());
        fila.setColaMedidaCantidad(colaMedida.size());

        // Ayudante
        fila.setEstadoAyudante(ayudante.getEstadoStr());
        fila.setRndBarnizado(ayudante.getEstado() == Ayudante.Estado.OCUPADO ? ayudante.getRndBarnizado() : null);
        fila.setTiempoBarnizado(ayudante.getEstado() == Ayudante.Estado.OCUPADO ? ayudante.getTiempoBarnizado() : null);
        fila.setFinBarnizado(ayudante.getEstado() == Ayudante.Estado.OCUPADO ? ayudante.getRelojFinBarnizado() : null);
    }
    private void copiarEstadisticasEspera(FilaVectorEstado fila, double esperaEst, double esperaMed){
        fila.setContadorMueblesEstandar(contadorEstandar);
        fila.setTiempoEsperaEstandarActual(esperaEst);
        fila.setAcumEsperaEstandar(acumEsperaEstandar);

        fila.setContadorMueblesMedida(contadorMedida);
        fila.setTiempoEsperaMedidaActual(esperaMed);
        fila.setAcumEsperaMedida(acumEsperaMedida);


    }
    private void copiarEstadisticasBloqueo(FilaVectorEstado fila, double bloqueoActualC1, double bloqueoActualC2, int mueblesAlFinal) {
        fila.setTiempoBloqueoActualC1(bloqueoActualC1);
        fila.setTiempoBloqueoActualC2(bloqueoActualC2);
        fila.setAcumTiempoBloqueo(acumBloqueoC1 + acumBloqueoC2);
        fila.setAcumTiempoBloqueoC1(acumBloqueoC1);
        fila.setAcumTiempoBloqueoC2(acumBloqueoC2);
        fila.setMueblesEnSistemaAlFinal(mueblesAlFinal);
    }


    private void copiarMueblesVivos(FilaVectorEstado fila) {
        List<MuebleSnapshot> snapshots = new ArrayList<>();
        for (Mueble m : mueblesVivos.values()) {
            snapshots.add(new MuebleSnapshot(
                m.getId(),
                m.getTipoStr(),
                m.getEstadoStr(),
                m.getTiempoLlegada()
            ));
        }
        fila.setMueblesVivos(snapshots);
    }

    private ResultadoSimulacion construirResultado(List<FilaVectorEstado> todasLasFilas,
                                                    double horaDesde, int iteracionesAMostrar,
                                                    double tiempoMaximo) {
        ResultadoSimulacion resultado = new ResultadoSimulacion();

        /*List<FilaVectorEstado> filasAMostrar = new ArrayList<>();
        int count = 0;
        for (FilaVectorEstado f : todasLasFilas) {
            if (f.getReloj() >= horaDesde && count < iteracionesAMostrar) {
                filasAMostrar.add(f);
                count++;
            }
        }*/
        resultado.setFilasAMostrar(filasAMostrar);

        //FilaVectorEstado ultimaFila = todasLasFilas.isEmpty() ? null : todasLasFilas.get(todasLasFilas.size() - 1);
        resultado.setUltimaFila(ultimaFila);

        resultado.setPromedioEsperaEstandar(contadorEstandar > 0 ? acumEsperaEstandar / contadorEstandar : 0);
        resultado.setPromedioEsperaMedida(contadorMedida > 0 ? acumEsperaMedida / contadorMedida : 0);
        
        // El porcentaje de bloqueo se calcula sobre el reloj total simulado para reflejar la realidad del histórico
        double tiempoTotalSimulado = reloj > 0 ? reloj : 1.0;
        resultado.setPorcentajeBloqueoCarpintero1((acumBloqueoC1 / tiempoTotalSimulado) * 100);
        resultado.setPorcentajeBloqueoCarpintero2((acumBloqueoC2 / tiempoTotalSimulado) * 100);
        
        resultado.setMueblesEnSistemaAlFinal(mueblesVivos.size());
        resultado.setTotalIteraciones(numIteracion);
        resultado.setTiempoFinalSimulacion(reloj);

        return resultado;
    }

    private double generarExponencial(double media, double rnd) {
        if (rnd <= 0) rnd = 0.0001;
        if (rnd >= 1) rnd = 0.9999;
        return -media * Math.log(1 - rnd);
    }

    private double generarUniforme(double a, double b, double rnd) {
        return a + (b - a) * rnd;
    }
}