package com.juanjo.example.juanjoaguado_tfc;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder> {

    private List<Solicitud> solicitudesList;

    public SolicitudesAdapter(List<Solicitud> solicitudesList) {
        this.solicitudesList = solicitudesList;
    }

    @NonNull
    @Override
    public SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new SolicitudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {
        Solicitud solicitud = solicitudesList.get(position);
        holder.bind(solicitud);
    }

    @Override
    public int getItemCount() {
        return solicitudesList.size();
    }

    public static class SolicitudViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewSolicitud;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSolicitud = itemView.findViewById(R.id.textViewSolicitud);
        }

        public void bind(Solicitud solicitud) {
            textViewSolicitud.setText("Motivo: " + solicitud.getMotivo() + "\nEstado: " + solicitud.getEstado());
        }
    }
}

