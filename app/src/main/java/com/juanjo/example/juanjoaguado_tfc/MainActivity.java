package com.juanjo.example.juanjoaguado_tfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegistrarHoras, buttonBajasVacaciones, buttonNominas, buttonDatosPersonales;
    private BarChart barChart;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegistrarHoras = findViewById(R.id.buttonRegistrarHoras);
        buttonBajasVacaciones = findViewById(R.id.buttonBajasVacaciones);
        buttonNominas = findViewById(R.id.buttonNominas);
        buttonDatosPersonales = findViewById(R.id.buttonDatosPersonales);
        barChart = findViewById(R.id.barChart);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("horas_trabajo");

        buttonRegistrarHoras.setOnClickListener(v -> mostrarDialogoRegistroHoras());
        buttonBajasVacaciones.setOnClickListener(v -> abrirActividadBajasVacaciones());
        buttonNominas.setOnClickListener(v -> abrirActividadNominas());
        buttonDatosPersonales.setOnClickListener(v -> mostrarDialogoDatosPersonales());

        cargarHorasTrabajadas();
    }

    private void mostrarDialogoRegistroHoras() {
        DialogFragment dialogFragment = new RegistroHorasDialog();
        dialogFragment.show(getSupportFragmentManager(), "RegistroHorasDialog");
    }

    private void abrirActividadBajasVacaciones() {
        Intent intent = new Intent(MainActivity.this, BajasVacacionesActivity.class);
        startActivity(intent);
    }

    private void abrirActividadNominas() {
        Intent intent = new Intent(MainActivity.this, NominasActivity.class);
        startActivity(intent);
    }

    private void mostrarDialogoDatosPersonales() {
        DialogFragment dialogFragment = new DatosPersonalesDialog();
        dialogFragment.show(getSupportFragmentManager(), "DatosPersonalesDialog");
    }

    private void cargarHorasTrabajadas() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail().replace(".", "_");
            databaseReference.orderByKey().startAt("01-05-2024_" + userEmail).endAt("31-05-2024_" + userEmail)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Float> horasTrabajadasPorDia = new HashMap<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                HorasTrabajo horasTrabajo = dataSnapshot.getValue(HorasTrabajo.class);
                                if (horasTrabajo != null) {
                                    String fecha = horasTrabajo.getId().split("_")[0];
                                    float horasTrabajadas = calcularHorasTrabajadas(horasTrabajo.getStartTime(), horasTrabajo.getEndTime(), horasTrabajo.getBreakHours());
                                    if (horasTrabajadasPorDia.containsKey(fecha)) {
                                        horasTrabajadasPorDia.put(fecha, horasTrabajadasPorDia.get(fecha) + horasTrabajadas);
                                    } else {
                                        horasTrabajadasPorDia.put(fecha, horasTrabajadas);
                                    }
                                }
                            }
                            mostrarGrafico(horasTrabajadasPorDia);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
        }
    }

    private float calcularHorasTrabajadas(String startTime, String endTime, String breakHours) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            long startMillis = sdf.parse(startTime).getTime();
            long endMillis = sdf.parse(endTime).getTime();
            float breakTime = Float.parseFloat(breakHours);
            if (endMillis < startMillis) {
                endMillis += 24 * 60 * 60 * 1000;
            }
            return ((endMillis - startMillis) / (1000 * 60 * 60)) - breakTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void mostrarGrafico(Map<String, Float> horasTrabajadasPorDia) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Float> entry : horasTrabajadasPorDia.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey().substring(0, 5)); // Formato día/mes
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Horas trabajadas");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setTextSize(12f);
        barChart.getXAxis().setLabelCount(labels.size());
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMaximum(10);
        barChart.getAxisLeft().setLabelCount(11, true);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate(); // Refresh chart
    }
}

