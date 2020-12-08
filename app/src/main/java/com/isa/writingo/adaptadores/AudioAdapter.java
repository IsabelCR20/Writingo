package com.isa.writingo.adaptadores;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.isa.writingo.R;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.Inflater;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    Vector<String> rutas;
    Context contexto;
    LayoutInflater inflador;
    private int tipo;
    private MediaPlayer player = null;
    private  boolean  reproduciendo=false;

    public AudioAdapter(Context contexto, int tipo,  Vector<String> rutas){
        this.contexto = contexto;
        this.rutas = rutas;
        inflador = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = inflador.inflate(R.layout.boton_ad, null);
        return new AudioAdapter.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(tipo == 0) {                                                 // Se trata de un adaptador para AUDIO
            holder.btnImagen.setImageResource(R.drawable.play1);
            holder.btnImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!reproduciendo) {
                        holder.btnImagen.setImageResource(R.drawable.stop1);
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(rutas.get(position));
                            player.prepare();
                            player.start();
                            reproduciendo = true;
                        } catch (IOException e) {
                            Log.e("cosa", "prepare() failed");
                        }
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                holder.btnImagen.setImageResource(R.drawable.play1);
                                reproduciendo = false;
                            }
                        });
                    } else {
                        reproduciendo = false;
                        player.release();
                        player = null;
                        holder.btnImagen.setImageResource(R.drawable.play1);
                    }
                }
            });
        } else if (tipo == 1) {                                        // Se trata de un adaptador para VIDEO.
            holder.btnImagen.setImageResource(R.drawable.cinema1);
            holder.btnImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        AlertDialog.Builder constructor = new AlertDialog.Builder(contexto);    // Crea una alerta encimada xD
                        VideoView vv = new VideoView(contexto);             // Componente que mostrará el video.
                        vv.setVideoPath(rutas.get(position));               // Asigna la ruta que le corresponde
                        vv.start();
                        constructor.setView(vv).show();
                    } catch(Exception ex){
                        Log.d("cosa", "Error al ver video: " + ex.getMessage());
                    }
                }
            });
        } else if(tipo == 2){                                         // Se trata de un adaptador para ARCHIVOS PDF
            holder.btnImagen.setImageResource(R.drawable.file1);
            holder.btnImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.d("cosa", rutas.get(position));
                        Dialog cuadroDialogo = new Dialog(contexto);               // Definicion del cuadro de dialogo que abre el xml
                        cuadroDialogo.setContentView(R.layout.visor_pdf);
                        PDFView visorPdf = cuadroDialogo.findViewById(R.id.pdfvVisor);
                        visorPdf.fromUri(Uri.parse(rutas.get(position))).load();
                        cuadroDialogo.show();
                    } catch(Exception ex){
                        Log.d("cosa", "Error al ver archivo: " + ex.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton btnImagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnImagen = itemView.findViewById(R.id.btnImgAudio);
        }
    }
}
