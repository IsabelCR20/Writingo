package com.isa.writingo.adaptadores;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isa.writingo.R;
import com.isa.writingo.modelo.nota;
import com.isa.writingo.modelo.recordatorio;
import com.isa.writingo.modelo.tarea;

import java.io.File;
import java.util.Vector;


public class PostitsAdapter extends RecyclerView.Adapter<PostitsAdapter.ViewHolder> {

    private LayoutInflater inflador;
    private Vector<nota> vectorNotas;
    private Vector<tarea> vectorTareas;
    private Vector<recordatorio> vectorRec;
    private Context contexto;
    private int tipo;

    // Métodos privados
    //private View.OnClickListener onClickItemListener;
    private onLongItemClickListener MIonLongClickItemListener;

    public void setOnLongItemClickListener(onLongItemClickListener onLongItemClickListener) {
        MIonLongClickItemListener = onLongItemClickListener;
    }
    public interface onLongItemClickListener {
        void ItemLongClicked(   View v, int position);
    }

    //Constructor del adaptador
    public PostitsAdapter(Context contexto, int tipo, Vector<nota> vectorNotas, Vector<tarea> vectorTareas, Vector<recordatorio> vectorRec){
        inflador = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contexto = contexto;
        if(tipo == 0){ // Notas
            this.vectorNotas = vectorNotas;
        } else {    // Tareas
            this.vectorTareas = vectorTareas;
            this.vectorRec = vectorRec;
        }
        this.tipo = tipo;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titulo;
        public TextView desc;
        public ConstraintLayout contenedor;
        public RecyclerView rvAdjuntos;
        public RecyclerView rvRecordatorios;
        public RecyclerView rvGaleria;
        public RecyclerView rvAudios;
        public RecyclerView rvVideos;
        public RecyclerView rvArchivos;
        public Button btnDeleteReco;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTituloEdit);
            desc = itemView.findViewById(R.id.txtDescripcion);
            contenedor = itemView.findViewById(R.id.contenedor_postit);
            rvRecordatorios = itemView.findViewById(R.id.rvRecordatoriosDeTarea);
            btnDeleteReco = itemView.findViewById(R.id.btnDeleteRec);
            rvGaleria = itemView.findViewById(R.id.rvGaleriaPostit);
            rvAudios = itemView.findViewById(R.id.rvAudiosPostit);
            rvVideos = itemView.findViewById(R.id.rvVideosPostit);
            rvArchivos = itemView.findViewById(R.id.rvArchivosPostit);

        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = inflador.inflate(R.layout.postit_ad, null);
        //vista.setOnLongClickListener(this.onLongClickItemListener);
        return new ViewHolder(vista);
    }

    // Personalizacion de la vista inflada
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Vector<Bitmap> listaBitMaps = new Vector<>();
        Vector<String> rutasImagenes = new Vector<>();
        Vector<String> rutasAudios = new Vector<>();
        Vector<String> rutasVideo = new Vector<>();
        Vector<String> rutasArchivo = new Vector<>();
        String rutas[];
        String gen="NADA";
        File sd = Environment.getExternalStorageDirectory();
        if(tipo == 0){
            nota postit = vectorNotas.elementAt(position);
            holder.titulo.setText(postit.getTitulo());
            holder.desc.setText(postit.getDesc());
            holder.contenedor.setBackgroundTintList(ColorStateList.valueOf(postit.getColor()));
            rutas =postit.getFile().split(",");
            gen = postit.getFile();
            // Evento de click largo
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("cosa", "CLICK LARGOOOO");
                    if (MIonLongClickItemListener != null) {
                        Log.d("cosa", "no null");
                        MIonLongClickItemListener.ItemLongClicked(view, position);
                    }
                    return true;
                }
            });


        } else {
            tarea postit = vectorTareas.elementAt(position);
            holder.titulo.setText(postit.getTitulo());
            holder.desc.setText(postit.getDesc() + "\n* Entrega: " + postit.getFecha_fin());
            holder.contenedor.setBackgroundTintList(ColorStateList.valueOf(postit.getColor()));
            Vector<recordatorio> recosDeTarea = new Vector<>();
            for (recordatorio miReco: vectorRec) {
                if(miReco.getId_tarea() == postit.getId_tarea()){
                    recosDeTarea.add(miReco);
                }
            }
            RecordAdapter adaptador = new RecordAdapter(contexto, recosDeTarea);
            adaptador.setTipo(1);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(contexto);

            holder.rvRecordatorios.setAdapter(adaptador);
            holder.rvRecordatorios.setLayoutManager(lm);
            rutas = postit.getFile().split(",");
            gen = postit.getFile();

            // Evento de click largo
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("cosa", "CLICK LARGOOOO");
                    if (MIonLongClickItemListener != null) {
                        Log.d("cosa", "no null");
                        MIonLongClickItemListener.ItemLongClicked(view, position);
                    }
                    return true;
                }
            });
        }
        for (String x: rutas) {
            Log.d("cosa", x);
            if(x.contains("JPEG_")) {
                File image = new File(x);
                if (image.exists()) {
                    Bitmap mapa = BitmapFactory.decodeFile(image.getAbsolutePath());
                    listaBitMaps.add(mapa);
                    rutasImagenes.add(x);
                } else {
                    Log.d("cosa", "El archivo no existe: " + x);
                }
            } else if(x.contains(("MICRO_"))){
                rutasAudios.add(x);
            } else if(x.contains("VIDEO_")){
                rutasVideo.add(x);
            } else if(x.contains("document")){
                rutasArchivo.add(x);
            }
        }
        // Cosas del recycler de las fotos
        GaleriaAdapter adpatadorGaleria = new GaleriaAdapter(contexto,listaBitMaps, true, rutasImagenes);
        RecyclerView.LayoutManager lmGaleria = new GridLayoutManager(contexto, 2);
        holder.rvGaleria.setAdapter(adpatadorGaleria);
        holder.rvGaleria.setLayoutManager(lmGaleria);
        // Cosas del recycler de los audios
        AudioAdapter adaptadorAudios = new AudioAdapter(contexto,0, rutasAudios);
        RecyclerView.LayoutManager lmAudios = new GridLayoutManager(contexto, 3);
        holder.rvAudios.setLayoutManager(lmAudios);
        holder.rvAudios.setAdapter(adaptadorAudios);
        // Cosas del recycler de los videos
        AudioAdapter adaptadorVideos = new AudioAdapter(contexto, 1, rutasVideo);
        RecyclerView.LayoutManager lmVideos = new GridLayoutManager(contexto, 4);
        holder.rvVideos.setLayoutManager(lmVideos);
        holder.rvVideos.setAdapter(adaptadorVideos);
        //Cosas del recycler de los archivos
        AudioAdapter adaptadorArchivos = new AudioAdapter(contexto, 2, rutasArchivo);
        RecyclerView.LayoutManager lmArchivos = new GridLayoutManager(contexto, 4);
        holder.rvArchivos.setLayoutManager(lmArchivos);
        holder.rvArchivos.setAdapter(adaptadorArchivos);

        // Evento de click largo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("cosa", "CLICK LARGOOOO");
                if (MIonLongClickItemListener != null) {
                    Log.d("cosa", "no null");
                    MIonLongClickItemListener.ItemLongClicked(view, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tipo == 0)
            return vectorNotas.size();
        else
            return  vectorTareas.size();
    }





}
