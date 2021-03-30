package sample.Model;

import java.util.ArrayList;

public class Book {
    private String nombre;
    private ArrayList<Receta> recetas;

    public Book(String nombre, ArrayList<Receta> recetas) {
        this.nombre = nombre;
        this.recetas = recetas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(ArrayList<Receta> recetas) {
        this.recetas = recetas;
    }
}
