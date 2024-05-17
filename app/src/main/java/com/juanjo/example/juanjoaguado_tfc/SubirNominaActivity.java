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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SubirNominaActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private Button buttonSelectPDF, buttonUploadPDF;
    private Uri pdfUri;
    private String userEmail;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_nomina);

        buttonSelectPDF = findViewById(R.id.buttonSelectPDF);
        buttonUploadPDF = findViewById(R.id.buttonUploadPDF);

        storageReference = FirebaseStorage.getInstance().getReference("nominas");
        databaseReference = FirebaseDatabase.getInstance().getReference("nominas");

        userEmail = getIntent().getStringExtra("correo");

        buttonSelectPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        buttonUploadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPDF();
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
        }
    }

    private void uploadPDF() {
        if (pdfUri != null) {
            // Referencia del archivo con un nombre único
            final String fileName = System.currentTimeMillis() + ".pdf";
            final StorageReference fileReference = storageReference.child(userEmail).child(fileName);

            fileReference.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();

                                    // Guardar la URL en la base de datos bajo el nodo del usuario
                                    Map<String, Object> nominaData = new HashMap<>();
                                    nominaData.put("url", url);
                                    databaseReference.child(userEmail.replace(".", ",")).child(fileName).setValue(nominaData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(SubirNominaActivity.this, "Nómina subida exitosamente", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SubirNominaActivity.this, "Error al subir la nómina", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SubirNominaActivity.this, "Error al subir la nómina", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Por favor selecciona un archivo PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
