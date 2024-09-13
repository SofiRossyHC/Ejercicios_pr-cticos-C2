package org.example.juego.service;

import org.example.juego.modelo.modeloJuego;

import java.util.List;

public interface Interface_juego {
    public void guardarResultados(modeloJuego to);//C
    public List<modeloJuego> obtenerResultados();//R
    public void actualizarResultados(modeloJuego to, int index);//U
    public void eliminarResultados(int index);//D



}
