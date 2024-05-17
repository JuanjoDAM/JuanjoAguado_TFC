package com.juanjo.example.juanjoaguado_tfc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class EmpleadosAdapter extends RecyclerView.Adapter<EmpleadosAdapter.EmpleadoViewHolder> {

    private List<String> listaCorreosEmpleados;
    private OnItemClickListener mListener;

    public EmpleadosAdapter(List<String> listaCorreosEmpleados) {
        this.listaCorreosEmpleados = listaCorreosEmpleados;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new EmpleadoViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        String correo = listaCorreosEmpleados.get(position);
        holder.bind(correo);
    }

    @Override
    public int getItemCount() {
        return listaCorreosEmpleados.size();
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCorreo;

        public EmpleadoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewCorreo = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(String correo) {
            textViewCorreo.setText(correo);
        }
    }
}



