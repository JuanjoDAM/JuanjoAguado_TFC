package com.juanjo.example.juanjoaguado_tfc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SubirNominaActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private Button buttonSelectPDF, buttonUploadPDF;
    private ImageView imageViewPDF;
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
        imageViewPDF = findViewById(R.id.imageViewPDF);

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
            displayPDF(pdfUri);
        }
    }

    private void displayPDF(Uri uri) {
        try {
            File file = new File(getCacheDir(), "temp.pdf");
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }

            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            PdfRenderer.Page page = pdfRenderer.openPage(0);

            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            imageViewPDF.setImageBitmap(bitmap);
            imageViewPDF.setVisibility(View.VISIBLE);

            page.close();
            pdfRenderer.close();
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al mostrar el PDF", Toast.LENGTH_SHORT).show();
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
