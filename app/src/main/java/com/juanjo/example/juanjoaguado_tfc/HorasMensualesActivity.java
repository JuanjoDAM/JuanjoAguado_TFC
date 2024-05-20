package com.juanjo.example.juanjoaguado_tfc;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HorasMensualesActivity extends AppCompatActivity {

    private BarChart barChart;
    private DatabaseReference databaseReference;
    private Map<String, Float> monthlyHoursMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horas_mensuales);

        barChart = findViewById(R.id.barChart);
        databaseReference = FirebaseDatabase.getInstance().getReference("horas_trabajo");

        cargarDatosHorasMensuales();
    }

    private void cargarDatosHorasMensuales() {
        String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (username != null) {
            databaseReference.orderByKey().startAt(username).endAt(username + "\uf8ff")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            monthlyHoursMap = new HashMap<>();
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String startTimeStr = dataSnapshot.child("startTime").getValue(String.class);
                                String endTimeStr = dataSnapshot.child("endTime").getValue(String.class);
                                String breakHoursStr = dataSnapshot.child("breakHours").getValue(String.class);

                                if (startTimeStr != null && endTimeStr != null && breakHoursStr != null) {
                                    try {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                        long startMillis = timeFormat.parse(startTimeStr).getTime();
                                        long endMillis = timeFormat.parse(endTimeStr).getTime();

                                        if (endMillis < startMillis) {
                                            endMillis += 24 * 60 * 60 * 1000; // Handle case where endTime is past midnight
                                        }

                                        float hoursWorked = (endMillis - startMillis) / (1000f * 60 * 60);
                                        float breakHours = Float.parseFloat(breakHoursStr);

                                        hoursWorked -= breakHours;

                                        String dateKey = dataSnapshot.getKey();
                                        String monthYear = monthFormat.format(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(dateKey.split("_")[0]));

                                        if (monthlyHoursMap.containsKey(monthYear)) {
                                            monthlyHoursMap.put(monthYear, monthlyHoursMap.get(monthYear) + hoursWorked);
                                        } else {
                                            monthlyHoursMap.put(monthYear, hoursWorked);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            mostrarGrafico();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(HorasMensualesActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void mostrarGrafico() {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Float> entry : monthlyHoursMap.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Horas Trabajadas Mensualmente");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.invalidate(); // Refresh chart
    }
}

