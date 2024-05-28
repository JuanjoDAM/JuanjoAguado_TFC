package com.juanjo.example.juanjoaguado_tfc;

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

public class VerUsuariosActivity extends AppCompatActivity implements UsuariosAdapter.OnItemClickListener {

    private RecyclerView recyclerViewUsuarios;
    private UsuariosAdapter adapter;
    private List<String> listaCorreosUsuarios;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        listaCorreosUsuarios = new ArrayList<>();
        adapter = new UsuariosAdapter(listaCorreosUsuarios);
        recyclerViewUsuarios.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        obtenerCorreosUsuariosRegistrados();
    }

    private void obtenerCorreosUsuariosRegistrados() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreosUsuarios.clear();
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    String correo = usuarioSnapshot.child("email").getValue(String.class);
                    listaCorreosUsuarios.add(correo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VerUsuariosActivity.this, "Error al obtener los correos electrónicos de los usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        String correoSeleccionado = listaCorreosUsuarios.get(position);
        DatosPersonalesDialog dialogFragment = new DatosPersonalesDialog(true); // true para administrador
        Bundle args = new Bundle();
        args.putString("email", correoSeleccionado);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "DatosPersonalesDialog");
    }
}
