package sample.Controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.Book;
import sample.Model.Receta;

import java.io.*;
import java.util.ArrayList;

public class InicioController {

    public TextField bookTitleTextField;
    public HBox hBoxNewBook;
    public Button newBookButton;
    public Button saveNewBookButton;
    public Button openBookButton;
    private boolean isDialogWindowOpen = false;

    public void toggleVisibleNewBook(ActionEvent actionEvent) {
        toggleVisibleHBox();
    }

    public void openBook(ActionEvent actionEvent) {
        if(!isDialogWindowOpen) {
            openDialogWindowAction();
            toggleVisibleHBox(false);
            bookTitleTextField.setText("");

            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File selectedFile = fc.showOpenDialog(null);
            if (selectedFile != null) {
                try{
                    Gson gson = new Gson();

                    Book book = gson.fromJson(new FileReader(selectedFile.getAbsolutePath()), Book.class);

                    Main.setBook(book); // guardarmos la receta en Main.class

                    // cerramos la ventana actual
                    final Node source = (Node) actionEvent.getSource();
                    final Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                    Main.setFileProperties(selectedFile.getAbsolutePath());
                    openRecipeWindow();

                }catch(Exception ex){
                    ex.printStackTrace();
                }
                openDialogWindowAction();
            }else{
                System.out.println("selectedFile's cancel button pressed");
                openDialogWindowAction();
            }
        }
    }


    public void toggleVisibleHBox() {
        boolean isVisible =  hBoxNewBook.isVisible();
        hBoxNewBook.setVisible(!isVisible);
    }

    public void toggleVisibleHBox(boolean visible){
        hBoxNewBook.setVisible(visible);
    }

    public void newBook() throws IOException {
        if (!isDialogWindowOpen) {
            openDialogWindowAction();
            String fileTitle = bookTitleTextField.getText();
            DirectoryChooser dc = new DirectoryChooser();
            File selectedDirectory = dc.showDialog(null);
            if(selectedDirectory != null){
                PrintWriter write = new PrintWriter(selectedDirectory.getAbsolutePath() + "/"+fileTitle, "UTF-8");
                write.println("");
                write.flush();
                write.close();
                Main.setFileProperties(selectedDirectory.getAbsolutePath() + "/" + fileTitle + ".json");
                Main.setBook(new Book(fileTitle, new ArrayList<Receta>()));
                openDialogWindowAction();

                Stage stage = (Stage) openBookButton.getScene().getWindow();
                stage.close();

                openRecipeWindow();
            }else{
                System.out.println("selectedDirectory's cancel button pressed");
                openDialogWindowAction();
            }
        }
    }

    public void newBookEnterPressed(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode().toString().equals("ENTER")){
            newBook();
        }
    }

    public void newBookButtonClicked(ActionEvent actionEvent) throws IOException {
        newBook();
    }

    public void openDialogWindowAction(){
        isDialogWindowOpen = !isDialogWindowOpen;
        newBookButton.setDisable(isDialogWindowOpen);
        bookTitleTextField.setDisable(isDialogWindowOpen);
        saveNewBookButton.setDisable(isDialogWindowOpen);
        openBookButton.setDisable(isDialogWindowOpen);
        
    }

    private void openRecipeWindow() throws IOException {

        //Creamos nueva ventana
        Stage window = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../View/recetas.fxml"));
        window.setTitle("Receta");
        window.setScene(new Scene(root, 1280, 720));
        window.setResizable(false);
        window.show();
    }
}