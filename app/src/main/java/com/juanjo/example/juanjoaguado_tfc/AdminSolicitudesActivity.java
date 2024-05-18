package com.juanjo.example.juanjoaguado_tfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminSolicitudesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSolicitudes;
    private AdminSolicitudAdapter solicitudAdapter;
    private List<Solicitud> solicitudList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_solicitudes);

        recyclerViewSolicitudes = findViewById(R.id.recyclerViewSolicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this));
        solicitudList = new ArrayList<>();
        solicitudAdapter = new AdminSolicitudAdapter(solicitudList, new AdminSolicitudAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Solicitud solicitud) {
                mostrarDetallesSolicitud(solicitud);
            }
        });
        recyclerViewSolicitudes.setAdapter(solicitudAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("solicitudes_bajas_vacaciones");

        cargarSolicitudes();
    }

    private void cargarSolicitudes() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                solicitudList.clear();
                for (DataSnapshot solicitudSnapshot : snapshot.getChildren()) {
                    Solicitud solicitud = solicitudSnapshot.getValue(Solicitud.class);
                    solicitudList.add(solicitud);
                }
                solicitudAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminSolicitudesActivity.this, "Error al cargar las solicitudes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDetallesSolicitud(Solicitud solicitud) {
        Intent intent = new Intent(this, DetallesSolicitudActivity.class);
        intent.putExtra("solicitud", solicitud);
        startActivity(intent);
    }
}

