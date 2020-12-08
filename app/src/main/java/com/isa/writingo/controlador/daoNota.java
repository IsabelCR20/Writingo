package com.isa.writingo.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.isa.writingo.DB;
import com.isa.writingo.modelo.nota;

import java.text.SimpleDateFormat;
import java.util.Date;

public class daoNota {

    //Agrega una nota a la base de datos
    public void agregar(nota obj, Context contexto){

        ContentValues values = new ContentValues();
        values.put(DB.C_TITULO_N, obj.getTitulo());
        values.put(DB.C_DESC_N, obj.getDesc());
        values.put(DB.C_FECHA, obj.getFecha());
        values.put(DB.C_COLOR, obj.getColor());
        values.put(DB.C_FILE, obj.getFile());
        DB dataBase = new DB(contexto);
        try{
            dataBase.agregar(DB.T_NOTAS,values);
        } catch (Exception e){
            Toast.makeText(contexto, "Agregar" +e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    //Modifica un campo de la base de datos
    public void editar(nota obj, Context contexto){
        ContentValues values = new ContentValues();
        values.put(DB.C_TITULO_N, obj.getTitulo());
        values.put(DB.C_DESC_N, obj.getDesc());
        values.put(DB.C_FECHA, obj.getFecha());
        values.put(DB.C_COLOR, obj.getColor());

        DB dataBase = new DB(contexto);
        dataBase.editar(DB.T_NOTAS,values, obj.getId_nota());
    }

    //Elimina un registro de la base de datos
    public void eliminar(int id_nota, Context contexto){
        DB dataBase = new DB(contexto);
        dataBase.eliminar(DB.T_NOTAS,id_nota);
    }

    //Consultar todos
    public Cursor verTodos(Context contexto){
        DB dataBase = new DB(contexto);
        try {
            String proyeccion[] = {DB.C_ID_N, DB.C_TITULO_N, DB.C_DESC_N, DB.C_FECHA, DB.C_COLOR, DB.C_FILE};
            Cursor c = dataBase.consultar(DB.T_NOTAS,proyeccion, DB.C_FECHA + " DESC");

           /* if(c != null)
                c.moveToFirst();
            Toast.makeText(contexto, c.getString(1), Toast.LENGTH_SHORT).show();*/
            return  c;
        } catch (Exception e){
            Toast.makeText(contexto, "Consulta: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
