package com.juanjo.example.juanjoaguado_tfc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminSolicitudAdapter extends RecyclerView.Adapter<AdminSolicitudAdapter.SolicitudViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Solicitud solicitud);
    }

    private List<Solicitud> solicitudList;
    private OnItemClickListener listener;

    public AdminSolicitudAdapter(List<Solicitud> solicitudList, OnItemClickListener listener) {
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
        private TextView textViewEmail, textViewEstado;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
        }

        public void bind(final Solicitud solicitud, final OnItemClickListener listener) {
            textViewEmail.setText(solicitud.getUserId());
            textViewEstado.setText(solicitud.getEstado());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(solicitud);
                }
            });
        }
    }
}



