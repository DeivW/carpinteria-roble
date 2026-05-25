package com.carpinteria.controller;

import com.carpinteria.model.ParametrosSimulacion;
import com.carpinteria.model.ResultadoSimulacion;
import com.carpinteria.simulation.SimuladorCarpinteria;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SimulacionController {

    @PostMapping("/simular")
    public ResultadoSimulacion simular(@RequestBody ParametrosSimulacion params) {
        SimuladorCarpinteria simulador = new SimuladorCarpinteria();
        return simulador.simular(params);
    }
}
