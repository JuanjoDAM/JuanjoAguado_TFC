package com.juanjo.example.juanjoaguado_tfc;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NominasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNominas;
    private NominaAdapter nominaAdapter;
    private List<Nomina> nominaList;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominas);

        recyclerViewNominas = findViewById(R.id.recyclerViewNominas);
        recyclerViewNominas.setLayoutManager(new LinearLayoutManager(this));
        nominaList = new ArrayList<>();
        nominaAdapter = new NominaAdapter(nominaList, new NominaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Nomina nomina) {
                // Descargar el archivo PDF al hacer clic en la nómina
                descargarNomina(nomina);
            }
        });
        recyclerViewNominas.setAdapter(nominaAdapter);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        cargarNominasUsuarioActual();
    }

    private void cargarNominasUsuarioActual() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference userNominasRef = storageReference.child("nominas").child(currentUser.getEmail());

            userNominasRef.listAll().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    nominaList.clear();
                    int[] contador = {1}; // Inicializar el contador como un arreglo de un solo elemento
                    for (StorageReference item : task.getResult().getItems()) {
                        // Obtener la URL de descarga de cada nómina
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            String nombreArchivo = obtenerNombreArchivo(contador[0]); // Obtener el nombre de archivo secuencial
                            String url = uri.toString();
                            Nomina nomina = new Nomina(userId, nombreArchivo, url); // Crear la instancia de Nomina
                            nominaList.add(nomina);
                            nominaAdapter.notifyDataSetChanged();
                            contador[0]++; // Incrementar el contador para la próxima iteración
                        }).addOnFailureListener(e -> {
                            // Manejar errores al obtener la URL de descarga
                            Toast.makeText(NominasActivity.this, "Error al obtener la URL de descarga", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    // Manejar errores al listar las nóminas
                    Toast.makeText(NominasActivity.this, "Error al listar las nóminas", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // El usuario no está autenticado, redirigir al inicio de sesión
            startActivity(new Intent(NominasActivity.this, LoginActivity.class));
            finish();
        }
    }

    private String obtenerNombreArchivo(int contador) {
        return "Nomina " + contador;
    }

    private void descargarNomina(Nomina nomina) {
        String url = nomina.getUrl();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Descargando nomina");
        request.setDescription("Descargando archivo PDF");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "nomina.pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        } else {
            Toast.makeText(this, "Error al iniciar la descarga", Toast.LENGTH_SHORT).show();
        }
    }

}
