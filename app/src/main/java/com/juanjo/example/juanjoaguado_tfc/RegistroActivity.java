package com.juanjo.example.juanjoaguado_tfc;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextCorreo;
    private EditText editTextContraseña;
    private Button buttonRegistrarse;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_layout);

        // Inicializar AuthManager
        authManager = new AuthManager();

        // Vincular vistas
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        buttonRegistrarse = findViewById(R.id.buttonRegistrarse); // Obtener referencia al botón de registro

        // Configurar onClickListener para el botón de registrarse
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();

        // Validar que se hayan ingresado el correo electrónico y la contraseña
        if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
            Toast.makeText(getApplicationContext(), "Por favor, ingresa un correo electrónico y una contraseña válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar usuario utilizando AuthManager
        authManager.registrarUsuario(correo, contraseña, new AuthManager.RegistroCallback() {
            @Override
            public void onError(String message) {
                // Error en el registro
                Toast.makeText(getApplicationContext(), "Error al registrar usuario: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                // Registro exitoso
                Toast.makeText(getApplicationContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                // Redirigir al usuario de vuelta al LoginActivity
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                // Finalizar la actividad actual (RegistroActivity)
                finish();
            }

        });

    }
}
