package com.example.fundacionalbornozjimenez.model;

public class FormularioPublicacionModel {
    private String nombre,id;

    public FormularioPublicacionModel(String nombre, String id) {
        this.nombre=nombre;
        this.id=id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getId(){
        return id;
    }
}
