package com.juanjo.example.juanjoaguado_tfc;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthManager {
    private static final String TAG = "AuthManager";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public AuthManager() {
        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Obtener la referencia de la base de datos en tiempo real
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void registrarUsuario(final String email, String password, final RegistroCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            String uid = mAuth.getCurrentUser().getUid();
                            // Crear un objeto usuario
                            Usuario usuario = new Usuario(email);
                            // Guardar el objeto usuario en Realtime Database
                            mDatabase.child("usuarios").child(uid).setValue(usuario)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                callback.onSuccess();
                                            } else {
                                                Log.e(TAG, "Fallo al guardar el usuario en la base de datos", task.getException());
                                                callback.onError(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            // Error en el registro
                            Log.e(TAG, "Fallo en el registro", task.getException());
                            callback.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void iniciarSesion(String email, String password, final InicioSesionCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            callback.onSuccess();
                        } else {
                            // Error en el inicio de sesión
                            Log.e(TAG, "Fallo en el inicio de sesión", task.getException());
                            callback.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void enviarRestablecimientoContrasena(String email, final RestablecimientoContrasenaCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Correo de restablecimiento de contraseña enviado exitosamente
                            callback.onSuccess();
                        } else {
                            // Error al enviar el correo de restablecimiento de contraseña
                            Log.e(TAG, "Fallo al enviar el correo de restablecimiento de contraseña", task.getException());
                            callback.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public interface RegistroCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface InicioSesionCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface RestablecimientoContrasenaCallback {
        void onSuccess();
        void onError(String message);
    }

    // Clase Usuario para almacenar información del usuario en Firebase
    public static class Usuario {
        public String email;

        public Usuario() {
            // Constructor vacío necesario para la deserialización de Firebase
        }

        public Usuario(String email) {
            this.email = email;
        }
    }
}

