package com.juanjo.example.juanjoaguado_tfc;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_registro_horas, container, false);

        editTextStartTime = view.findViewById(R.id.editTextStartTime);
        editTextEndTime = view.findViewById(R.id.editTextEndTime);
        editTextBreakHours = view.findViewById(R.id.editTextBreakHours);
        buttonSave = view.findViewById(R.id.buttonSave);

        databaseReference = FirebaseDatabase.getInstance().getReference("horas_trabajo");

        buttonSave.setOnClickListener(v -> guardarDatos());

        return view;
    }

    private void guardarDatos() {
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String breakHours = editTextBreakHours.getText().toString().trim();

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(breakHours)) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = currentUser != null ? currentUser.getEmail() : "";

        String id = currentDate + "_" + username.replace(".", "_");

        HorasTrabajo horasTrabajo = new HorasTrabajo(id, startTime, endTime, breakHours);

        databaseReference.child(id).setValue(horasTrabajo);

        Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

        dismiss();
    }
}


