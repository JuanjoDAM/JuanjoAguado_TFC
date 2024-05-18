package com.juanjo.example.juanjoaguado_tfc;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class VerSolicitudesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSolicitudes;
    private SolicitudesAdapter solicitudesAdapter;
    private List<Solicitud> solicitudesList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_solicitudes);

        recyclerViewSolicitudes = findViewById(R.id.recyclerViewSolicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this));

        solicitudesList = new ArrayList<>();
        solicitudesAdapter = new SolicitudesAdapter(solicitudesList);
        recyclerViewSolicitudes.setAdapter(solicitudesAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("solicitudes_bajas_vacaciones");

        cargarSolicitudes();
    }

    private void cargarSolicitudes() {
        String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (username != null) {
            databaseReference.orderByChild("userId").equalTo(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    solicitudesList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Solicitud solicitud = dataSnapshot.getValue(Solicitud.class);
                        solicitudesList.add(solicitud);
                    }
                    solicitudesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar errores aquí
                }
            });
        }
    }
}

