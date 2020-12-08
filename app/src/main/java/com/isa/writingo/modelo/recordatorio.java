package com.isa.writingo.modelo;

import java.io.Serializable;

public class recordatorio implements Serializable {
    private int id_rec;         // identificador
    private int id_tarea;    // tarea con la que se relaciona
    private String fecha;       // fecha y hora en la que hay que notificar
    private String hora;

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public recordatorio(int id_rec, int id_tarea, String fecha, String hora) {
        this.id_rec = id_rec;
        this.id_tarea = id_tarea;
        this.fecha = fecha;
        this.hora = hora;
    }
    public recordatorio(String fecha, String hora) {
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId_rec() {
        return id_rec;
    }

    public void setId_rec(int id_rec) {
        this.id_rec = id_rec;
    }

    public int getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(int id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
