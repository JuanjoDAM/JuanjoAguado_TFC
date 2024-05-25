package com.juanjo.example.juanjoaguado_tfc;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatosPersonalesActivity extends AppCompatActivity {

    private EditText editTextDNI, editTextNombre, editTextApellidos, editTextEmail;
    private Button buttonGuardar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        editTextDNI = findViewById(R.id.editTextDNI);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("datos_personales");

        buttonGuardar.setOnClickListener(v -> guardarDatosPersonales());
    }

    private void guardarDatosPersonales() {
        String dni = editTextDNI.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellidos) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Error de autenticación. Por favor, inicia sesión de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DatosPersonales datosPersonales = new DatosPersonales(dni, nombre, apellidos, email);

        databaseReference.child(userId).setValue(datosPersonales)
                .addOnSuccessListener(aVoid -> Toast.makeText(DatosPersonalesActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(DatosPersonalesActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show());
    }
}

