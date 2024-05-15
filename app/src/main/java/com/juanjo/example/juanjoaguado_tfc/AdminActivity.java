package com.juanjo.example.juanjoaguado_tfc;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    private TextView textViewAdminStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textViewAdminStatus = findViewById(R.id.textViewAdminStatus);

        // Verificar el estado de autenticación del usuario actual
        verificarEstadoAutenticacion();
    }

    private void verificarEstadoAutenticacion() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && Objects.equals(user.getEmail(), "admin@admin.com")) {
            // Si el usuario actual es el administrador, mostrar un mensaje de éxito
            textViewAdminStatus.setText("¡Bienvenido, administrador!");
        }
    }
}

