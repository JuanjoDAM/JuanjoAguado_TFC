package com.juanjo.example.juanjoaguado_tfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegistrarHoras;
    private Button buttonBajasVacaciones;
    private Button buttonNominas;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegistrarHoras = findViewById(R.id.buttonRegistrarHoras);
        buttonBajasVacaciones = findViewById(R.id.buttonBajasVacaciones);
        buttonNominas = findViewById(R.id.buttonNominas);
        barChart = findViewById(R.id.barChart);

        buttonRegistrarHoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistroHoras();
            }
        });

        buttonBajasVacaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadBajasVacaciones();
            }
        });

        buttonNominas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadNominas();
            }
        });

        cargarDatosHorasTrabajadas();
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

    private void cargarDatosHorasTrabajadas() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail().replace(".", "_");
            String currentMonth = new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());

            FirebaseDatabase.getInstance().getReference("horas_trabajo")
                    .orderByKey()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Integer> horasPorDia = new HashMap<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                if (key != null && key.contains(email) && key.contains(currentMonth)) {
                                    String startTime = snapshot.child("startTime").getValue(String.class);
                                    String endTime = snapshot.child("endTime").getValue(String.class);
                                    String breakHours = snapshot.child("breakHours").getValue(String.class);
                                    int horasTrabajadas = calcularHorasTrabajadas(startTime, endTime, breakHours);

                                    String dia = key.substring(0, 2);
                                    horasPorDia.put(dia, horasPorDia.getOrDefault(dia, 0) + horasTrabajadas);
                                }
                            }

                            List<BarEntry> entries = new ArrayList<>();
                            for (Map.Entry<String, Integer> entry : horasPorDia.entrySet()) {
                                entries.add(new BarEntry(Float.parseFloat(entry.getKey()), entry.getValue()));
                            }

                            BarDataSet dataSet = new BarDataSet(entries, "Horas trabajadas");
                            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);
                            barChart.invalidate();

                            Description description = new Description();
                            description.setText("Horas trabajadas por día");
                            barChart.setDescription(description);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private int calcularHorasTrabajadas(String startTime, String endTime, String breakHours) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            long startMillis = format.parse(startTime).getTime();
            long endMillis = format.parse(endTime).getTime();
            long breakMillis = (long) (Double.parseDouble(breakHours) * 3600000);

            if (endMillis < startMillis) {
                // Handle cases where endTime is past midnight
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endMillis);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                endMillis = calendar.getTimeInMillis();
            }

            return (int) ((endMillis - startMillis - breakMillis) / 3600000);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}




