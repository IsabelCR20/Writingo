package com.isa.writingo.fragmentos;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isa.writingo.R;
import com.isa.writingo.adaptadores.RecordAdapter;
import com.isa.writingo.modelo.recordatorio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class RegistroRecordatoriosFragment extends Fragment {
    TextView fecha;
    TextView hora;
    ImageButton btnAddRec;
    TextView txtFechaRec;
    TextView txtHoraRec;
    RecyclerView rvRec;
    Vector<recordatorio> listaRec = new Vector<recordatorio>();
    RecordAdapter adaptador;

    public Vector<recordatorio> getListaRec() {
        return listaRec;
    }
    public void setListaRec(Vector<recordatorio> vector) { listaRec = vector; }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String horaHoy = new SimpleDateFormat("hh:mm aa").format(new Date());

        View vista = inflater.inflate(R.layout.fragment_recordatorios, null);
        fecha = vista.findViewById(R.id.txtFechaCump);
        hora = vista.findViewById(R.id.txtHoraCump);
        btnAddRec = vista.findViewById(R.id.btnAddRec);
        txtFechaRec = vista.findViewById(R.id.txtFechaRec);
        txtHoraRec = vista.findViewById(R.id.txtHoraRec);
        rvRec = vista.findViewById(R.id.rvRecordatorios);

        fecha.setText(fechaHoy);
        hora.setText(horaHoy);

        fecha.setOnClickListener(clickFecha);
        hora.setOnClickListener(clickHora);
        txtFechaRec.setOnClickListener(clickFechaRec);
        txtHoraRec.setOnClickListener(clickHoraRec);
        btnAddRec.setOnClickListener(clickAddRec);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        //lm.setMeasuredDimension(100,100);

        adaptador = new RecordAdapter(getActivity(), listaRec);
        rvRec.setAdapter(adaptador);
        rvRec.setLayoutManager(lm);
        Log.d("cosa", "me creé");
        return vista;
    }

    View.OnClickListener clickFecha = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //FragmentManager fm = FragmentActivity
            DialogFragment dateFragment = DateTimePickerFragment.newInstace(fecha, 0);
            dateFragment.show(getChildFragmentManager(), "datePicker");
        }
    };
    View.OnClickListener clickHora = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogFragment dateFragment = DateTimePickerFragment.newInstace(hora, 1);
            dateFragment.show(getChildFragmentManager(), "datePicker");
        }
    };
    View.OnClickListener clickFechaRec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogFragment dateFragment = DateTimePickerFragment.newInstace(txtFechaRec, 0);
            dateFragment.show(getChildFragmentManager(), "datePicker");
        }
    };
    View.OnClickListener clickHoraRec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogFragment dateFragment = DateTimePickerFragment.newInstace(txtHoraRec, 1);
            dateFragment.show(getChildFragmentManager(), "datePicker");
        }
    };
    View.OnClickListener clickAddRec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnAddRec.setImageResource(R.drawable.add1_click);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnAddRec.setImageResource(R.drawable.add1);
                }
            }, 80);
            if(txtFechaRec.getText().toString().equals("") || txtHoraRec.getText().toString().equals("")){
                Toast.makeText(getActivity(),"Favor de ingresar una fecha y hora de recordatorio",Toast.LENGTH_SHORT).show();
            } else {
                recordatorio nuevo = new recordatorio(txtFechaRec.getText().toString(), txtHoraRec.getText().toString());
                listaRec.add(nuevo);
                adaptador.notifyItemInserted(listaRec.size() - 1);
            }
        }
    };



}


