package com.juanjo.example.juanjoaguado_tfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroHorasDialog extends DialogFragment {

    private EditText editTextStartTime, editTextEndTime, editTextBreakHours;
    private Button buttonSave;

    private DatabaseReference databaseReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.registro_horas_dialog, null);

        builder.setView(view)
                .setTitle("Registro de Horas")
                .setNegativeButton("Cancelar", (dialog, which) -> dismiss());

        editTextStartTime = view.findViewById(R.id.editTextStartTime);
        editTextEndTime = view.findViewById(R.id.editTextEndTime);
        editTextBreakHours = view.findViewById(R.id.editTextBreakHours);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Inicializar Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("horas_trabajo");

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });

        return builder.create();
    }

    private void guardarDatos() {
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String breakHours = editTextBreakHours.getText().toString().trim();

        // Validar que se hayan ingresado los datos
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(breakHours)) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la fecha actual en el formato deseado (día-mes-año)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Obtener el nombre de usuario del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = currentUser != null ? currentUser.getEmail() : "";

        // Construir el ID único para la entrada en la base de datos
        String id = currentDate + "_" + username.replace(".", "_"); // Reemplazar "." por "_" en el nombre de usuario

        // Crear un objeto HorasTrabajo con los datos ingresados
        HorasTrabajo horasTrabajo = new HorasTrabajo(id, startTime, endTime, breakHours);

        // Guardar los datos en la base de datos
        databaseReference.child(id).setValue(horasTrabajo);

        Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

        dismiss(); // Cerrar el diálogo después de guardar los datos
    }


}
