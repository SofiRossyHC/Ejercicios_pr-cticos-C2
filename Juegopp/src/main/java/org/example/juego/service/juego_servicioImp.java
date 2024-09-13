package org.example.juego.service;

import org.example.juego.modelo.modeloJuego;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class juego_servicioImp implements Interface_juego {
    public List<modeloJuego> lismo=new ArrayList<>();
    @Override
    public void guardarResultados(modeloJuego to) {
        lismo.add(to);
    }

    @Override
    public List<modeloJuego> obtenerResultados() {
        return lismo;
    }

    @Override
    public void actualizarResultados(modeloJuego to, int index) {
    lismo.set(index ,to);
    }

    @Override
    public void eliminarResultados(int index) {
    lismo.remove(index);
    }
}
