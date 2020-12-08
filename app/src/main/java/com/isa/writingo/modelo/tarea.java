package com.isa.writingo.modelo;

import java.io.Serializable;
import java.util.Set;

public class tarea implements Serializable {
    private int id_tarea;
    private String titulo;
    private String desc;
    private String fecha_fin;
    private String hora_fin;
    private int color;
    private String file;
    private int pendiente;


    public tarea(int id_tarea, String titulo, String desc, String fecha_fin, String hora_fin, int color, String file, int pendiente) {
        this.id_tarea = id_tarea;       // identificador
        this.titulo = titulo;           // titulo de tarea
        this.desc = desc;               // descripcion de la tarea
        this.fecha_fin = fecha_fin;     // fecha de cumplimiento de tarea
        this.hora_fin = hora_fin;
        this.color = color;
        this.file = file;
        this.pendiente = pendiente;
    }
    public tarea(String titulo, String desc, String fecha_fin, String hora_fin, int color, String file, int pendiente) {
        this.titulo = titulo;           // titulo de tarea
        this.desc = desc;               // descripcion de la tarea
        this.fecha_fin = fecha_fin;     // fecha de cumplimiento de tarea
        this.hora_fin = hora_fin;
        this.color = color;
        this.file = file;
        this.pendiente = pendiente;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getPendiente() {
        return pendiente;
    }

    public void setPendiente(int pendiente) {
        this.pendiente = pendiente;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(int id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
}
