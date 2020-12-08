package com.isa.writingo.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.isa.writingo.DB;
import com.isa.writingo.modelo.nota;
import com.isa.writingo.modelo.tarea;

import java.text.SimpleDateFormat;
import java.util.Date;

public class daoTarea {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    //Agrega una tarea a la base de datos
    public void agregar(tarea obj, Context contexto){
        String fecha_final = "";
        try {
            Date miFechaHora = sdf.parse(obj.getFecha_fin() + " " + obj.getHora_fin());
            fecha_final = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(miFechaHora).toString();
        } catch(Exception ex) {
            Log.d("cosa", "error al hacer la fecha");
        }
        ContentValues values = new ContentValues();
        values.put(DB.C_TITULO_T, obj.getTitulo());
        values.put(DB.C_DESC_T, obj.getDesc());
        values.put(DB.C_FECHA_FIN, fecha_final);
        values.put(DB.C_HORA_FIN, obj.getHora_fin());
        values.put(DB.C_COLOR, obj.getColor());
        values.put(DB.C_PENDIENTE, obj.getPendiente());
        values.put(DB.C_FILE, obj.getFile());
        DB dataBase = new DB(contexto);
        try{
            dataBase.agregar(DB.T_TAREAS,values);
        } catch (Exception e){
            Toast.makeText(contexto, "Agregar" +e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    //Modifica un campo de la base de datos
    public void editar(tarea obj, Context contexto){
        ContentValues values = new ContentValues();
        values.put(DB.C_TITULO_T, obj.getTitulo());
        values.put(DB.C_DESC_T, obj.getDesc());
        values.put(DB.C_FECHA_FIN, obj.getFecha_fin());
        values.put(DB.C_HORA_FIN, obj.getHora_fin());
        values.put(DB.C_COLOR, obj.getColor());

        DB dataBase = new DB(contexto);
        dataBase.editar(DB.T_TAREAS,values, obj.getId_tarea());
    }

    //Elimina un registro de la base de datos
    public void eliminar(int id_tarea, Context contexto){
        DB dataBase = new DB(contexto);
        dataBase.eliminar(DB.T_TAREAS,id_tarea);
    }

    //Consultar todos
    public Cursor verTodos(Context contexto){
        DB dataBase = new DB(contexto);
        try {
            String proyeccion[] = {DB.C_ID_T, DB.C_TITULO_T, DB.C_DESC_T, DB.C_FECHA_FIN, DB.C_HORA_FIN, DB.C_COLOR, DB.C_PENDIENTE, DB.C_FILE};
            Cursor c = dataBase.consultar(DB.T_TAREAS,proyeccion, DB.C_FECHA_FIN + " ASC");

           /* if(c != null)
                c.moveToFirst();
            Toast.makeText(contexto, c.getString(1), Toast.LENGTH_SHORT).show();*/
            //Log.d("cosa", "es null?"+ c==null?"null":"no null");
            return  c;
        } catch (Exception e){
            Log.d("cosa", "ex:" + e.getMessage());
            Toast.makeText(contexto, "Consulta: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public int ultimo(Context contexto){
        DB dataBase = new DB(contexto);
        int indice = -1;
        try{
            String proyeccion[] = {DB.C_ID_T};
            String where = DB.C_ID_T + "=(SELECT MAX ("+ DB.C_ID_T +") FROM "+ DB.T_TAREAS+")";
            Cursor c = dataBase.consultar(DB.T_TAREAS,proyeccion,null,where);
            c.moveToFirst();
            indice =  c.getInt(0);
        } catch(Exception ex){
            Log.d("cosa", ex.getMessage());
        }
        return indice;
    }
}
