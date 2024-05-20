package com.juanjo.example.juanjoaguado_tfc;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroHorasDialog extends DialogFragment {

    private TextView textViewDate;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private EditText editTextBreakHours;
    private Button buttonSave;
    private Button buttonWorkedYes;
    private Button buttonWorkedNo;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_registro_horas, container, false);

        textViewDate = view.findViewById(R.id.textViewDate);
        editTextStartTime = view.findViewById(R.id.editTextStartTime);
        editTextEndTime = view.findViewById(R.id.editTextEndTime);
        editTextBreakHours = view.findViewById(R.id.editTextBreakHours);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonWorkedYes = view.findViewById(R.id.buttonWorkedYes);
        buttonWorkedNo = view.findViewById(R.id.buttonWorkedNo);

        databaseReference = FirebaseDatabase.getInstance().getReference("horas_trabajo");

        // Set current date
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        textViewDate.setText(currentDate);

        buttonSave.setOnClickListener(v -> guardarHoras());

        buttonWorkedYes.setOnClickListener(v -> {
            buttonWorkedYes.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.colorSelected));
            buttonWorkedNo.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.colorUnselected));
        });

        buttonWorkedNo.setOnClickListener(v -> {
            buttonWorkedYes.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.colorUnselected));
            buttonWorkedNo.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.colorSelected));
        });

        return view;
    }

    private void guardarHoras() {
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String breakHoursStr = editTextBreakHours.getText().toString().trim();

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(breakHoursStr)) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String currentDate = textViewDate.getText().toString();

        if (username != null) {
            String id = currentDate + "_" + username.replace(".", "_");
            DatabaseReference entryRef = databaseReference.child(id);
            entryRef.child("startTime").setValue(startTime);
            entryRef.child("endTime").setValue(endTime);
            entryRef.child("breakHours").setValue(breakHoursStr);
            entryRef.child("id").setValue(id);
        }

        Toast.makeText(getActivity(), "Horas registradas", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}


