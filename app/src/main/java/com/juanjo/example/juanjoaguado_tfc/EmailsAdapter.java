package com.juanjo.example.juanjoaguado_tfc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String email);
    }

    private List<String> emails;
    private OnItemClickListener listener;

    public EmailsAdapter(List<String> emails, OnItemClickListener listener) {
        this.emails = emails;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        String email = emails.get(position);
        holder.bind(email, listener);
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(final String email, final OnItemClickListener listener) {
            textView.setText(email);
            itemView.setOnClickListener(v -> listener.onItemClick(email));
        }
    }
}

