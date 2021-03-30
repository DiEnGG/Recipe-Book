package sample;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Book;
import sample.Model.Receta;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Main extends Application {

    private static Book book;
    private static Receta editableReceta;
    private static String filePath;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/inicio.fxml"));
        primaryStage.setTitle("Libro Recetas - Inicio");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static Book getBook() {
        return book;
    }

    public static void setBook(Book b) {
        book = b;
    }

    public static Receta getEditableReceta(){
        return editableReceta;
    }
    public static void setEditableReceta(Receta r){
        editableReceta = r;
    }

    public static void saveBook() throws FileNotFoundException, UnsupportedEncodingException {

        //creamos archivo .json y lo guardamos.
        PrintWriter write = new PrintWriter(filePath, "UTF-8");
        //convertimos la clase en un String JSON;
        String json = new Gson().toJson(book);
        write.println(json);
        write.flush();
        write.close();
    }

    public static void setFileProperties(String path){
        filePath = path;
    }

    public static void addNewRecipeToBook(Receta receta){
        if(book != null){
            receta.setIndice(book.getRecetas().size());
        }else{
            receta.setIndice(0);
        }
        book.getRecetas().add(receta);
    }
    public static void saveRecipe(Receta receta){
        setEditableReceta(receta);
        book.getRecetas().remove(editableReceta.getIndice());
        book.getRecetas().add(editableReceta.getIndice(), editableReceta);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
