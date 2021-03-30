package sample.Model.TableModel;

import javafx.scene.control.Button;
import sample.Model.Ingrediente;

public class IngredienteTable {

    private Button btn;
    private Ingrediente ingrediente;

    public IngredienteTable(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;

        this.btn = new Button("X");
    }

    public Button getBtn(){
        return this.btn;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public String getNombre(){
        return ingrediente.getNombre();
    }
    public String getCantidad(){
        return ingrediente.getCantidad();
    }
}
