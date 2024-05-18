package com.juanjo.example.juanjoaguado_tfc;


import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;


public class AdminSolicitudesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSolicitudes;
    private SolicitudAdapter solicitudAdapter;
    private List<Solicitud> solicitudList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_solicitudes);

        recyclerViewSolicitudes = findViewById(R.id.recyclerViewSolicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this));
        solicitudList = new ArrayList<>();
        solicitudAdapter = new SolicitudAdapter(solicitudList, new SolicitudAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Solicitud solicitud) {
                // Mostrar diálogo para cambiar el estado de la solicitud
                mostrarDialogoCambioEstado(solicitud);
            }
        });
        recyclerViewSolicitudes.setAdapter(solicitudAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("solicitudes_bajas_vacaciones");

        cargarSolicitudes();
    }

    private void cargarSolicitudes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                solicitudList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Solicitud solicitud = snapshot.getValue(Solicitud.class);
                    solicitudList.add(solicitud);
                }
                solicitudAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminSolicitudesActivity.this, "Error al cargar las solicitudes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCambioEstado(Solicitud solicitud) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar estado de la solicitud");

        String[] estados = {"Aceptado", "Rechazado"};
        builder.setItems(estados, (dialog, which) -> {
            String nuevoEstado = estados[which];
            cambiarEstadoSolicitud(solicitud, nuevoEstado);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void cambiarEstadoSolicitud(Solicitud solicitud, String nuevoEstado) {
        databaseReference.child(solicitud.getId()).child("estado").setValue(nuevoEstado)
                .addOnSuccessListener(aVoid -> Toast.makeText(AdminSolicitudesActivity.this, "Estado actualizado", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AdminSolicitudesActivity.this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show());
    }
}



