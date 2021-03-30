package sample.Model;

import java.util.ArrayList;

public class Receta {
    private int indice;
    private String nombre;
    private ArrayList<Ingrediente> ingredientes;
    private ArrayList<Preparacion> preparacion;
    private ArrayList<Observacion> observacion;

    public Receta(){}

    public Receta(int indice, String nombre, ArrayList<Ingrediente> ingredientes, ArrayList<Preparacion> preparacion, ArrayList<Observacion> observacion) {
        this.indice = indice;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.preparacion = preparacion;
        this.observacion = observacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public ArrayList<Preparacion> getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(ArrayList<Preparacion> preparacion) {
        this.preparacion = preparacion;
    }

    public ArrayList<Observacion> getObservacion() {
        return observacion;
    }

    public void setObservacion(ArrayList<Observacion> observacion) {
        this.observacion = observacion;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return  nombre ;
    }
}
