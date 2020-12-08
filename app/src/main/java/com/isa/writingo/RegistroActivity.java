package com.isa.writingo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.isa.writingo.adaptadores.AudioAdapter;
import com.isa.writingo.adaptadores.GaleriaAdapter;
import com.isa.writingo.adaptadores.RecordAdapter;
import com.isa.writingo.controlador.daoNota;
import com.isa.writingo.controlador.daoRecordatorio;
import com.isa.writingo.controlador.daoTarea;
import com.isa.writingo.fragmentos.RegistroRecordatoriosFragment;
import com.isa.writingo.modelo.nota;
import com.isa.writingo.modelo.recordatorio;
import com.isa.writingo.modelo.tarea;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import yuku.ambilwarna.AmbilWarnaDialog;

public class RegistroActivity extends AppCompatActivity{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int PICK_PDF_FILE = 3;
    Vector<Bitmap> listaImagenes;
    Vector<String> listaRutasAudios;
    Vector<String> listaRutasVideos;
    Vector<String> listaRutasArchivos;
    Vector<String> listaRutasImagenes;
    Vector<recordatorio> vectorReco;
    OutputStream output;
    private boolean grabando = false;
    private MediaRecorder recorder;
    private String fileName;
    private AlarmManager alarmMgr;
    private PendingIntent alarmPendingIntent;
    //
    ImageButton btnCamara;
    ImageButton btnArchivos;
    ImageButton btnMicro;
    ImageButton btnVideo;
    ImageButton btnPalette;
    Button btnGuardar;
    Button btnColorSelected;
    TextView txtTitulo;
    TextView txtDesc;
    RecyclerView rvAdjuntos;
    RecyclerView rvAudiosAdj;
    RecyclerView rvVideosAdj;
    RecyclerView rvArchivosAdj;
    int tipo;
    int selectedColor = R.color.colorRosaSuave;
    //
    TextView fechaEntrega;
    TextView horaEntrega;
    private String fechaCump;
    private  String horaCump;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaImagenes = new Vector<>();
        listaRutasAudios = new Vector<>();
        listaRutasVideos = new Vector<>();
        listaRutasArchivos = new Vector<>();
        listaRutasImagenes = new Vector<>();    // Se usa en editar
        Intent miIntento = getIntent();
        tipo = miIntento.getIntExtra("tipo", 0);

        setContentView(R.layout.activity_registro);

        btnCamara = findViewById(R.id.btnCamara);
        btnArchivos = findViewById(R.id.btnFiles);
        btnMicro = findViewById(R.id.btnMicro);
        btnGuardar = findViewById(R.id.btnGuardarRegistro);
        btnVideo = findViewById(R.id.btnVideo);
        btnPalette = findViewById(R.id.btnPalette);
        btnColorSelected = findViewById(R.id.btnColorSelected);
        txtTitulo = findViewById(R.id.txtTituloEdit);
        txtDesc = findViewById(R.id.txtDescEdit);
        selectedColor = ContextCompat.getColor(getApplicationContext(),R.color.colorRosaSuave);
        rvAdjuntos = findViewById(R.id.rvAdjuntos);
        rvAudiosAdj = findViewById(R.id.rvAudiosAdjuntos);
        rvVideosAdj = findViewById(R.id.rvVideosAdjuntos);
        rvArchivosAdj = findViewById(R.id.rvArchivosAdjuntos);
        //


        //  Eventos
        btnCamara.setOnClickListener(click_btnCamara);
        btnArchivos.setOnClickListener(click_btnArchivos);
        btnMicro.setOnClickListener(click_btnMicro);
        btnVideo.setOnClickListener(click_btnVideo);
        btnGuardar.setOnClickListener(click_btnGuardar);
        btnPalette.setOnClickListener(click_btnPalette);

        if(tipo == 0){ // Registrar nota
            this.setTitle(R.string.tRegistroN);
        } else {
            this.setTitle(R.string.tRegistroT);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contenedorFrag,new RegistroRecordatoriosFragment(), "fragmentoReco")
                    .commit();
            Log.d("cosa", "Ya paso por la creacion del fragmento");
        }

        // Comprobar si se trata de una edicion
        if(getIntent().hasExtra("abrir")){
            String rutas[];
            int color;
            if(tipo == 0){
                nota precargada = (nota)getIntent().getSerializableExtra("abrir");
                txtTitulo.setText(precargada.getTitulo());
                txtDesc.setText(precargada.getDesc());
                rutas = precargada.getFile().split(",");
                color = precargada.getColor();
            } else {
                tarea precargada = (tarea)getIntent().getSerializableExtra("abrir");
                txtTitulo.setText(precargada.getTitulo());
                txtDesc.setText(precargada.getDesc());
                rutas = precargada.getFile().split(",");
                color = precargada.getColor();
                // Obtención de info de recordatorios
                 vectorReco = new Vector<>();
                Cursor listaReco = (new daoRecordatorio().verTodos(getApplicationContext()));
                if (listaReco.getCount() > 0)
                    listaReco.moveToFirst();

                while (!listaReco.isAfterLast()) {
                    recordatorio nuevoR = new recordatorio(listaReco.getInt(listaReco.getColumnIndex(DB.C_ID_R)),
                            listaReco.getInt(listaReco.getColumnIndex(DB.C_IDTAREA)),
                            listaReco.getString(listaReco.getColumnIndex(DB.C_FECHA_R)),
                            listaReco.getString(listaReco.getColumnIndex(DB.C_HORA_R)));
                    if(nuevoR.getId_tarea() == precargada.getId_tarea())
                        vectorReco.add(nuevoR);
                    listaReco.moveToNext();
                }
                this.horaCump = (precargada.getHora_fin());
                this.fechaCump = (precargada.getFecha_fin());
            }
            // Asignación de archivos adjuntos
            for (String ruta: rutas) {
                if(ruta.contains("JPEG_")){
                    listaRutasImagenes.add(ruta);
                    File image = new File(ruta);
                    if (image.exists()) {
                        Bitmap mapa = BitmapFactory.decodeFile(image.getAbsolutePath());
                        listaImagenes.add(mapa);
                    } else {
                        Log.d("cosa", "El archivo no existe (editar): " + ruta);
                    }
                } else if(ruta.contains("MICRO_")){
                    listaRutasAudios.add(ruta);
                } else if(ruta.contains("VIDEO_")){
                    listaRutasVideos.add(ruta);
                } else if(ruta.contains("document")){
                    listaRutasArchivos.add(ruta);
                }
            }
            // Cosas del recycler de las fotos
            GaleriaAdapter adpatadorGaleria = new GaleriaAdapter(getApplicationContext(),listaImagenes, false, listaRutasImagenes);
            RecyclerView.LayoutManager lmGaleria = new GridLayoutManager(getApplicationContext(), 4);
            rvAdjuntos.setAdapter(adpatadorGaleria);
            rvAdjuntos.setLayoutManager(lmGaleria);
            // Cosas del recycler de los audios
            AudioAdapter adaptadorAudios = new AudioAdapter(getApplicationContext(),0, listaRutasAudios);
            RecyclerView.LayoutManager lmAudios = new GridLayoutManager(getApplicationContext(), 4);
            rvAudiosAdj.setLayoutManager(lmAudios);
            rvAudiosAdj.setAdapter(adaptadorAudios);
            // Cosas del recycler de los videos
            AudioAdapter adaptadorVideos = new AudioAdapter(getApplicationContext(), 1, listaRutasVideos);
            RecyclerView.LayoutManager lmVideos = new GridLayoutManager(getApplicationContext(), 4);
            rvVideosAdj.setLayoutManager(lmVideos);
            rvVideosAdj.setAdapter(adaptadorVideos);
            //Cosas del recycler de los archivos
            AudioAdapter adaptadorArchivos = new AudioAdapter(getApplicationContext(), 2, listaRutasArchivos);
            RecyclerView.LayoutManager lmArchivos = new GridLayoutManager(getApplicationContext(), 4);
            rvArchivosAdj.setLayoutManager(lmArchivos);
            rvArchivosAdj.setAdapter(adaptadorArchivos);
            // Color
            selectedColor = color;
            btnColorSelected.setBackgroundColor(color);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(tipo==1 && getIntent().hasExtra("abrir")){
            RecordAdapter adaptadorRecos = new RecordAdapter(RegistroActivity.this,vectorReco);
            FragmentManager fm = getSupportFragmentManager();
            if(fm.findFragmentByTag("fragmentoReco") != null) {
                Log.d("cosa", "HOLA!!1");
                RegistroRecordatoriosFragment rrf = (RegistroRecordatoriosFragment) fm.findFragmentById(R.id.contenedorFrag);
                rrf.setListaRec(vectorReco);

                ((TextView) rrf.getView().findViewById(R.id.txtFechaCump)).setText(fechaCump.substring(0,10));
                ((TextView) rrf.getView().findViewById(R.id.txtHoraCump)).setText(fechaCump.substring(11));
                ((RecyclerView) rrf.getView().findViewById(R.id.rvRecordatorios)).setAdapter(adaptadorRecos);
                ((RecyclerView) rrf.getView().findViewById(R.id.rvRecordatorios)).setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            } else {
                Log.d("cosa", "ADIOS!!1");
            }
        }
    }

    private File tmpFile;
    private View.OnClickListener click_btnCamara = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnCamara.setImageResource(R.drawable.camara1_click);
            Handler h =new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnCamara.setImageResource(R.drawable.camara1);
                }
            }, 80);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEGTEMP_"  + timeStamp; //make a better file name
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;
            try {
                image = File.createTempFile(imageFileName,
                        ".jpg",
                        storageDir
                );
                tmpFile = image;
                Uri imageUri = FileProvider.getUriForFile(
                        getApplicationContext(),
                        "com.example.android.fileprovider", //(use your app signature + ".provider" )
                        image);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    private View.OnClickListener click_btnArchivos = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnArchivos.setImageResource(R.drawable.folder1_click);
            Handler h =new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnArchivos.setImageResource(R.drawable.folder1);
                }
            }, 80);
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            startActivityForResult(intent, PICK_PDF_FILE);
        }
    };


    private View.OnClickListener click_btnMicro = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!grabando) {
                grabando = true;
                btnMicro.setImageResource(R.drawable.micro1_click);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnMicro.setImageResource(R.drawable.micro1_reco);
                    }
                }, 80);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                fileName = getExternalFilesDir(Environment.DIRECTORY_PODCASTS) + "/MICRO_" + timeStamp + ".3gp";

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setOutputFile(fileName);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                Log.d("cosa", "audio"+fileName);
                try {
                    recorder.prepare();
                    Log.d("cosa", "preparado.");
                    recorder.start();
                    Log.d("cosa", "inicio");
                } catch (IOException e) {
                    Log.e("cosa", "prepare() failed");
                }

            } else {
                btnMicro.setImageResource(R.drawable.micro1);
                grabando = false;
                recorder.stop();
                recorder.release();
                recorder = null;
                listaRutasAudios.add(fileName);
                AudioAdapter adaptadorAudio = new AudioAdapter(getApplicationContext(),0, listaRutasAudios);
                RecyclerView.LayoutManager lmAudio = new GridLayoutManager(getApplicationContext(),4);
                rvAudiosAdj.setAdapter(adaptadorAudio);
                rvAudiosAdj.setLayoutManager(lmAudio);
            }
        }
    };

    private View.OnClickListener click_btnVideo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            btnVideo.setImageResource(R.drawable.video1_click);
            Handler h =new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnVideo.setImageResource(R.drawable.video1);
                }
            }, 80);

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    };

    private View.OnClickListener click_btnPalette = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnPalette.setImageResource(R.drawable.palette_click);
            Handler h =new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnPalette.setImageResource(R.drawable.paleta);
                }
            }, 80);
            openColorPicker();
        }
    };
    
    private View.OnClickListener click_btnGuardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(txtTitulo.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Favor de introducir un título.", Toast.LENGTH_SHORT).show();
            } else {
                String files="";    // Rutas de todos los archivos adjuntos
                for (Bitmap x: listaImagenes) {             // Obtener cada uno de los mapas de bits chiquitos para guardarlos
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    /*if(getIntent().hasExtra("abrir")){
                        try {
                            String fecha = ((nota) getIntent().getSerializableExtra("abrir")).getFecha();
                            Date fechaDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy" , Locale.US).parse(fecha);
                            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(fechaDate);
                        } catch (ParseException e) {
                            Log.d("cosa", "error en algo de la fecha, checar por guardar fotos");
                        }
                    }*/
                    String imageFileName = "JPEG_" + timeStamp;
                    try {
                        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File image = new File(storageDir,imageFileName+".jpg");
                        //image.createNewFile();
                        output = new FileOutputStream(image);
                        //Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(x, SOME_WIDTH);

                        boolean res = x.compress(Bitmap.CompressFormat.JPEG, 100, output);
                        Log.d("cosa", "File guardado : " +image.getAbsolutePath());
                        Log.d("cosa", "out" +  res);
                        output.flush();
                        output.close();
                        files += image.getAbsolutePath() + ",";
                        TimeUnit.SECONDS.sleep(1);
                    }catch(Exception ex){
                        Log.d("cosa", "Exepcion file : " +ex.getMessage());
                    }
                }
                for (String rutaAudio: listaRutasAudios) {
                    files += rutaAudio + ",";
                }
                for (String rutaVideo : listaRutasVideos){
                    files += rutaVideo + ",";
                }
                for (String rutaArchivo : listaRutasArchivos){
                    files += rutaArchivo + ",";
                }
                if(tipo == 0){  // Agregar nota
                    nota nuevo = new nota(txtTitulo.getText().toString(),
                            txtDesc.getText().toString(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                            selectedColor,
                            files);
                    if(getIntent().hasExtra("abrir")){
                        try{
                            daoNota dao = new daoNota();
                            String fecha = ((nota) getIntent().getSerializableExtra("abrir")).getFecha();
                            Date fechaDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy" , Locale.US).parse(fecha);
                            nuevo.setFecha(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fechaDate));
                            nuevo.setId_nota(((nota) getIntent().getSerializableExtra("abrir")).getId_nota());
                            dao.editar(nuevo, getApplicationContext());
                            Log.d("cosa", "Titulo: " + nuevo.getTitulo() + "   Fecha: " + nuevo.getFecha() + "  ID: " + nuevo.getId_nota());
                            Toast.makeText(getApplicationContext(), "Registro modificado", Toast.LENGTH_SHORT).show();
                            Intent retorno = new Intent();
                            //retorno.putExtra("nuevo", nuevo);
                            retorno.putExtra("tipo", 0);
                            setResult(RegistroActivity.RESULT_OK, retorno);
                            RegistroActivity.super.onBackPressed();
                        } catch(Exception ex){
                            Toast.makeText(getApplicationContext(), "Fallo al editar nota " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            daoNota dao = new daoNota();
                            dao.agregar(nuevo, getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Registro agregado", Toast.LENGTH_SHORT).show();
                            Intent retorno = new Intent();
                            //retorno.putExtra("nuevo", nuevo);
                            retorno.putExtra("tipo", 0);
                            setResult(RegistroActivity.RESULT_OK, retorno);
                            RegistroActivity.super.onBackPressed();
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Fallo al agregar nota " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {        // agregar tarea
                    FrameLayout frame = findViewById(R.id.contenedorFrag);
                    fechaEntrega = frame.findViewById(R.id.txtFechaCump);
                    horaEntrega = frame.findViewById(R.id.txtHoraCump);

                    try{
                        tarea nueva = new tarea(txtTitulo.getText().toString(),
                                txtDesc.getText().toString(),
                                //fechaEntrega.getText().toString()+","+horaEntrega.getText().toString()),
                                fechaEntrega.getText().toString(),
                                horaEntrega.getText().toString(),
                                selectedColor,
                                files,
                                1);
                        daoTarea dao = new daoTarea();
                        dao.agregar(nueva, getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Registro agregado", Toast.LENGTH_SHORT).show();
                        int lastIdx = dao.ultimo(getApplicationContext());
                        daoRecordatorio daor = new daoRecordatorio();
                        FragmentManager fm = getSupportFragmentManager();
                        if(fm.findFragmentById(R.id.contenedorFrag) != null){
                            RegistroRecordatoriosFragment rrf = (RegistroRecordatoriosFragment) fm.findFragmentById(R.id.contenedorFrag);
                            Vector<recordatorio> recosDeEsta= rrf.getListaRec();
                            Log.d("cosa", "Lista de recos total: "+recosDeEsta.size());
                            int IdAlarma = 10;
                            for (recordatorio rec: recosDeEsta) {
                                rec.setId_tarea(lastIdx);
                                daor.agregar(rec, getApplicationContext());
                                Log.d("cosa", "* "+rec.getId_rec() + " " + rec.getId_tarea() + " FECHA:" + rec.getFecha());
                                // Por cada recordatorio de la lista, agregar una alarma.
                                // Ajuste de fecha y hora
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                                Date miFechaHora = sdf.parse(rec.getFecha() + " " + rec.getHora());
                                String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(miFechaHora).toString();
                                Date fecha_final = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fecha);
                                Log.d("cosa", "Fecha final: " + fecha_final.toString());
                                // Ajuste de alarma

                                alarmMgr = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                                Intent alarmIntent = new Intent(getApplicationContext(), RecibirAlarma.class);
                                alarmIntent.putExtra("mensaje", "Recordatorio: " + nueva.getTitulo());
                                alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), IdAlarma++, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
                                alarmIntent.setData(Uri.parse("custom://" + System.currentTimeMillis()));
                                Calendar calendario = Calendar.getInstance();
                                calendario.setTime(fecha_final);
                                Log.d("cosa","Calndario -> Hora: " + calendario.getTime().toString());
                                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), alarmPendingIntent);
                            }
                        }

                        Intent retorno = new Intent();
                        //retorno.putExtra("nuevo", nueva);
                        retorno.putExtra("tipo", 1);
                        setResult(RegistroActivity.RESULT_OK, retorno);
                        RegistroActivity.super.onBackPressed();
                    } catch(Exception ex){
                        Toast.makeText(getApplicationContext(), "Fallo al agregar tarea " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try{
                        int dia = Integer.parseInt(fechaEntrega.getText().toString().substring(0,1));
                        int mes = Integer.parseInt(fechaEntrega.getText().toString().substring(3,4));
                        int anio = Integer.parseInt(fechaEntrega.getText().toString().substring(6));
                        int hora = Integer.parseInt(horaEntrega.getText().toString().substring(0,1));
                        int min = Integer.parseInt(horaEntrega.getText().toString().substring(3,4));
                        if(horaEntrega.getText().toString().contains("PM")) hora+=12;

                        // Ajuste de fecha y hora
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                        Date miFechaHora = sdf.parse( fechaEntrega.getText().toString() + " " + horaEntrega.getText().toString());
                        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(miFechaHora).toString();
                        Date fecha_final = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fecha);
                        Log.d("cosa", "Fecha final: " + fecha_final.toString());
                        // Ajuste de alarma

                        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                        Intent alarmIntent = new Intent(getApplicationContext(), RecibirAlarma.class);
                        alarmIntent.putExtra("mensaje", "FIN de tarea: " + txtTitulo.getText().toString());
                        alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
                        alarmIntent.setData(Uri.parse("custom://" + System.currentTimeMillis()));
                        Calendar calendario = Calendar.getInstance();
                        calendario.setTime(fecha_final);
                        Log.d("cosa","Calndario -> Hora: " + calendario.getTime().toString());
                        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), alarmPendingIntent);
                    } catch(Exception ex){
                        Log.d("cosa", "Agregar alarmas fallo: " +ex.getMessage());
                    }
                }
            }
        }
    };

    public void openColorPicker(){
        AmbilWarnaDialog selector = new AmbilWarnaDialog(this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                btnColorSelected.setBackgroundColor(color);
            }
        });
        selector.show();
    }
    //
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //AlertDialog.Builder constructor = new AlertDialog.Builder(RegistroActivity.this);    // Crea una alerta encimada xD
            //ImageView foto = new ImageView(RegistroActivity.this);
            //Bitmap bitmap = decodeSampledBitmapFromFile(tmpFile.getAbsolutePath(), 2000, 1700);
            Bitmap miBitmap = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());

            //Bitmap bitmap =
            //foto.setImageBitmap(bitmap);
            //constructor.setView(foto).show();

            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            listaImagenes.add(miBitmap);
            RecyclerView.LayoutManager lm = new GridLayoutManager(getApplicationContext(),4);
            rvAdjuntos.setLayoutManager(lm);
            GaleriaAdapter adaptador = new GaleriaAdapter(getApplication(),listaImagenes, false, null);
            rvAdjuntos.setAdapter(adaptador);
            File archivoCamara = new File(getOriginalImagePath());
            archivoCamara.delete();

            Log.d("cosa", "Foto ruta: " + tmpFile.getAbsolutePath());
            //Log.d("cosa", "Foto original: " + archivoCamara.getAbsolutePath());
           // Log.d("cosa", "Foto URI2: " + FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", new File(mCurrentPhotoPath)));
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream inputStream = videoAsset.createInputStream();
                File archivoVideo = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/VIDEO_" + timeStamp + ".mp4");
                FileOutputStream outputStream = new FileOutputStream(archivoVideo);

                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                inputStream.close();
                outputStream.close();

                listaRutasVideos.add(archivoVideo.getAbsolutePath());
                AudioAdapter adaptadorVideo = new AudioAdapter(RegistroActivity.this,1,listaRutasVideos);
                RecyclerView.LayoutManager lmVideo = new GridLayoutManager(getApplicationContext(), 4);
                rvVideosAdj.setAdapter(adaptadorVideo);
                rvVideosAdj.setLayoutManager(lmVideo);
            } catch(Exception ex){
                Log.d("cosa", "Error al guardar video: " + ex.getMessage());
            }
        }
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                listaRutasArchivos.add(uri.toString());
                //Log.d("cosa", uri.toString());
                AudioAdapter adaptadorArchivos = new AudioAdapter(RegistroActivity.this,2, listaRutasArchivos);
                RecyclerView.LayoutManager lmArchivos = new GridLayoutManager(getApplicationContext(), 4);
                rvArchivosAdj.setAdapter(adaptadorArchivos);
                rvArchivosAdj.setLayoutManager(lmArchivos);
            }
        }
    }

    public String getOriginalImagePath() {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = RegistroActivity.this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }

}