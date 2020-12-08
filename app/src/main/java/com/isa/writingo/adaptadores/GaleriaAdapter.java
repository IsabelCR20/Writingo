package com.isa.writingo.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.isa.writingo.R;

import java.io.File;
import java.net.URI;
import java.util.Vector;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder> {
    private Vector<Bitmap> imagenes;
    private Vector<String> rutasImagenes;
    private LayoutInflater inflador;
    private Context contexto;
    private boolean isPostit;

    public GaleriaAdapter(Context contexto, Vector<Bitmap> imagenes, boolean isPostit, Vector<String> rutasImagenes) {
        this.imagenes = imagenes;
        this.contexto = contexto;
        this.isPostit = isPostit;
        this.rutasImagenes = rutasImagenes;
        inflador = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFotoAd);
        }
    }

    @NonNull
    @Override
    public GaleriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = inflador.inflate(R.layout.img_ad, null);
        return new GaleriaAdapter.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final GaleriaAdapter.ViewHolder holder, final int position) {
        Bitmap imagenBitmap = Bitmap.createScaledBitmap(imagenes.get(position),
                (int) (imagenes.get(position).getWidth()*0.08),
                (int) (imagenes.get(position).getHeight()*0.08), true);
        holder.img.setImageBitmap(/*imagenes.get(position)*/imagenBitmap);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPostit){
                    Bitmap miBitmap = BitmapFactory.decodeFile(rutasImagenes.get(position));
                    AlertDialog.Builder constructor = new AlertDialog.Builder(contexto);    // Crea una alerta encimada xD
                    ImageView foto = new ImageView(contexto);

                    foto.setImageBitmap(miBitmap);

                    Log.d("cosa", "Ruta imagen:" + rutasImagenes.get(position));
                    constructor.setView(foto).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }


}
