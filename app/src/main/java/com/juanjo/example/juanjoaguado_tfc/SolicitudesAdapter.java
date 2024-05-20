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

    public class SolicitudViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewEmail;
        public TextView textViewMotivo;
        public TextView textViewEstado;

        public SolicitudViewHolder(View itemView) {
            super(itemView);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewMotivo = itemView.findViewById(R.id.textViewMotivo);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
        }

        public void bind(Solicitud solicitud) {
            textViewEmail.setText(solicitud.getUserId());

            if (!solicitud.getMotivo().isEmpty()) {
                textViewMotivo.setText("Motivo: " + solicitud.getMotivo());
                textViewMotivo.setVisibility(View.VISIBLE);
            } else {
                textViewMotivo.setVisibility(View.GONE);
            }

            if (!solicitud.getEstado().isEmpty()) {
                textViewEstado.setText("Estado: " + solicitud.getEstado());
                textViewEstado.setVisibility(View.VISIBLE);
            } else {
                textViewEstado.setVisibility(View.GONE);
            }
        }
    }

}

