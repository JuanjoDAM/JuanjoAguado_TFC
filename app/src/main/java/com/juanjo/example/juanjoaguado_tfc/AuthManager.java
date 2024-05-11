package com.juanjo.example.juanjoaguado_tfc;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthManager {
    private static final String TAG = "AuthManager";
    private FirebaseAuth mAuth;

    public AuthManager() {
        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    public void registrarUsuario(String email, String password, final RegistroCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            callback.onSuccess();
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
}
