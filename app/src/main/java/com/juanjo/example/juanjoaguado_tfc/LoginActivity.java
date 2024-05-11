package com.juanjo.example.juanjoaguado_tfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegistrarse;
    private Button buttonIniciarSesion;
    private TextView textViewForgotPassword;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Inicializar AuthManager
        authManager = new AuthManager();

        // Vincular vistas
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonRegistrarse = findViewById(R.id.buttonRegistrarse); // Obtener referencia al botón de registro

        // Configurar onClickListener para el botón de iniciar sesión
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        // Configurar onClickListener para el texto de restablecer contraseña
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el correo electrónico ingresado por el usuario
                String email = editTextEmail.getText().toString().trim();
                // Verificar si se ingresó un correo electrónico
                if (!TextUtils.isEmpty(email)) {
                    // Enviar correo electrónico de restablecimiento de contraseña
                    authManager.enviarRestablecimientoContrasena(email, new AuthManager.RestablecimientoContrasenaCallback() {
                        @Override
                        public void onSuccess() {
                            // Correo de restablecimiento de contraseña enviado exitosamente
                            Toast.makeText(getApplicationContext(), "Se ha enviado un correo electrónico de restablecimiento de contraseña", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            // Error al enviar el correo de restablecimiento de contraseña
                            Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Mostrar un mensaje de error si no se ingresó un correo electrónico
                    Toast.makeText(getApplicationContext(), "Por favor ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Configurar onClickListener para el botón de registrarse
        // Configurar onClickListener para el botón de registrarse
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de registro
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registrarUsuario() {
        // Aquí puedes agregar la lógica para manejar el registro de usuario
        // Por ejemplo, abrir una nueva actividad para el registro de usuario
    }

    private void iniciarSesion() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validar que se hayan ingresado el correo electrónico y la contraseña
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iniciar sesión utilizando AuthManager
        authManager.iniciarSesion(email, password, new AuthManager.InicioSesionCallback() {
            @Override
            public void onSuccess() {
                // Inicio de sesión exitoso
                // Puedes redirigir al usuario a la siguiente actividad o realizar otras acciones
                Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                // Error en el inicio de sesión
                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}