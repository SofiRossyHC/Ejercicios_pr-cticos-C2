package org.example.juego;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.juego.modelo.modeloJuego;
import org.example.juego.service.Interface_juego;
import org.example.juego.service.juego_servicioImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelloController {

    @Autowired
    private Interface_juego interfaceImJ = new juego_servicioImp();

    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    @FXML
    private Button btniniciar, btnanular;

    @FXML
    private Label nombreturno, punt_jugador1, punt_jugador2;

    @FXML
    private TextField nombrejugador1, nombrejugador2;

    @FXML
    private TableView<modeloJuego> tabla_puntajes;

    @FXML
    private TableColumn<modeloJuego, String> nom_partido, nom_jugador1, nom_jugador2, nom_ganador, puntuacion, cestado;

    private String jugadorActual;
    private boolean juegoActivo;
    private int contadorTurnos;
    private List<modeloJuego> resultadosPartidas = new ArrayList<>();
    private ObservableList<modeloJuego> ob_modeloJ = FXCollections.observableArrayList();

    private List<Button> tablero;
    private String ganador;
    private int puntuacionJugador1 = 0;
    private int puntuacionJugador2 = 0;
    private int indexEdit = -1; // Variable para almacenar el índice de la fila en curso

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

        // Inicialización de la tabla
        inicializarTabla();
    }

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
            indexEdit = resultadosPartidas.size(); // Guardar el índice de la fila en curso
            estadoEnCurso();
        }
    }

    @FXML
    private void anularJuego() {
        desactivarBotones();
        limpiarBotones();
        juegoActivo = false;
        nombreturno.setText("");
        if (indexEdit >= 0) {
            resultadosPartidas.get(indexEdit).setEstado("Anulada");
            resultadosPartidas.get(indexEdit).setGanador("");
            resultadosPartidas.get(indexEdit).setPuntuacion("0 - 0");
            ob_modeloJ.set(indexEdit, resultadosPartidas.get(indexEdit)); // Actualizar la tabla
        }
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
                    puntuacionJugador1++;
                    punt_jugador1.setText(String.valueOf(puntuacionJugador1));
                    nombreturno.setText(nombrejugador1.getText() + " ¡Ganaste!");
                } else {
                    puntuacionJugador2++;
                    punt_jugador2.setText(String.valueOf(puntuacionJugador2));
                    nombreturno.setText(nombrejugador2.getText() + " ¡Ganaste!");
                }
                guardarEnTabla("Terminada");
                desactivarBotones();
                juegoActivo = false;
            } else if (contadorTurnos == 9) {
                nombreturno.setText("Empate");
                guardarEnTabla("Empate");
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

    private void guardarEnTabla(String estadoPartida) {
        if (estadoPartida.equals("Empate")) {
            ganador = "Empate";
        } else {
            ganador = jugadorActual.equals("X") ? nombrejugador1.getText() : nombrejugador2.getText();
        }

        modeloJuego resultadoFin = new modeloJuego(
                "Partida " + (resultadosPartidas.size() + 1), nombrejugador1.getText(), nombrejugador2.getText(),
                ganador, puntuacionJugador1 + " - " + puntuacionJugador2,
                estadoPartida
        );

        if (indexEdit >= 0) {
            resultadosPartidas.set(indexEdit, resultadoFin);
            ob_modeloJ.set(indexEdit, resultadoFin); // Actualizar la tabla
        } else {
            resultadosPartidas.add(resultadoFin);
            ob_modeloJ.add(resultadoFin);
        }
        tabla_puntajes.setItems(ob_modeloJ); // Actualiza la tabla
    }

    private void estadoEnCurso() {
        modeloJuego resultadoCurso = new modeloJuego(
                "Partida " + (resultadosPartidas.size()+1 ), nombrejugador1.getText(), nombrejugador2.getText(),
                "", "",
                "Jugando..."
        );
        resultadosPartidas.add(resultadoCurso);
        ob_modeloJ.add(resultadoCurso);
        tabla_puntajes.setItems(ob_modeloJ); // Actualiza la tabla
    }

    private void inicializarTabla() {
        nom_partido.setCellValueFactory(new PropertyValueFactory<>("nombrePartida"));
        nom_jugador1.setCellValueFactory(new PropertyValueFactory<>("nombreJugador1"));
        nom_jugador2.setCellValueFactory(new PropertyValueFactory<>("nombreJugador2"));
        nom_ganador.setCellValueFactory(new PropertyValueFactory<>("ganador"));
        puntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
        cestado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }
}
