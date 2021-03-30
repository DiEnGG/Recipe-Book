package sample.Model.TreeTableModel;

import javafx.scene.control.Button;
import sample.Model.Receta;

public class RecetaTreeTable {
    private String nombre;
    private Receta receta;
    private Button button;

    public RecetaTreeTable(Receta r){
        this.receta = r;
        this.button = new Button("X");
        this.nombre = r.getNombre();
    }

    public Button getButton(){
        return this.button;
    }
    public Receta getReceta(){
        return this.receta;
    }

    public String getNombre(){
        return this.nombre;
    }

}
