package com.isa.writingo;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.isa.writingo.controlador.daoNota;
import com.isa.writingo.fragmentos.PrincipalFragment;
import com.isa.writingo.modelo.nota;
import com.isa.writingo.ui.main.SectionsPagerAdapter;

import java.security.Principal;

public class MainActivity extends AppCompatActivity implements PrincipalFragment.OnFragmentIterationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //getApplicationContext().deleteDatabase("writingo.db");
        //new daoNota().eliminar(19, getApplicationContext());
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        //new daoNota().agregar(new nota("Nueva nota", "Esta es la nueva nota", "15/71/280"), getApplicationContext());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}