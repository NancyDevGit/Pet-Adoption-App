package com.example.fundacionalbornozjimenez.model;

public class SolicitudesModel {

    private String idSolicitud,idSolicitante,fecha,estadoSol,nombre,sexo,edad,ocupacion,vivienda,
            ciudad,dele_mun,estado,mascotas;



    public SolicitudesModel(String idSolicitante,String idSolicitud,String fecha,String estadoSol,String nombre,
                            String apellidos,String sexo, String edad, String ocupacion,
                            String vivienda,String mascotas,String ciudad,String dele_mun, String estado){


        this.idSolicitante=idSolicitante;
        this.fecha=fecha;
        this.estadoSol=estadoSol;
        this.nombre=nombre+" "+apellidos;
        this.sexo=sexo;
        this.edad=edad;
        this.ocupacion=ocupacion;
        this.vivienda=vivienda;
        this.mascotas=mascotas;
        this.ciudad=ciudad;
        this.dele_mun=dele_mun;
        this.estado=estado;
        this.idSolicitud=idSolicitud;

    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public String getIdSolicitante() {
        return idSolicitante;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstadoSol() {
        return estadoSol;
    }

    public String getNombre() {
        return nombre;
    }



    public String getSexo() {
        return sexo;
    }

    public String getEdad() {
        return edad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public String getVivienda() {
        return vivienda;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDele_mun() {
        return dele_mun;
    }

    public String getEstado() {
        return estado;
    }

    public String getMascotas() {
        return mascotas;
    }
}
