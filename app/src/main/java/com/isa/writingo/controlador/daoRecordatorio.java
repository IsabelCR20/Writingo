package com.isa.writingo.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.isa.writingo.DB;
import com.isa.writingo.modelo.nota;
import com.isa.writingo.modelo.recordatorio;

public class daoRecordatorio {
    //Agrega recordatorio a la base de datos
    public void agregar(recordatorio obj, Context contexto){
        ContentValues values = new ContentValues();
        values.put(DB.C_IDTAREA, obj.getId_tarea());
        values.put(DB.C_FECHA_R, obj.getFecha());
        values.put(DB.C_HORA_R, obj.getHora());

        DB dataBase = new DB(contexto);
        try{
            dataBase.agregar(DB.T_REC,values);
        } catch (Exception e){
            Toast.makeText(contexto, "Agregar" +e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    /*
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
    */
    //Elimina un registro de la base de datos
    public void eliminar(int id_rec, Context contexto){
        DB dataBase = new DB(contexto);
        dataBase.eliminar(DB.T_REC,id_rec);
    }

    //Consultar todos
    public Cursor verTodos(Context contexto){
        DB dataBase = new DB(contexto);
        try {
            String proyeccion[] = {DB.C_ID_R, DB.C_IDTAREA, DB.C_FECHA_R, DB.C_HORA_R};
            Cursor c = dataBase.consultar(DB.T_REC,proyeccion, null);

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
