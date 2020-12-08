package com.isa.writingo.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isa.writingo.DB;
import com.isa.writingo.R;
import com.isa.writingo.RegistroActivity;
import com.isa.writingo.adaptadores.PostitsAdapter;
import com.isa.writingo.controlador.daoNota;
import com.isa.writingo.controlador.daoRecordatorio;
import com.isa.writingo.controlador.daoTarea;
import com.isa.writingo.modelo.nota;
import com.isa.writingo.modelo.recordatorio;
import com.isa.writingo.modelo.tarea;

import java.text.SimpleDateFormat;
import java.util.Vector;

public class PrincipalFragment extends Fragment {
    private int RESULT_REG = 1;
    FloatingActionButton fabSearch;
    FloatingActionButton fabAdd;
    PostitsAdapter adaptador;
    RecyclerView rvf;
    View vista;
    Vector<nota> vectorNotas;
    Vector<tarea> vectorTareas;
    Vector<recordatorio> vectorReco;
    int tipo;
    public int posicionActual;
    private int banderaMenu;    // 0 = nadie; 1 = nota; 2 = tarea
    //Constructor
    public PrincipalFragment(int tipo){
        this.tipo = tipo;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("cosa","crea vista");
        vectorTareas = new Vector<>();
        vectorReco = new Vector<>();
        vista = inflater.inflate(R.layout.fragment_principal, null);   // Inflando al fragment prinicpal
        //RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
        RecyclerView.LayoutManager lm = new StaggeredGridLayoutManager(2,1);

        Log.d("cosa","tipo = " +tipo);
        if(tipo == 0){      // Notas
            Log.d("cosa","Entro a Notas");

            rvf =vista.findViewById(R.id.rvPostits);
            registerForContextMenu(rvf);
            rvf.setAdapter(adaptador);
            rvf.setLayoutManager(lm);
            cargaNotas(rvf);

            rvf.addItemDecoration(new VerticalSpaceItemDecoration());
            //
            fabAdd = vista.findViewById(R.id.fabAdd);
            fabSearch = vista.findViewById(R.id.fabSearch);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent screen = new Intent(getActivity(),RegistroActivity.class);
                    screen.putExtra("tipo", 0); // Tipo 0 indica agregar nota;
                    startActivityForResult(screen, RESULT_REG);
                }
            });
            return vista;
        } else {    // Tareas
            Log.d("cosa","Entro a Tareas");
            rvf = vista.findViewById(R.id.rvPostits);
            registerForContextMenu(rvf);
            rvf.setLayoutManager(lm);

            cargarTareas(rvf);
            rvf.addItemDecoration(new VerticalSpaceItemDecoration());

            //
            fabAdd = vista.findViewById(R.id.fabAdd);
            fabSearch = vista.findViewById(R.id.fabSearch);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent screen = new Intent(getActivity(),RegistroActivity.class);
                    screen.putExtra("tipo", 1); // Tipo 0 indica agregar tarea;
                    startActivityForResult(screen, RESULT_REG);
                }
            });
            return vista;
        }

    }

    public interface OnFragmentIterationListener {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_REG){
            if(resultCode == RegistroActivity.RESULT_OK) {
                RecyclerView.LayoutManager lm = new StaggeredGridLayoutManager(2,1);
                Toast.makeText(getActivity(), "Resultado ok", Toast.LENGTH_SHORT).show();
                if(data.getIntExtra("tipo", 0) == 0){
                    cargaNotas(rvf);
                } else {
                    cargarTareas((RecyclerView) vista.findViewById(R.id.rvPostits));
                }
                rvf.setLayoutManager(lm);
            }
        }
    }

    private void cargaNotas( RecyclerView rvf){
        vectorNotas = new Vector<>();
        try {
            Cursor lista = (new daoNota()).verTodos(getContext());
            Log.d("cosa", "lista notas" + lista.getCount());
            if (lista.getCount() > 0)
                lista.moveToFirst();

            while (!lista.isAfterLast()) {
                nota nuevaNota = new nota(lista.getInt(lista.getColumnIndex(DB.C_ID_N)),
                        lista.getString(lista.getColumnIndex(DB.C_TITULO_N)),
                        lista.getString(lista.getColumnIndex(DB.C_DESC_N)),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lista.getString(lista.getColumnIndex(DB.C_FECHA))).toString()
                        ,
                        lista.getInt(lista.getColumnIndex(DB.C_COLOR)),
                        lista.getString(lista.getColumnIndex(DB.C_FILE)));
                vectorNotas.add(nuevaNota);
                lista.moveToNext();
            }
        } catch (Exception ex){
            Log.d("cosa", "Error en insertar nota: " + ex.getMessage());
        }
        //
        adaptador = new PostitsAdapter(getActivity(), 0, vectorNotas, new Vector<tarea>(), new Vector<recordatorio>());

        adaptador.setOnLongItemClickListener(new PostitsAdapter.onLongItemClickListener(){
            @Override
            public void ItemLongClicked(View v, int position) {
                Log.d("cosa", "long click desde el adaptador, pos: " + position);
                posicionActual = position;
                banderaMenu = 1;
                v.showContextMenu();
            }
        });
        //
        rvf.setAdapter(adaptador);
    }
    private void cargarTareas(RecyclerView rvf){
        vectorTareas = new Vector<>();
        vectorReco = new Vector<>();
        //
        try {
            Cursor lista = (new daoTarea()).verTodos(getContext());
            Log.d("cosa", "lista " + lista.getCount());
            if (lista.getCount() > 0)
                lista.moveToFirst();

            //Toast.makeText(getActivity(), "Ates de consulta", Toast.LENGTH_SHORT);
            Log.d("cosa", lista.getString(lista.getColumnIndex(DB.C_FECHA_FIN)) +"  ***");
            Log.d("cosa", new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lista.getString(lista.getColumnIndex(DB.C_FECHA_FIN))) +"  formato gringo");

            while (!lista.isAfterLast()) {
                tarea nuevaTarea = new tarea(lista.getInt(lista.getColumnIndex(DB.C_ID_T)),
                        lista.getString(lista.getColumnIndex(DB.C_TITULO_T)),
                        lista.getString(lista.getColumnIndex(DB.C_DESC_T)),
                        sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lista.getString(lista.getColumnIndex(DB.C_FECHA_FIN))))
                        ,
                        lista.getString(lista.getColumnIndex(DB.C_HORA_FIN)),
                        lista.getInt(lista.getColumnIndex(DB.C_COLOR)),
                        lista.getString(lista.getColumnIndex(DB.C_FILE)),
                        lista.getInt(lista.getColumnIndex(DB.C_PENDIENTE)));
                vectorTareas.add(nuevaTarea);
                lista.moveToNext();
            }
            Log.d("cosa", "despues consulta");
            //
            Cursor listaReco = (new daoRecordatorio().verTodos(getContext()));
            if (listaReco.getCount() > 0)
                listaReco.moveToFirst();

            while (!listaReco.isAfterLast()) {
                recordatorio nuevoR = new recordatorio(listaReco.getInt(listaReco.getColumnIndex(DB.C_ID_R)),
                        listaReco.getInt(listaReco.getColumnIndex(DB.C_IDTAREA)),
                        listaReco.getString(listaReco.getColumnIndex(DB.C_FECHA_R)),
                        listaReco.getString(listaReco.getColumnIndex(DB.C_HORA_R)));
                vectorReco.add(nuevoR);
                listaReco.moveToNext();
            }
        } catch(Exception ex){
            Log.d("cosa", "Error en insertar tarea: " + ex.getMessage());
        }
        Log.d("cosa", "pasó asignacion ");
        //
        adaptador = new PostitsAdapter(getActivity(), 1, new Vector<nota>(), vectorTareas, vectorReco);
        //Agregar evento de click al elemento
        adaptador.setOnLongItemClickListener(new PostitsAdapter.onLongItemClickListener(){
            @Override
            public void ItemLongClicked(View v, int position) {
                Log.d("cosa", "long click desde el adaptador, pos: " + position);
                posicionActual = position;
                banderaMenu = 2;
                Log.d("cosa", "Pos actual -> " + posicionActual + "   bandera->" + banderaMenu);
                v.showContextMenu();
            }
        });
        //
        rvf.setAdapter(adaptador);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(banderaMenu == 1){
            MenuInflater inflater = new MenuInflater(getActivity());
            inflater.inflate(R.menu.menu_contextual_notas, menu);
            for (int i = 0; i < menu.size(); ++i) {
                MenuItem item = menu.getItem(i);
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onContextItemSelected(item);
                        return true;
                    }
                });
            }
            Log.d("cosa", "Creacion de menu NOTA");
        } else if(banderaMenu == 2){
            MenuInflater inflater = new MenuInflater(getActivity());
            inflater.inflate(R.menu.menu_contextual_tareas, menu);
            for (int i = 0; i < menu.size(); ++i) {
                MenuItem item = menu.getItem(i);
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onContextItemSelected(item);
                        return true;
                    }
                });
            }
            Log.d("cosa", "Creacion de menu TAREA... pos->" + posicionActual);
        }
        //banderaMenu = 0;
    }

    int vez=0;
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Log.d("cosa", "Pos actual: " + posicionActual + "    bandera: " + banderaMenu);
        switch(item.getItemId()) {
            case R.id.mnu_delete:
                Log.d("cosa", "cudadito borra");
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.alert1)
                        .setTitle(R.string.dBorrarNota)
                        .setMessage( Html.fromHtml(getString(R.string.dMsnBorrar) + ": "+ "<strong>"+  vectorNotas.elementAt(posicionActual).getTitulo()+"</strong>" + "?") )
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int idNota = vectorNotas.elementAt(posicionActual).getId_nota();
                                new daoNota().eliminar(idNota, getActivity());
                                Log.d("cosa", "ELIMINAR NOTA:  id->" + idNota);
                                cargaNotas(rvf);
                                rvf.setLayoutManager(new StaggeredGridLayoutManager(2,1));
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                break;
            case R.id.mnu_edit:
                Log.d("cosa", "EDITAR NOTA");
                Intent screen = new Intent(getActivity(),RegistroActivity.class);
                nota notitaSeleccionada = vectorNotas.elementAt(posicionActual);
                Log.d("cosa", "Nota seleccionada: ID -> " + notitaSeleccionada.getId_nota());
                screen.putExtra("tipo", 0);
                screen.putExtra("abrir", notitaSeleccionada);
                startActivityForResult(screen, RESULT_REG);
                break;
            case R.id.mnu_delete_t:
                Log.d("cosa", "cudadito borra tarea: pos-> " + posicionActual + "    vector: " + vectorTareas.toString());
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.alert1)
                        .setTitle(R.string.dBorrarTarea)
                        .setMessage( Html.fromHtml(getString(R.string.dMsnBorrar) + ": "+ "<strong>"+  vectorTareas.elementAt(posicionActual).getTitulo()+"</strong>" + "?") )
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int idTarea = vectorTareas.elementAt(posicionActual).getId_tarea();
                                new daoTarea().eliminar(idTarea, getActivity());
                                Log.d("cosa", "ELIMINAR TAREA:  id->" + idTarea);
                                cargarTareas(rvf);
                                rvf.setLayoutManager(new StaggeredGridLayoutManager(2,1));
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                break;
            case R.id.mnu_edit_t:
                Log.d("cosa", "EDITAR TAREA");
                Intent pantalla = new Intent(getActivity(),RegistroActivity.class);
                tarea tareaSeleccionada = vectorTareas.elementAt(posicionActual);
                pantalla.putExtra("tipo", 1);
                pantalla.putExtra("abrir", tareaSeleccionada);
                startActivityForResult(pantalla, RESULT_REG);
                break;
        }
        return true;

    }

    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private  int verticalSpaceHeight;
        private int rightSpace;
        private int leftSpace;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }
        public VerticalSpaceItemDecoration(int verticalSpaceHeight, int rightSpace) {
            this.verticalSpaceHeight = verticalSpaceHeight;
            this.rightSpace = rightSpace;
        }
        public VerticalSpaceItemDecoration(){}
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.top = 5;
            outRect.left = 2;
            outRect.right = 2;

        }
    }
}
