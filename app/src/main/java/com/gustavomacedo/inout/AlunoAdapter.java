package com.gustavomacedo.inout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder> {

    private Context context;
    private ArrayList alunosNome, alunosRGM, alunosData, alunosHoraEntrada, alunosHoraSaida;

    public AlunoAdapter(Context context, ArrayList alunosNome, ArrayList alunosRGM, ArrayList alunosData, ArrayList alunosHoraEntrada, ArrayList alunosHoraSaida) {
        this.context = context;
        this.alunosNome = alunosNome;
        this.alunosRGM = alunosRGM;
        this.alunosData = alunosData;
        this.alunosHoraEntrada = alunosHoraEntrada;
        this.alunosHoraSaida = alunosHoraSaida;
    }

    @NonNull
    @Override
    public AlunoAdapter.AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.aluno_row, parent, false);
        return new AlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoAdapter.AlunoViewHolder holder, int position) {
        holder.alunoNome.setText(alunosNome.get(position).toString());
        holder.alunoRGM.setText(alunosRGM.get(position).toString());
        holder.alunoData.setText(alunosData.get(position).toString());
        holder.alunoHoraEntrada.setText(alunosHoraEntrada.get(position).toString());
        holder.alunoHoraSaida.setText(alunosHoraSaida.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return alunosRGM.size();
    }

    public class AlunoViewHolder extends RecyclerView.ViewHolder {

        TextView alunoNome, alunoRGM, alunoData, alunoHoraEntrada, alunoHoraSaida;

        public AlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            alunoNome = itemView.findViewById(R.id.alunoNome);
            alunoRGM = itemView.findViewById(R.id.alunoRgm);
            alunoData = itemView.findViewById(R.id.alunoData);
            alunoHoraEntrada = itemView.findViewById(R.id.alunoHoraEntrada);
            alunoHoraSaida = itemView.findViewById(R.id.alunoHoraSaida);
        }
    }
}
