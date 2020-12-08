package com.isa.writingo.modelo;

import java.io.Serializable;

public class nota implements Serializable {
    private int id_nota;
    private String titulo;
    private String desc;
    private String fecha;
    private int color;
    private String file;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public nota(int id_nota, String titulo, String desc, String fecha, int color, String file){
        this.id_nota = id_nota;         // identificador
        this.titulo = titulo;           // titulo de la nota
        this.desc = desc;               // contenido de la nota
        this.fecha = fecha;             // fecha en que se crea la nota
        this.color = color;
        this.file = file;
    }
    public nota( String titulo, String desc, String fecha, int color, String file){
        this.titulo = titulo;           // titulo de la nota
        this.desc = desc;               // contenido de la nota
        this.fecha = fecha;             // fecha en que se crea la nota
        this.color = color;
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId_nota() {
        return id_nota;
    }

    public void setId_nota(int id_nota) {
        this.id_nota = id_nota;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
