package com.juanjo.example.juanjoaguado_tfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DatosPersonalesDialog extends DialogFragment {

    private EditText editTextDNI, editTextNombre, editTextApellidos, editTextCorreo;
    private Button buttonEliminar, buttonCancelar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private boolean isAdmin = false;
    private String email;

    public DatosPersonalesDialog(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_datos_personales, null);

        editTextDNI = view.findViewById(R.id.editTextDNI);
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextApellidos = view.findViewById(R.id.editTextApellidos);
        editTextCorreo = view.findViewById(R.id.editTextCorreo);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        Button buttonGuardar = view.findViewById(R.id.buttonGuardar);

        databaseReference = FirebaseDatabase.getInstance().getReference("datos_personales");
        mAuth = FirebaseAuth.getInstance();

        if (isAdmin) {
            email = getArguments().getString("email");
            buttonEliminar.setVisibility(View.VISIBLE);
            buttonGuardar.setVisibility(View.GONE);
            cargarDatosPersonales();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                email = currentUser.getEmail().replace(".", "_");
                buttonEliminar.setVisibility(View.GONE);
                buttonGuardar.setVisibility(View.VISIBLE);
                cargarDatosPersonales();
            }
        }

        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatosPersonalesDialog.this.getDialog().cancel();
                    }
                });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarUsuario();
            }
        });

        return builder.create();
    }

    private void cargarDatosPersonales() {
        // Reemplaza los caracteres prohibidos en el email antes de usarlo como clave en Firebase
        String emailKey = email.replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_");

        databaseReference.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatosPersonales datosPersonales = snapshot.getValue(DatosPersonales.class);
                if (datosPersonales != null) {
                    editTextDNI.setText(datosPersonales.getDni());
                    editTextNombre.setText(datosPersonales.getNombre());
                    editTextApellidos.setText(datosPersonales.getApellidos());
                    editTextCorreo.setText(datosPersonales.getCorreo());
                    if (isAdmin) {
                        editTextDNI.setEnabled(false);
                        editTextNombre.setEnabled(false);
                        editTextApellidos.setEnabled(false);
                        editTextCorreo.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatos() {
        String dni = editTextDNI.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();

        if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatosPersonales datosPersonales = new DatosPersonales(dni, nombre, apellidos, correo);
        // Reemplaza los caracteres prohibidos en el email antes de usarlo como clave en Firebase
        String emailKey = email.replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_");

        databaseReference.child(emailKey).setValue(datosPersonales)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                });
    }

    private void eliminarUsuario() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación")
                .setMessage("¿Quieres eliminar " + editTextNombre.getText().toString() + " de forma permanente?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Reemplaza los caracteres prohibidos en el email antes de usarlo como clave en Firebase
                    String emailKey = email.replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_");

                    // Mover el usuario a la tabla "denegados" en Realtime Database
                    DatabaseReference denegadosRef = FirebaseDatabase.getInstance().getReference("denegados");
                    denegadosRef.child(emailKey).setValue(true)
                            .addOnSuccessListener(aVoid -> {
                                // Eliminar de la tabla "datos_personales"
                                databaseReference.child(emailKey).removeValue()
                                        .addOnSuccessListener(aVoid1 -> {
                                            // Eliminar de la tabla "usuarios"
                                            DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
                                            usuariosRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                        userSnapshot.getRef().removeValue()
                                                                .addOnSuccessListener(aVoid2 -> {
                                                                    Toast.makeText(getContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                                                                    dismiss();
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(getContext(), "Error al eliminar el usuario de la tabla 'usuarios'", Toast.LENGTH_SHORT).show();
                                                                });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getContext(), "Error al buscar el usuario en la tabla 'usuarios'", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getContext(), "Error al eliminar el usuario de Realtime Database", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error al mover el usuario a la tabla 'denegados'", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }


}





