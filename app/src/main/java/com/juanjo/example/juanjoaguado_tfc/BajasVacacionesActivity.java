package com.juanjo.example.juanjoaguado_tfc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BajasVacacionesActivity extends AppCompatActivity {

    private EditText editTextMotivo;
    private RadioGroup radioGroupTipo;
    private DatePicker datePickerInicio, datePickerFin;
    private Button buttonEnviar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas_vacaciones);

        editTextMotivo = findViewById(R.id.editTextMotivo);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);
        datePickerInicio = findViewById(R.id.datePickerInicio);
        datePickerFin = findViewById(R.id.datePickerFin);
        buttonEnviar = findViewById(R.id.buttonEnviar);

        databaseReference = FirebaseDatabase.getInstance().getReference("solicitudes_bajas_vacaciones");

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitud();
            }
        });
    }

    private void enviarSolicitud() {
        String motivo = editTextMotivo.getText().toString().trim();
        String tipo = obtenerTipoSeleccionado();
        String inicio = obtenerFecha(datePickerInicio);
        String fin = obtenerFecha(datePickerFin);

        if (motivo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un motivo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el nombre de usuario del usuario actual
        String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (username == null) {
            Toast.makeText(this, "No se pudo obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construir el ID único para la entrada en la base de datos
        String solicitudId = username.replace(".", "_") + "_" + inicio + "_" + fin;

        if (solicitudId != null) {
            Solicitud solicitud = new Solicitud(solicitudId, username, tipo, inicio, fin, motivo, "pendiente", "");
            databaseReference.child(solicitudId).setValue(solicitud);
            Toast.makeText(this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }





    private String obtenerTipoSeleccionado() {
        int tipoId = radioGroupTipo.getCheckedRadioButtonId();
        if (tipoId == R.id.radioButtonBaja) {
            return "Baja";
        } else if (tipoId == R.id.radioButtonVacaciones) {
            return "Vacaciones";
        } else {
            return "";
        }
    }

    private String obtenerFecha(DatePicker datePicker) {
        int dia = datePicker.getDayOfMonth();
        int mes = datePicker.getMonth() + 1; // Se suma 1 porque enero es 0
        int anio = datePicker.getYear();
        return dia + "-" + mes + "-" + anio;
    }
}

