package com.juanjo.example.juanjoaguado_tfc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

public class DetallesSolicitudActivity extends AppCompatActivity {

    private TextView textViewEmail, textViewMotivo, textViewTipo, textViewInicio, textViewFin, textViewEstado;
    private Button buttonAceptar, buttonRechazar;
    private Solicitud solicitud;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_solicitud);

        textViewEmail = findViewById(R.id.textViewEmail);
        textViewMotivo = findViewById(R.id.textViewMotivo);
        textViewTipo = findViewById(R.id.textViewTipo);
        textViewInicio = findViewById(R.id.textViewInicio);
        textViewFin = findViewById(R.id.textViewFin);
        textViewEstado = findViewById(R.id.textViewEstado);
        buttonAceptar = findViewById(R.id.buttonAceptar);
        buttonRechazar = findViewById(R.id.buttonRechazar);

        solicitud = (Solicitud) getIntent().getSerializableExtra("solicitud");
        databaseReference = FirebaseDatabase.getInstance().getReference("solicitudes_bajas_vacaciones");

        mostrarDetalles();

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEstadoSolicitud("aceptado");
            }
        });

        buttonRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEstadoSolicitud("rechazado");
            }
        });
    }

    private void mostrarDetalles() {
        textViewEmail.setText(solicitud.getUserId());
        textViewMotivo.setText(solicitud.getMotivo());
        textViewTipo.setText(solicitud.getTipo());
        textViewInicio.setText(solicitud.getFechaInicio());
        textViewFin.setText(solicitud.getFechaFin());
        textViewEstado.setText(solicitud.getEstado());
    }

    private void actualizarEstadoSolicitud(String nuevoEstado) {
        solicitud.setEstado(nuevoEstado);
        databaseReference.child(solicitud.getId()).setValue(solicitud).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DetallesSolicitudActivity.this, "Solicitud " + nuevoEstado, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetallesSolicitudActivity.this, "Error al actualizar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


