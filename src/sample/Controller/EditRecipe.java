package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.Main;
import sample.Model.Ingrediente;
import sample.Model.Observacion;
import sample.Model.Preparacion;
import sample.Model.Receta;
import sample.Model.TableModel.IngredienteTable;
import sample.Model.TableModel.ObservacionTable;
import sample.Model.TableModel.PreparacionTable;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class EditRecipe implements Initializable {
    private Receta receta;
    public Label labelNombreReceta;
    public HBox hboxNombreReceta;
    public TextField recipeName;
    public TableView tableIngredientes;
    public TableColumn columnNombre;
    public TableColumn columnCantidad;
    public TableColumn columnDeleteIngrediente;
    public TextField nameIngrediente;
    public TextField cantidadIngrediente;
    public Button btnIngredientes;
    public TableView tablePreparacion;
    public TableColumn columnPasos;
    public TableColumn columnDeletePreparacion;
    public TextArea taPreparacion;
    public Button btnPreparacion;
    public TableView tableObservacion;
    public TableColumn columnObs;
    public TableColumn columnDeleteObservacion;
    public TextArea taObservacion;
    public Button btnObservacion;
    public Button buttonChangeName;

    private IngredienteTable editIngrediente;
    private PreparacionTable editPreparacion;
    private ObservacionTable editObservacion;

    private boolean edit =false;

    ObservableList<IngredienteTable> itemsIngredientes = FXCollections.observableArrayList();
    ObservableList<PreparacionTable> itemsPreparacion = FXCollections.observableArrayList();
    ObservableList<ObservacionTable> itemsObs = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        receta = Main.getEditableReceta();

        //TableView Ingredientes
        tableIngredientes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IngredienteTable>() {
            @Override
            public void changed(ObservableValue<? extends IngredienteTable> observable, IngredienteTable oldValue, IngredienteTable newValue) {
                if(newValue != null) {//Comprobamos que el Listener sea ejecutado cuando seleccionamos un elemento y NO cuando DESELECCIONEMOS uno.
                    cleanFields();
                    unSelectItems(tablePreparacion, tableObservacion);
                    tableIngredientes.getSelectionModel().select(newValue);
                    nameIngrediente.setText(newValue.getNombre());
                    cantidadIngrediente.setText(newValue.getCantidad());
                    btnIngredientes.setText("Guardar Cambios");
                    editIngrediente = newValue;
                }
            }
        });
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnDeleteIngrediente.setCellValueFactory(new PropertyValueFactory<>("btn"));

        //TableView Preparacion
        tablePreparacion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PreparacionTable>() {
            @Override
            public void changed(ObservableValue<? extends PreparacionTable> observable, PreparacionTable oldValue, PreparacionTable newValue) {
                if(newValue != null) { //Comprobamos que el Listener sea ejecutado cuando seleccionamos un elemento y NO cuando DESELECCIONEMOS uno.
                    cleanFields();
                    unSelectItems(tableIngredientes, tableObservacion);
                    tablePreparacion.getSelectionModel().select(newValue);
                    taPreparacion.setText(newValue.getPaso());
                    btnPreparacion.setText("Guardar Cambios");
                    editPreparacion = newValue;
                }
            }
        });
        columnPasos.setCellValueFactory(new PropertyValueFactory<>("paso"));
        columnDeletePreparacion.setCellValueFactory(new PropertyValueFactory<>("btn"));

        //TableView Observacion
        tableObservacion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ObservacionTable>() {
            @Override
            public void changed(ObservableValue<? extends ObservacionTable> observable, ObservacionTable oldValue, ObservacionTable newValue) {
                if(newValue != null) {//Comprobamos que el Listener sea ejecutado cuando seleccionamos un elemento y NO cuando DESELECCIONEMOS uno.
                    cleanFields();
                    unSelectItems(tableIngredientes, tablePreparacion);
                    tableObservacion.getSelectionModel().select(newValue);
                    taObservacion.setText(newValue.getObs());
                    btnObservacion.setText("Guardar Cambios");
                    editObservacion = newValue;
                }
            }
        });
        columnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
        columnDeleteObservacion.setCellValueFactory(new PropertyValueFactory<>("btn"));

        if(receta != null){
            edit = true;

            labelNombreReceta.setText(receta.getNombre());

            //rellenamos la table ingredientes con datos
            for(Ingrediente i : receta.getIngredientes()){
                addIngrediente(i, false);
            }
            //rellenamos la table preparacion con datos
            for(Preparacion i : receta.getPreparacion()){
                addPreparacion(i, false);
            }
            //rellenamos la table observacion con datos
            for(Observacion i : receta.getObservacion()){
                addObservacion(i,false);
            }
        }else{
            receta = new Receta();
        }
    }

    public void confirmarNombreReceta(ActionEvent actionEvent) {
        if(recipeName.getText().trim().length()==0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Mensaje de Error!");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, Debes escribir el nombre de la Receta para cambiarlo!");

            alert.showAndWait();
        }else{
            receta.setNombre(recipeName.getText());
            labelNombreReceta.setText(recipeName.getText());
            displayChangeName();
        }
    }

    public void confirmarIngredientes(ActionEvent actionEvent) {
        if(nameIngrediente.getText().trim().length()==0 || cantidadIngrediente.getText().trim().length()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mensaje de Error!");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, Debes escribir el nombre del ingrediente y sus respectiva cantidad para agregarlo!");

            alert.showAndWait();
        }else{
            Ingrediente i = new Ingrediente( nameIngrediente.getText() , cantidadIngrediente.getText());

            if (editIngrediente == null){
                addIngrediente(i,false);
            } else{
                addIngrediente(i, true);
            }
            cleanFields();
        }

    }

    public void confirmarPreparacion(ActionEvent actionEvent) {
        if(taPreparacion.getText().trim().length()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mensaje de Error!");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, Debes escribir el paso antes de agregarlo!");

            alert.showAndWait();
        }else{
            Preparacion p = new Preparacion(taPreparacion.getText());

            if (editPreparacion == null) {
                addPreparacion(p, false);
            }else{
                addPreparacion(p, true);
            }
            cleanFields();
        }
    }

    public void confirmarObservacion(ActionEvent actionEvent) {
        if(taObservacion.getText().trim().length()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mensaje de Error!");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, Debes escribir la observacion antes de agregarla!");

            alert.showAndWait();
        }else{
            Observacion o = new Observacion(taObservacion.getText());
            if(editObservacion == null){
                addObservacion(o, false);
            }else{
                addObservacion(o, true);
            }
            cleanFields();
        }
    }

    public void displayChangeNameButton(ActionEvent actionEvent) {
        cleanFields();
        unSelectItems();
        displayChangeName();
    }

    private void addIngrediente(Ingrediente i, boolean edit){
        final IngredienteTable it = new IngredienteTable(i);
        it.getBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemsIngredientes.remove(it);
            }
        });

        if(!edit) {
            itemsIngredientes.add(it);
            tableIngredientes.setItems(itemsIngredientes);
        }else{
            itemsIngredientes.add( itemsIngredientes.indexOf(editIngrediente), it );
            itemsIngredientes.remove(editIngrediente);
            editIngrediente = null;
        }
    }

    private void addPreparacion(Preparacion p, boolean edit){
        final PreparacionTable pt = new PreparacionTable(p);
        pt.getBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemsPreparacion.remove(pt);
            }
        });
        if(!edit){
            itemsPreparacion.add(pt);
            tablePreparacion.setItems(itemsPreparacion);
        }
        else{
            itemsPreparacion.add( itemsPreparacion.indexOf(editPreparacion), pt);
            itemsPreparacion.remove(editPreparacion);
            editPreparacion = null;
        }

    }

    private void addObservacion(Observacion o, boolean edit){
        final ObservacionTable ot = new ObservacionTable(o);
        ot.getBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemsObs.remove(ot);
            }
        });

        if(!edit){
            itemsObs.add(ot);
            tableObservacion.setItems(itemsObs);

        }else{
            itemsObs.add( itemsObs.indexOf(editObservacion), ot);
            itemsObs.remove(editObservacion);
            editObservacion = null;
        }

    }

    private void displayChangeName(){
        if(buttonChangeName.getText().equals("Cancelar")){
            buttonChangeName.setText("Editar");
        }else{
            buttonChangeName.setText("Cancelar");
        }
        hboxNombreReceta.setVisible(!hboxNombreReceta.isVisible());
    }

    private void cleanFields(){
        nameIngrediente.setText("");
        cantidadIngrediente.setText("");
        taPreparacion.setText("");
        taObservacion.setText("");
        btnIngredientes.setText("Agregar");
        btnPreparacion.setText("Agregar");
        btnObservacion.setText("Agregar");
        editIngrediente = null;
        editPreparacion = null;
        editObservacion = null;
    }

    private void unSelectItems(TableView tableA, TableView tableB){
        tableA.getSelectionModel().clearSelection();
        tableB.getSelectionModel().clearSelection();
    }
    private void unSelectItems(){
        tableIngredientes.getSelectionModel().clearSelection();
        tablePreparacion.getSelectionModel().clearSelection();
        tableObservacion.getSelectionModel().clearSelection();
    }

    public void saveBook(ActionEvent actionEvent) throws FileNotFoundException, UnsupportedEncodingException {
        cleanFields();
        unSelectItems();
        Main.saveBook();
    }

    public void saveRecipe(ActionEvent actionEvent) {
        cleanFields();
        unSelectItems();
        if(edit){

            //Lista Ingredientes
            ArrayList<Ingrediente> ingredientes = new ArrayList<>();
            for(IngredienteTable i: itemsIngredientes){
                ingredientes.add(i.getIngrediente());
            }
            //Lista Preparacion
            ArrayList<Preparacion> preparacion = new ArrayList<>();
            for(PreparacionTable p: itemsPreparacion){
                preparacion.add(p.getPreparacion());
            }
            //Lista Preparacion
            ArrayList<Observacion> obs = new ArrayList<>();
            for(ObservacionTable o: itemsObs){
                obs.add(o.getObservacion());
            }

            receta.setIngredientes(ingredientes);
            receta.setPreparacion(preparacion);
            receta.setObservacion(obs);

            Main.saveRecipe(receta);
        }else{
            //Lista Ingredientes
            ArrayList<Ingrediente> ingredientes = new ArrayList<>();
            for(IngredienteTable i: itemsIngredientes){
                ingredientes.add(i.getIngrediente());
            }
            //Lista Preparacion
            ArrayList<Preparacion> preparacion = new ArrayList<>();
            for(PreparacionTable p: itemsPreparacion){
                preparacion.add(p.getPreparacion());
            }
            //Lista Preparacion
            ArrayList<Observacion> obs = new ArrayList<>();
            for(ObservacionTable o: itemsObs){
                obs.add(o.getObservacion());
            }

            receta.setIngredientes(ingredientes);
            receta.setPreparacion(preparacion);
            receta.setObservacion(obs);
            Main.addNewRecipeToBook(receta);
        }
    }
}


