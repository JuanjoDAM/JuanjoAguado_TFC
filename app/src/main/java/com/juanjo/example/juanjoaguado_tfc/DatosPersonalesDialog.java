package com.juanjo.example.juanjoaguado_tfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
    private Button buttonEliminar, buttonCancelar;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private boolean isAdmin;

    public DatosPersonalesDialog(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");

        // Inflar el layout
        builder.setView(R.layout.dialog_datos_personales);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            editTextDNI = dialog.findViewById(R.id.editTextDNI);
            editTextNombre = dialog.findViewById(R.id.editTextNombre);
            editTextApellidos = dialog.findViewById(R.id.editTextApellidos);
            editTextCorreo = dialog.findViewById(R.id.editTextCorreo);
            buttonEliminar = dialog.findViewById(R.id.buttonEliminar);
            buttonCancelar = dialog.findViewById(R.id.buttonCancelar);

            databaseReference = FirebaseDatabase.getInstance().getReference("datos_personales");
            currentUser = FirebaseAuth.getInstance().getCurrentUser();

            buttonCancelar.setOnClickListener(v -> dismiss());

            if (isAdmin) {
                buttonEliminar.setVisibility(View.VISIBLE);
                buttonEliminar.setOnClickListener(v -> confirmarEliminarUsuario());
            }

            cargarDatosPersonales();
        }
    }

    private void cargarDatosPersonales() {
        String email = getArguments().getString("email").replace(".", "_");
        if (email != null) {
            databaseReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Toast.makeText(getActivity(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void confirmarEliminarUsuario() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Quieres eliminar a " + editTextNombre.getText().toString() + " de forma permanente?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarUsuario())
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminarUsuario() {
        String email = getArguments().getString("email").replace(".", "_");
        if (email != null) {
            databaseReference.child(email).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                        dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al eliminar el usuario", Toast.LENGTH_SHORT).show());
        }
    }
}
