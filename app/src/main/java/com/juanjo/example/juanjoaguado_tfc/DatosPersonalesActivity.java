package com.juanjo.example.juanjoaguado_tfc;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatosPersonalesActivity extends AppCompatActivity {

    private EditText editTextDNI, editTextNombre, editTextApellidos, editTextEmail;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        editTextDNI = findViewById(R.id.editTextDNI);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextEmail = findViewById(R.id.editTextEmail);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("datos_personales");

        String email = getIntent().getStringExtra("email");

        if (email != null) {
            cargarDatosPersonales(email);
        } else {
            Toast.makeText(this, "No se recibió el correo electrónico", Toast.LENGTH_SHORT).show();
        }

        if (getIntent().getBooleanExtra("isAdmin", false)) {
            deshabilitarCampos();
        }
    }

    private void cargarDatosPersonales(String email) {
        String emailKey = email.replace(".", "_");

        databaseReference.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatosPersonales datos = dataSnapshot.getValue(DatosPersonales.class);
                if (datos != null) {
                    editTextDNI.setText(datos.getDni());
                    editTextNombre.setText(datos.getNombre());
                    editTextApellidos.setText(datos.getApellidos());
                    editTextEmail.setText(datos.getCorreo());
                } else {
                    Toast.makeText(DatosPersonalesActivity.this, "No se encontraron datos personales", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DatosPersonalesActivity.this, "Error al cargar los datos personales", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deshabilitarCampos() {
        editTextDNI.setEnabled(false);
        editTextNombre.setEnabled(false);
        editTextApellidos.setEnabled(false);
        editTextEmail.setEnabled(false);
    }
}





