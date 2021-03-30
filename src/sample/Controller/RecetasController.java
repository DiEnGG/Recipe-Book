package sample.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import sample.Main;
import sample.Model.*;
import sample.Model.TreeTableModel.RecetaTreeTable;
import sample.Model.TreeTableModel.RootTreeTable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecetasController implements Initializable {

    public TreeTableView recipesTree;
    public TreeTableColumn columnRecipes;
    public TreeTableColumn columnDelete;
    public TreeItem root = new TreeItem();
    public TableView tableViewIngredientes;
    public TableColumn columnIngrediente;
    public TableColumn columnCantidad;
    public Label recipeName;
    public TableColumn columnObs;
    public TableColumn columnPasos;
    public TableView tableViewObs;
    public TableView tableViewPasos;
    public HBox hboxTools;
    public AnchorPane anchorLeft;
    public Button btnTool;

    private Receta receta;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTreeTable();
        anchorLeft.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double value = ((Double) newValue) - 5;
                hboxTools.setPrefWidth(value);
                recipesTree.setPrefWidth((Double) newValue);
                columnRecipes.setPrefWidth( ( (Double) newValue) - ( columnDelete.getWidth() + 5 ));
            }
        });
    }

    private void fillTreeTable() {
        Book book = Main.getBook();

        if(book != null) {
            root.getChildren().clear();
            for (Receta r : book.getRecetas()) {
                final RecetaTreeTable rtt = new RecetaTreeTable(r);

                rtt.getButton().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        book.getRecetas().remove(rtt.getReceta());
                        Main.setBook(book);
                        fillTreeTable();
                    }
                });

                root.getChildren().add(new TreeItem(rtt));
            }

            root.setValue(new RootTreeTable(book.getNombre()));

            columnRecipes.setCellValueFactory(new TreeItemPropertyValueFactory<>("nombre"));
            columnDelete.setCellValueFactory(new TreeItemPropertyValueFactory<>("button"));

            recipesTree.setRoot(root);
            recipesTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (recipesTree.getSelectionModel().getSelectedIndex() != 0) {
                        TreeItem ti = (TreeItem) newValue;
                        RecetaTreeTable rtt = (RecetaTreeTable) ti.getValue();
                        lookRecipe(rtt.getReceta());
                    }
                }
            });
            root.setExpanded(true);
            recipesTree.getSelectionModel().select(1);
        }
    }

    public void lookRecipe(Receta recipe){
        receta = recipe;
        recipeName.setText("Nombre de Receta: " + recipe.getNombre());
        ObservableList<Ingrediente> itemsIngredientes = FXCollections.observableArrayList();
        for(Ingrediente i : recipe.getIngredientes()){
            itemsIngredientes.add(i);
        }
        columnIngrediente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tableViewIngredientes.setItems(itemsIngredientes);

        ObservableList<Preparacion> itemsPasos = FXCollections.observableArrayList();
        for(Preparacion p : recipe.getPreparacion()){
            itemsPasos.add(p);
        }
        columnPasos.setCellValueFactory( new PropertyValueFactory<>("paso"));
        tableViewPasos.setItems(itemsPasos);

        ObservableList<Observacion> itemsObs = FXCollections.observableArrayList();
        for(Observacion o : recipe.getObservacion()){
            itemsObs.add(o);
        }
        columnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
        tableViewObs.setItems(itemsObs);
    }

    public void createRecipe(ActionEvent actionEvent) throws IOException {
        closeWindow(actionEvent);
        openWindowCreateRecipe("Nueva Receta");
    }

    public void editRecipeWindow(ActionEvent actionEvent) throws IOException {
        closeWindow(actionEvent);
        Main.setEditableReceta(receta);
        openWindowCreateRecipe(receta.getNombre());
    }

    private void openWindowCreateRecipe(String windowTitle) throws IOException {
        //Creamos nueva ventana
        Stage window = new Stage();
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Saliendo...");
                alert.setHeaderText("Seguro que quieres cerrar la ventana actual?");
                alert.setContentText("Perderas los cambios no guardados");

                ButtonType buttonTypeAccept = new ButtonType("Aceptar");
                ButtonType buttonTypeCancel = new ButtonType("Cancelar");

                alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeAccept){
                    Main.setEditableReceta(null);

                    Stage window = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("../View/recetas.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    window.setTitle("Receta");
                    window.setScene(new Scene(root, 1280, 720));
                    window.setResizable(false);
                    window.show();
                } else {
                    event.consume();
                }
            }
        });
        Parent root = FXMLLoader.load(getClass().getResource("../View/editRecipe.fxml"));
        window.setTitle(windowTitle);
        window.setScene(new Scene(root, 1280, 720));
        window.setResizable(false);
        window.show();
    }

    private void closeWindow(ActionEvent ac){
        Node node = (Node) ac.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void saveBook(ActionEvent actionEvent) throws FileNotFoundException, UnsupportedEncodingException {
        Main.saveBook();
    }
}
