package com.isa.writingo.adaptadores;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isa.writingo.R;
import com.isa.writingo.R;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isa.writingo.modelo.nota;
import com.isa.writingo.modelo.recordatorio;

import java.util.Vector;

import static android.os.Build.VERSION_CODES.R;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private LayoutInflater inflador;
    private Vector<recordatorio> vector;
    private Context contexto;
    private int tipo;

    public RecordAdapter(Context contexto, Vector<recordatorio> vector){
        inflador = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vector = vector;
        this.contexto = contexto;
    }
    public void setTipo(int tipo){
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = inflador.inflate(com.isa.writingo.R.layout.record_ad, null);
        //LinearLayoutManager.LayoutParams parms2;// = (LinearLayoutManager.LayoutParams) holder.imgReloj.getLayoutParams();
        GridLayoutManager.LayoutParams lp;
        //LinearLayoutManager.
        return new RecordAdapter.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        recordatorio rec = vector.elementAt(position);
        holder.fechaR.setText(rec.getFecha() + ", " + rec.getHora());
        holder.btnDeleteRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnDeleteRec.setImageResource(com.isa.writingo.R.drawable.delete2_click);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.btnDeleteRec.setImageResource(com.isa.writingo.R.drawable.delete2);
                    }
                }, 80);
                vector.remove(position);
                notifyItemRemoved(position);
            }
        });
        if(this.tipo == 1) {
            holder.fechaR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            holder.btnDeleteRec.setVisibility(View.INVISIBLE);

            int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, contexto.getResources().getDisplayMetrics());
            ConstraintLayout.LayoutParams parms = (ConstraintLayout.LayoutParams) holder.imgReloj.getLayoutParams();
            parms.height = dimensionInDp;
            parms.width = dimensionInDp;
            //parms.setMargins(8,12,2,0);
            Log.d("cosa", "dp" + dimensionInDp);
            holder.imgReloj.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.imgReloj.setLayoutParams(parms);


        }
    }

    @Override
    public int getItemCount() {
        return vector.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fechaR;
        public ImageButton btnDeleteRec;
        public ImageView imgReloj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaR = itemView.findViewById(com.isa.writingo.R.id.txtMostrarRec);
            btnDeleteRec = itemView.findViewById(com.isa.writingo.R.id.btnDeleteRec);
            imgReloj = itemView.findViewById(com.isa.writingo.R.id.imgReloj);
            //img = itemView.findViewById(com.isa.writingo.R.id.)
        }
    }



}
