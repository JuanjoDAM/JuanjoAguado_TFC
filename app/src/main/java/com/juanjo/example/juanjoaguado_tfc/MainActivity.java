package com.juanjo.example.juanjoaguado_tfc;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegistrarHoras;
    private Button buttonBajasVacaciones;
    private Button buttonNominas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegistrarHoras = findViewById(R.id.buttonRegistrarHoras);
        buttonBajasVacaciones = findViewById(R.id.buttonBajasVacaciones);
        buttonNominas = findViewById(R.id.buttonNominas);

        buttonRegistrarHoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistroHoras();
            }
        });

        buttonBajasVacaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadBajasVacaciones();
            }
        });

        buttonNominas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadNominas();
            }
        });
    }

    private void mostrarDialogoRegistroHoras() {
        DialogFragment dialogFragment = new RegistroHorasDialog();
        dialogFragment.show(getSupportFragmentManager(), "RegistroHorasDialog");
    }

    private void abrirActividadBajasVacaciones() {
        Intent intent = new Intent(MainActivity.this, BajasVacacionesActivity.class);
        startActivity(intent);
    }

    private void abrirActividadNominas() {
        Intent intent = new Intent(MainActivity.this, NominasActivity.class);
        startActivity(intent);
    }
}
