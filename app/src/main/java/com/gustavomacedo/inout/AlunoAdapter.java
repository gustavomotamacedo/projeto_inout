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
    private ArrayList alunosNome, alunosRGM, alunosData, alunosHoraEntrada, alunosHoraSaida, alunosEvento;

    public AlunoAdapter(Context context, ArrayList alunosNome, ArrayList alunosRGM, ArrayList alunosEvento, ArrayList alunosData, ArrayList alunosHoraEntrada, ArrayList alunosHoraSaida) {
        this.context = context;
        this.alunosNome = alunosNome;
        this.alunosRGM = alunosRGM;
        this.alunosData = alunosData;
        this.alunosHoraEntrada = alunosHoraEntrada;
        this.alunosHoraSaida = alunosHoraSaida;
        this.alunosEvento = alunosEvento;
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
        holder.alunoData.setText(alunosData.get(position) == null ? "" : alunosData.get(position).toString());
        holder.alunoEvento.setText(alunosEvento.get(position) == null ? "" : alunosEvento.get(position).toString());
        holder.alunoHoraEntrada.setText(alunosHoraEntrada.get(position) == null ? "" : alunosHoraEntrada.get(position).toString());
        holder.alunoHoraSaida.setText(alunosHoraSaida.get(position) == null ? "" : alunosHoraSaida.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return alunosRGM.size();
    }

    public class AlunoViewHolder extends RecyclerView.ViewHolder {

        TextView alunoNome, alunoRGM, alunoEvento, alunoData, alunoHoraEntrada, alunoHoraSaida;

        public AlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            alunoNome = itemView.findViewById(R.id.alunoNome);
            alunoRGM = itemView.findViewById(R.id.alunoRgm);
            alunoEvento = itemView.findViewById(R.id.alunoEvento);
            alunoData = itemView.findViewById(R.id.alunoData);
            alunoHoraEntrada = itemView.findViewById(R.id.alunoHoraEntrada);
            alunoHoraSaida = itemView.findViewById(R.id.alunoHoraSaida);
        }
    }
}
