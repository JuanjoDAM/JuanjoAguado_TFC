package com.juanjo.example.juanjoaguado_tfc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private static final int SELECT_EMPLOYEE_REQUEST = 2;

    private Button buttonSeleccionarEmpleado;
    private FirebaseStorage storage;
    private String selectedEmployeeEmail;
    private Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        buttonSeleccionarEmpleado = findViewById(R.id.buttonSeleccionarEmpleado);
        storage = FirebaseStorage.getInstance();

        buttonSeleccionarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarEmpleado();
            }
        });
    }

    private void seleccionarEmpleado() {
        Intent intent = new Intent(this, SeleccionarEmpleadoActivity.class);
        startActivityForResult(intent, SELECT_EMPLOYEE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_EMPLOYEE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedEmployeeEmail = data.getStringExtra("employeeEmail");
            seleccionarArchivo();
        } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            subirArchivo();
        }
    }

    private void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Selecciona un PDF"), PICK_PDF_REQUEST);
    }

    private void subirArchivo() {
        if (pdfUri != null && selectedEmployeeEmail != null) {
            String email = selectedEmployeeEmail.replace(".", "_");
            StorageReference storageReference = storage.getReference().child("nominas/" + email + "/" + pdfUri.getLastPathSegment());
            storageReference.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminActivity.this, "Nómina subida exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminActivity.this, "Error al subir la nómina: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}



