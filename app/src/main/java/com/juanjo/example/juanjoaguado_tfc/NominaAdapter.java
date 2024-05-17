package com.juanjo.example.juanjoaguado_tfc;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NominaAdapter extends RecyclerView.Adapter<NominaAdapter.NominaViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Nomina nomina);
    }

    private List<Nomina> nominaList;
    private OnItemClickListener listener;

    public NominaAdapter(List<Nomina> nominaList, OnItemClickListener listener) {
        this.nominaList = nominaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NominaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nomina, parent, false);
        return new NominaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NominaViewHolder holder, int position) {
        Nomina nomina = nominaList.get(position);
        holder.bind(nomina, position + 1, listener);
    }

    @Override
    public int getItemCount() {
        return nominaList.size();
    }

    public static class NominaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomina;

        public NominaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomina = itemView.findViewById(R.id.textViewNomina);
        }

        public void bind(final Nomina nomina, int position, final OnItemClickListener listener) {
            textViewNomina.setText("Nomina " + position); // Mostrar nombres predefinidos
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listener.onItemClick(nomina);
                }
            });
        }
    }
}


