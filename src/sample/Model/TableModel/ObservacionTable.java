package sample.Model.TableModel;

import javafx.scene.control.Button;
import sample.Model.Observacion;

public class ObservacionTable {

    private Observacion observacion;
    private Button btn;

    public ObservacionTable(Observacion observacion){
        this.observacion = observacion;
        this.btn = new Button("X");
    }

    public Observacion getObservacion() {
        return observacion;
    }

    public Button getBtn() {
        return btn;
    }

    public String getObs(){
        return observacion.getObs();
    }
}
