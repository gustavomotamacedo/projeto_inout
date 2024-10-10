package com.gustavomacedo.inout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private Context context;
    private ArrayList eventosId, eventosNome, eventosQtdAlunos;

    public EventoAdapter(Context context, ArrayList eventosId, ArrayList eventosNome, ArrayList eventosQtdAlunos) {
        this.context = context;
        this.eventosId = eventosId;
        this.eventosNome = eventosNome;
        this.eventosQtdAlunos = eventosQtdAlunos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.evento_row, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        holder.eventoId.setText(eventosId.get(position).toString());
        holder.eventoNome.setText(eventosNome.get(position).toString());
        holder.eventoQtdAlunos.setText(eventosQtdAlunos.get(position).toString());
        holder.mainLayout.setOnClickListener(v -> {
            Intent in = new Intent(context.getApplicationContext(), AlunosActivity.class);
            in.putExtra("_id_evento", holder.eventoId.getText().toString());
            context.startActivity(in);
        });
    }

    @Override
    public int getItemCount() {
        return eventosId.size();
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mainLayout;
        TextView eventoId, eventoNome, eventoQtdAlunos;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            eventoId = itemView.findViewById(R.id.eventoId);
            eventoNome = itemView.findViewById(R.id.eventoNome);
            eventoQtdAlunos = itemView.findViewById(R.id.eventoQtdAlunos);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
