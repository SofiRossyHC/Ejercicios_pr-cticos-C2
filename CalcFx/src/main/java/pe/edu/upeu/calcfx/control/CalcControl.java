package pe.edu.upeu.calcfx.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.calcfx.modelo.CalcTO;
import pe.edu.upeu.calcfx.servicio.CalcServiceI;

import java.util.List;

@Component
public class CalcControl {
    @Autowired
    CalcServiceI serviceI;
 @FXML
    TextField txtResultado;

 @FXML
    TableView tableView;
 @FXML
    TableColumn <CalcTO, String> cVal1, cVal2, cResult;
 @FXML
 TableColumn <CalcTO, Character > cOper;
 @FXML
 TableColumn<CalcTO ,Void> cOpc;
 @FXML
 Button btn8,btn9;
 public void ativa_des (Boolean ind){
     btn8.setDisable(ind);
     btn9.setDisable(ind);
 }
@FXML
public void iniciar(ActionEvent event) {
     ativa_des(true);
}
 private ObservableList<CalcTO> calcTOList;
 private int indexEdit=-1;



 @FXML
    public void accionButton(ActionEvent event){
     System.out.println("Holas");
     Button button =(Button) event.getSource();
     switch (button.getId()){
         case "btn1","btn2","btn3","btn4","btn5","btn6","btn7","btn8","btn9","btn0":{
             EscribirNumeros(button.getText());
         }break;
         case "btnsuma","btnresta","btnMult","btnDiv":{
             operador(button.getText());
         }break;
         case "txtResulatdo":{
             calcularResultado();
         }break;
         case "btnborrar":{
             txtResultado.clear();
         }
     }

 }
public void EscribirNumeros(String valor){
     txtResultado.appendText(valor);
}


public void operador(String valor){
     txtResultado.appendText(""+valor+"");
}

public void calcularResultado(){
     String[] valores=txtResultado.getText().split("");
     double val1=Double.parseDouble(String.valueOf(valores[0]));
     double val2=Double.parseDouble(String.valueOf(valores[2]));
     switch (valores[1]){
         case "+":{txtResultado.setText(String.valueOf(val1+val2));}break;
         case "-":{txtResultado.setText(String.valueOf(val1-val2));}break;
         case "*":{txtResultado.setText(String.valueOf(val1*val2));}break;
         case "/":{txtResultado.setText(String.valueOf(val1/val2));}break;

     }

    CalcTO to=new CalcTO();
    to.setNum1(String.valueOf(val1));
    to.setNum2(String.valueOf(val2));
    to.setOperador(valores[1].charAt(0));
    to.setResultado(String.valueOf(txtResultado.getText()));
    if(indexEdit!=-1){
        serviceI.actualizarResultados(to, indexEdit);
    }else{
        serviceI.guardarResultados(to);
    }
    indexEdit=-1;
    listaOper();

}

    // Acción para editar una operación
    private void editOperCalc(CalcTO cal, int index) {
    System.out.println("Editing: " + cal.getNum1() + " Index:"+index);
    txtResultado.setText(cal.getNum1()+" "+cal.getOperador()+" "+cal.getNum2());
    indexEdit=index;
    }
    private void deleteOperCalc(CalcTO cal,int index) {
        System.out.println("Deleting: " + cal.getNum2());
        serviceI.eliminarResultados(index);
        listaOper();
        //eliminar fila
        //tableView.getItems().remove(cal); // Elimina la operación del

    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<CalcTO, Void>, TableCell<CalcTO, Void>>
                cellFactory = param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.getStyleClass().setAll("btn", "btn-success");
                editButton.setOnAction(event -> {
                    CalcTO cal = getTableView().getItems().get(getIndex());
                    editOperCalc(cal, getIndex());
                });
                deleteButton.getStyleClass().setAll("btn", "btn-danger");
                deleteButton.setOnAction(event -> {
                    CalcTO cal = getTableView().getItems().get(getIndex());
                    deleteOperCalc(cal, getIndex());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(editButton, deleteButton);
                    buttons.setSpacing(10);
                    setGraphic(buttons);
                }
            }
        };
        cOpc.setCellFactory(cellFactory);
    }

        public void listaOper (){
            List<CalcTO> lista=serviceI.obtenerResultados();
            for (CalcTO to:lista) {
                System.out.println(to.toString());
            }
            //redimensionar columna,
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            // Vincular columnas con propiedades de CalcTO
            cVal1.setCellValueFactory(new PropertyValueFactory<CalcTO,
                    String>("num1"));
//cambiar en la misma celda y el combobox es para seleccionar que operador usaras
            cVal1.setCellFactory(TextFieldTableCell.<CalcTO>forTableColumn());
            cVal2.setCellValueFactory(new PropertyValueFactory<CalcTO,
                    String>("num2"));

            cVal2.setCellFactory(TextFieldTableCell.<CalcTO>forTableColumn());
            cOper.setCellValueFactory(new
                    PropertyValueFactory<>("operador"));
            cOper.setCellFactory(ComboBoxTableCell.<CalcTO,
                    Character>forTableColumn('+', '-', '/', '*'));
            cResult.setCellValueFactory(new PropertyValueFactory<CalcTO,
                    String>("resultado"));

            cResult.setCellFactory(TextFieldTableCell.<CalcTO>forTableColumn());
            // Agregar botones de eliminar y modificar
            addActionButtonsToTable();
            //calcTOList coloca en coleccion y lo llama en un observable arrays list,para que sea ctualice no segura
            calcTOList = FXCollections.observableArrayList(serviceI.obtenerResultados());
            // Asignar los datos al TableView
            AnchorPane.setLeftAnchor(tableView, 0.0);
            AnchorPane.setRightAnchor(tableView, 0.0);
//esto es para que lo redimensione.o sea darle tamaño
            cOper.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25)); // 25% del ancho total

            cResult.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25)); // 25% del ancho total

            cOpc.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
            tableView.setItems(calcTOList);
        }
    }











