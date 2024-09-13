package org.example.juego;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

import org.example.juego.modelo.modeloJuego;
import org.example.juego.service.Interface_juego;
import org.example.juego.service.juego_servicioImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class HelloController {
    //@Autowired
    public Interface_juego interfaceImJ=new juego_servicioImp();

    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    @FXML
    private Button btniniciar, btnanular;

    @FXML
    private Label nombreturno, punt_jugador1, punt_jugador2;

    @FXML
    private TextField nombrejugador1, nombrejugador2;

    @FXML
   private TableView tabla_puntajes;

    @FXML
    private TableColumn<modeloJuego, String> nom_partido, nom_jugador1, nom_jugador2, nom_ganador, puntuacion, cestado;

    private String jugadorActual;
    private boolean juegoActivo;
    private int contadorTurnos;
    private List<modeloJuego> resultadosPartidas = new ArrayList<>();
    private ObservableList<modeloJuego> ob_modeloJ = FXCollections.observableArrayList();


    private List<Button> tablero;
    String ganador;
    int puntuacionJugador1 = 0;
    int puntuacionJugador2 = 0;
    String estadoPartida;
    private int puntajeJugador1, puntajeJugador2;
    private int indexEdit = -1;

    @FXML
    public void initialize() {
        tablero = new ArrayList<>();
        tablero.add(btn1);
        tablero.add(btn2);
        tablero.add(btn3);
        tablero.add(btn4);
        tablero.add(btn5);
        tablero.add(btn6);
        tablero.add(btn7);
        tablero.add(btn8);
        tablero.add(btn9);

        desactivarBotones();

        for (Button btn : tablero) {
            btn.setOnAction(e -> marcarCasilla(btn));
        }

    }
    int num =1;

    @FXML
    private void iniciarJuego() {
        if (!nombrejugador1.getText().isEmpty() && !nombrejugador2.getText().isEmpty()) {
            jugadorActual = "X";
            nombreturno.setText(nombrejugador1.getText());
            juegoActivo = true;
            contadorTurnos = 0;
            activarBotones();
            btniniciar.setDisable(true);
            btnanular.setDisable(false);
            modeloJuego resultado=new modeloJuego(("Partida "+(num++)), nombrejugador1.getText(), nombrejugador2.getText(),
                    "", "",
                    "");
            //interfaceImJ=new juego_servicioImp();
            listaOper();

        }
    }

    @FXML
    private void anularJuego() {
        desactivarBotones();
        limpiarBotones();
        juegoActivo = false;
        nombreturno.setText("");
        guardar_en_tabla("Anulada");
        btniniciar.setDisable(false);
        btnanular.setDisable(true);
    }

    @FXML
    private void marcarCasilla(Button boton) {
        if (juegoActivo && boton.getText().isEmpty()) {
            boton.setText(jugadorActual);
            boton.setDisable(true);
            contadorTurnos++;
            if (verificarGanador()) {
                if (jugadorActual.equals("X")) {
                    puntajeJugador1++;
                    punt_jugador1.setText(String.valueOf(puntajeJugador1));
                    nombreturno.setText(nombrejugador1.getText() + "  ¡Ganaste!");

                } else {
                    puntajeJugador2++;
                    punt_jugador2.setText(String.valueOf(puntajeJugador2));
                    nombreturno.setText(nombrejugador2.getText() + "  ¡Ganaste!");
                }
                guardar_en_tabla("Terminada");
                desactivarBotones();

                juegoActivo = false;
            } else if (contadorTurnos == 9) {
                nombreturno.setText("Empate");
                guardar_en_tabla("Empate");
                juegoActivo = false;
            } else {
                cambiarTurno();
            }
        }
    }

    private void cambiarTurno() {
        if (jugadorActual.equals("X")) {
            jugadorActual = "O";
            nombreturno.setText(nombrejugador2.getText());
        } else {
            jugadorActual = "X";
            nombreturno.setText(nombrejugador1.getText());
        }
    }

    private boolean verificarGanador() {

        return (compararCasillas(btn1, btn2, btn3) || compararCasillas(btn4, btn5, btn6) || compararCasillas(btn7, btn8, btn9) ||
                compararCasillas(btn1, btn4, btn7) || compararCasillas(btn2, btn5, btn8) || compararCasillas(btn3, btn6, btn9) ||
                compararCasillas(btn1, btn5, btn9) || compararCasillas(btn3, btn5, btn7));
    }

    private boolean compararCasillas(Button b1, Button b2, Button b3) {
        return (!b1.getText().isEmpty() && b1.getText().equals(b2.getText()) && b1.getText().equals(b3.getText()));
    }

    private void desactivarBotones() {
        for (Button btn : tablero) {
            btn.setDisable(true);
        }
    }

    private void activarBotones() {
        for (Button btn : tablero) {
            btn.setDisable(false);
            btn.setText("");
        }
    }

    private void limpiarBotones() {
        for (Button btn : tablero) {
            btn.setText("");
        }
    }


    private void guardar_en_tabla(String estadoPartida) {


        if (estadoPartida.equals("Empate")) {
            ganador = "Empate";
        } else {
            ganador = jugadorActual.equals("X") ? nombrejugador1.getText() : nombrejugador2.getText();
            if (ganador.equals(nombrejugador1.getText())) {
                puntuacionJugador1 = 1;
            } else {
                puntuacionJugador2 = 1;
            }
        }


        modeloJuego resultado = new modeloJuego(
                "Partida " + (resultadosPartidas.size() + 1), nombrejugador1.getText(), nombrejugador2.getText(),
                ganador, puntuacionJugador1 + " - " + puntuacionJugador2,
                estadoPartida
        );

        resultadosPartidas.add(resultado);



        ob_modeloJ.add(resultado);

    }







    public void  listaOper(){
        //interfaceImJ=new juego_servicioImp();
        List<modeloJuego> lismo=interfaceImJ.obtenerResultados();
        for (modeloJuego to:lismo) {
            System.out.println(to.toString());
            System.out.println("Gasasas"+lismo.size());
        }

        nom_partido.setCellValueFactory(new PropertyValueFactory<modeloJuego,
                String>("nombrePartida"));
        nom_jugador1.setCellValueFactory(new PropertyValueFactory<modeloJuego,
                String>("nombreJugador1"));
        nom_jugador2.setCellValueFactory(new PropertyValueFactory<modeloJuego,
                String>("nombreJugador2"));
        nom_ganador.setCellValueFactory(new PropertyValueFactory<modeloJuego,
                String>("ganador"));
        puntuacion.setCellValueFactory(new PropertyValueFactory<modeloJuego,
                String>("puntuacion"));
        cestado.setCellValueFactory(new PropertyValueFactory<modeloJuego,
                String>("estado"));


        ob_modeloJ=FXCollections.observableArrayList(interfaceImJ.obtenerResultados());

        tabla_puntajes.setItems(ob_modeloJ);
        //tabla_puntajes.refresh();
    }

}







