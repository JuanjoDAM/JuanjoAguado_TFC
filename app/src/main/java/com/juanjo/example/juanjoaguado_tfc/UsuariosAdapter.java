package com.juanjo.example.juanjoaguado_tfc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private List<String> usuariosList;
    private OnItemClickListener listener;

    public UsuariosAdapter(List<String> usuariosList) {
        this.usuariosList = usuariosList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        String email = usuariosList.get(position);
        holder.textViewEmail.setText(email);
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEmail;

        public UsuarioViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
