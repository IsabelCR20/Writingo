package com.isa.writingo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    SQLiteDatabase db;
    //Variables estáticas para administrar mejor la DB
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "writingo.db";
    // Nombre de tablas y columnas de la BD
    public static final String T_NOTAS = "notas";
    public static final String C_ID_N = "id";
    public static final String C_TITULO_N = "titulo";
    public static final String C_DESC_N ="descripcion";
    public static final String C_FECHA ="fecha_creacion";
    public static final String T_TAREAS = "tareas";
    public static final String C_ID_T ="id";
    public static final String C_TITULO_T="titulo";
    public static final String C_DESC_T="descripcion";
    public static final String C_FECHA_FIN ="fecha_fin";
    public static final String C_HORA_FIN = "hora_fin";
    public static final String C_PENDIENTE = "pendiente";
    public static final String C_FILE = "files";
    public static final String T_REC = "recordatorios";
    public static final String C_ID_R ="id";
    public static final String C_IDTAREA = "id_tarea";
    public static final String C_FECHA_R = "fecha";
    public static final String C_COLOR = "color";
    public static final String C_HORA_R = "hora";


    // constructor de la clase que se está heredando. Se le indica el contexto, nombre de la bd, cursorFactory y version de bd
    public DB(Context contexto){
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Metodos sobreescritos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String crear_notas = "create table " + T_NOTAS + "(" +
                C_ID_N + " integer primary key autoincrement," +
                C_TITULO_N + " text not null, " +
                C_DESC_N + " text not null, "+
                C_FECHA + " datetime not null," + //C_FECHA + " text not null," +
                C_COLOR + " integer not null," +
                C_FILE + " text"
                + ");";
        String crear_tareas = "create table " + T_TAREAS + "(" +
                C_ID_T + " integer primary key autoincrement," +
                C_TITULO_T + " text not null, " +
                C_DESC_T + " text not null, "+
                C_FECHA_FIN + " datetime not null," +//C_FECHA_FIN + " text not null," +
                C_HORA_FIN + " text not null," +
                C_COLOR + " integer not null,"+
                C_PENDIENTE + " integer not null," +
                C_FILE + " text"
                + ");";
        String crear_rec = "create table " + T_REC + "(" +
                C_ID_R + " integer primary key autoincrement," +
                C_IDTAREA + " integer not null, " +
                C_FECHA_R + " datetime not null,"  +    //C_FECHA_R + " text not null,"+
                C_HORA_R + " text not null"
                + ");";
        // Ejecución de sentencias
        sqLiteDatabase.execSQL(crear_notas);
        sqLiteDatabase.execSQL(crear_tareas);
        sqLiteDatabase.execSQL(crear_rec);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {

    }
    /*
        public void  abriC(int op){
            switch (op){
                case 0:     // escribir
                    db = this.getWritableDatabase();
                    break;
                case 1:     // leer
                    db = this.getReadableDatabase();
                    break;
            }

        }

        public void  cerrarC(){
            db.close();
        }
    */
    public void agregar(String nombreTabla, ContentValues values){
        db = this.getWritableDatabase();
        try{
            db.insert(nombreTabla, null, values);
        } catch (Exception e){
            throw e;
        }
    }
    public void editar(String nombreTabla, ContentValues values, int id){
        db = this.getWritableDatabase();
        int x = db.update(nombreTabla, values, "id=?", new String[] {String.valueOf(id)});
        //db.close();
    }
    public void  eliminar(String nombreTabla,int id){
        db = this.getWritableDatabase();
        db.delete(nombreTabla, "id=?", new String[]{String.valueOf(id)});
        //db.close();
    }
    public Cursor consultar(String nombreTabla, String[] proyeccion, String orderBy){
        db = this.getReadableDatabase();
        try{
            Cursor c = db.query(nombreTabla, proyeccion,null,null,null,null,orderBy);
            //db.close();
            return c;
        } catch (Exception e){
            throw e;
        }
    }
    public Cursor consultar(String nombreTabla, String[] proyeccion, String orderBy, String where){
        db = this.getReadableDatabase();
        try{
            Cursor c = db.query(nombreTabla, proyeccion,where,null,null,null,orderBy);
            //db.close();
            return c;
        } catch (Exception e){
            throw e;
        }


    }
}
