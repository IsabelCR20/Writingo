package com.isa.writingo.fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private TextView caja;
    private Calendar c;
    private int tipo;

    // Pasar la caja al constructor de la clase, no es posible por ese medio, aspiq ue se crea newInstace
    public static DateTimePickerFragment newInstace(TextView caja, int tipo) {
        DateTimePickerFragment dtpFragmento = new DateTimePickerFragment();
        dtpFragmento.setTextView(caja);
        dtpFragmento.setTipo(tipo);
        return dtpFragmento;
    }

    public void setTextView(TextView caja) {
        this.caja = caja;
    }
    public void setTipo(int tipo){
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Fecha actual
        if(this.tipo == 0){
            c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);

            if (this.caja.getText().toString().length() > 0) {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Date parsedDate = null;
                try {
                    parsedDate = formato.parse(this.caja.getText().toString());
                    c.setTime(parsedDate);
                    anio = c.get(Calendar.YEAR);
                    mes = c.get(Calendar.MONTH);
                    dia = c.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return new DatePickerDialog(getActivity(), this, anio, mes, dia);
        } else {
            c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int minuto = c.get(Calendar.MINUTE);
            if (this.caja.getText().toString().length() > 0) {
                SimpleDateFormat formato = new SimpleDateFormat("hh:mm aa");
                Date parsedTime = null;
                try {
                    parsedTime = formato.parse(this.caja.getText().toString());
                    c.setTime(parsedTime);
                    hora = c.get(Calendar.HOUR_OF_DAY);
                    minuto = c.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return new TimePickerDialog(getActivity(), this, hora, minuto,
                    DateFormat.is24HourFormat(getActivity()));
        }

    }

    public void onDateSet(DatePicker vista, int anio, int mes, int dia) {
        String selectedDate = String.format("%02d", dia) + "/" + String.format("%02d", (mes + 1)) + "/" + String.format("%04d", anio);

        this.caja.setText(selectedDate);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hora, int min) {
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, min);
        SimpleDateFormat formato = new SimpleDateFormat("hh:mm aa");

        String selectedTime = formato.format(c.getTime());
        this.caja.setText(selectedTime);
    }
}