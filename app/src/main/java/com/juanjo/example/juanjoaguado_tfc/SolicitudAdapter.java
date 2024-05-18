package com.juanjo.example.juanjoaguado_tfc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Solicitud solicitud);
    }

    private List<Solicitud> solicitudList;
    private OnItemClickListener listener;

    public SolicitudAdapter(List<Solicitud> solicitudList, OnItemClickListener listener) {
        this.solicitudList = solicitudList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new SolicitudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {
        Solicitud solicitud = solicitudList.get(position);
        holder.bind(solicitud, listener);
    }

    @Override
    public int getItemCount() {
        return solicitudList.size();
    }

    public static class SolicitudViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSolicitud;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSolicitud = itemView.findViewById(R.id.textViewSolicitud);
        }

        public void bind(final Solicitud solicitud, final OnItemClickListener listener) {
            textViewSolicitud.setText(solicitud.getMotivo() + " (" + solicitud.getEstado() + ")");
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listener.onItemClick(solicitud);
                }
            });
        }
    }
}


