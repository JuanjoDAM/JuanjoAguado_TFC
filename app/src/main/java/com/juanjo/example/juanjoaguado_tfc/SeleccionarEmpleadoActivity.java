package com.juanjo.example.juanjoaguado_tfc;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

// SeleccionarEmpleadoActivity.java

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

public class SeleccionarEmpleadoActivity extends AppCompatActivity implements EmpleadosAdapter.OnItemClickListener {

    private RecyclerView recyclerViewEmpleados;
    private List<String> listaCorreosEmpleados;
    private EmpleadosAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_empleado);

        recyclerViewEmpleados = findViewById(R.id.recyclerViewEmpleados);
        recyclerViewEmpleados.setLayoutManager(new LinearLayoutManager(this));

        listaCorreosEmpleados = new ArrayList<>();
        adapter = new EmpleadosAdapter(listaCorreosEmpleados);
        recyclerViewEmpleados.setAdapter(adapter);

        adapter.setOnItemClickListener(this); // Establecer el listener de clics en el adaptador

        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        obtenerCorreosUsuariosRegistrados();
    }

    private void obtenerCorreosUsuariosRegistrados() {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        usuariosRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreosEmpleados.clear();
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    String correo = usuarioSnapshot.child("email").getValue(String.class);
                    listaCorreosEmpleados.add(correo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SeleccionarEmpleadoActivity.this, "Error al obtener los correos electrónicos de los usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        String correoSeleccionado = listaCorreosEmpleados.get(position);
        // Aquí puedes abrir la actividad para subir la nómina y pasar el correo seleccionado como extra
        Intent intent = new Intent(this, SubirNominaActivity.class);
        intent.putExtra("correo", correoSeleccionado);
        startActivity(intent);
    }
}







