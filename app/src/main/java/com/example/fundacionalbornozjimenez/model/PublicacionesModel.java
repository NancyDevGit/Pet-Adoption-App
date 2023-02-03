package com.example.fundacionalbornozjimenez.model;

public class PublicacionesModel {

    private int foto,iconoCiudad,iconoSexo;
    private String estadoSol,idMascota,nombre, ciudad,edad,personalidad, raza, masInformacion;
    private String fotoS;


    public PublicacionesModel(String estadoSol,String idMascota,String nombre, int iconoSexo, int iconoCiudad, String ciudad, String edad,
                              String personalidad, String raza){
        this.nombre=nombre;
        this.iconoSexo=iconoSexo;
        this.iconoCiudad=iconoCiudad;
        this.ciudad=ciudad;
        this.edad=edad;
        this.personalidad=personalidad;
        this.raza=raza;
        this.estadoSol=estadoSol;
        this.idMascota=idMascota;

    }

    public String getIdMascota() {
        return idMascota;
    }

    public String getEstadoSol() {
        return estadoSol;
    }

    public PublicacionesModel(String nombre, int iconoSexo, int iconoCiudad, String ciudad, String edad,
                              String personalidad, String raza){
        this.nombre=nombre;
        this.iconoSexo=iconoSexo;
        this.iconoCiudad=iconoCiudad;
        this.ciudad=ciudad;
        this.edad=edad;
        this.personalidad=personalidad;
        this.raza=raza;


    }

    public PublicacionesModel(int foto, int iconoCiudad, int iconoSexo,String nombre,
                              String ciudad, String edad, String personalidad, String raza,
                              String masInformacion) {
        this.foto = foto;
        this.iconoCiudad = iconoCiudad;
        this.iconoSexo=iconoSexo;
        this.nombre = nombre;
        this.ciudad=ciudad;
        this.edad = edad;
        this.personalidad = personalidad;
        this.raza = raza;
        this.masInformacion = masInformacion;

    }

    public PublicacionesModel(String fotoS, int iconoCiudad, int iconoSexo,String nombre,
                              String ciudad, String edad, String personalidad, String raza,
                              String masInformacion) {
        this.fotoS = fotoS;
        this.iconoCiudad = iconoCiudad;
        this.iconoSexo=iconoSexo;
        this.nombre = nombre;
        this.ciudad=ciudad;
        this.edad = edad;
        this.personalidad = personalidad;
        this.raza = raza;
        this.masInformacion = masInformacion;

    }

    public String getFotoS() {
        return fotoS;
    }

    public int getIconoCiudad() {
        return iconoCiudad;
    }

    public int getIconoSexo() {
        return iconoSexo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getEdad() {
        return edad;
    }

    public String getPersonalidad() {
        return personalidad;
    }

    public String getRaza() {
        return raza;
    }

    public String getMasInformacion() {
        return masInformacion;
    }

}
