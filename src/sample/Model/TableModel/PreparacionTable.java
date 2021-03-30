package sample.Model.TableModel;

import javafx.scene.control.Button;
import sample.Model.Preparacion;

public class PreparacionTable {

    private Preparacion preparacion;
    private Button btn;

    public PreparacionTable(Preparacion preparacion){
        this.preparacion = preparacion;
        this.btn = new Button("X");
    }

    public Preparacion getPreparacion() {
        return preparacion;
    }

    public Button getBtn() {
        return btn;
    }

    public String getPaso(){
        return preparacion.getPaso();
    }

}

