package com.juanjo.example.juanjoaguado_tfc;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatosPersonalesDialog extends DialogFragment {

    private EditText editTextDNI, editTextNombre, editTextApellidos, editTextCorreo;
    private Button buttonGuardarDatos;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_datos_personales, container, false);

        editTextDNI = view.findViewById(R.id.editTextDNI);
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextApellidos = view.findViewById(R.id.editTextApellidos);
        editTextCorreo = view.findViewById(R.id.editTextCorreo);
        buttonGuardarDatos = view.findViewById(R.id.buttonGuardarDatos);

        databaseReference = FirebaseDatabase.getInstance().getReference("datos_personales");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        cargarDatosPersonales();

        buttonGuardarDatos.setOnClickListener(v -> guardarDatos());

        return view;
    }

    private void cargarDatosPersonales() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DatosPersonales datosPersonales = snapshot.getValue(DatosPersonales.class);
                    if (datosPersonales != null) {
                        editTextDNI.setText(datosPersonales.getDni());
                        editTextNombre.setText(datosPersonales.getNombre());
                        editTextApellidos.setText(datosPersonales.getApellidos());
                        editTextCorreo.setText(datosPersonales.getCorreo());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void guardarDatos() {
        String dni = editTextDNI.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();

        if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellidos) || TextUtils.isEmpty(correo)) {
            showToast("Por favor completa todos los campos");
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : "";

        if (email != null) {
            String emailKey = email.replace(".", "_");

            DatosPersonales datosPersonales = new DatosPersonales(dni, nombre, apellidos, correo);

            databaseReference.child(emailKey).setValue(datosPersonales)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Datos guardados correctamente");
                        dismiss();
                    })
                    .addOnFailureListener(e -> showToast("Error al guardar los datos"));
        } else {
            showToast("No se pudo obtener el correo electrónico del usuario");
        }
    }

    private void showToast(String message) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    }



