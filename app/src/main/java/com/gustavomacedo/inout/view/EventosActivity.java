package com.gustavomacedo.inout.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavomacedo.inout.R;
import com.gustavomacedo.inout.controller.EventoAdapter;
import com.gustavomacedo.inout.model.DbHelper;

import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView eventosView;
    private ArrayList<String> eventosId, eventosNome, eventosHorario, eventosQtdAlunos;
    private ArrayList<String> alunoId, alunoRgm, alunoNome;
    private ArrayList<Cursor> eventosPorAluno;

    private Button btnAddEvento, btnExportarCsv;
    private DbHelper dbHelper;

    private static final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos_export.csv";
    private static final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos_export.csv";
    private static final String CSV_PATH_ALUNO_EVENTOS = "/data/data/com.gustavomacedo.inout/files/aluno_eventos_export.csv";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eventosView = findViewById(R.id.eventosView);
        btnAddEvento = findViewById(R.id.btnAddEvento);
        btnExportarCsv = findViewById(R.id.btnExportarCsv);

        dbHelper = new DbHelper(this);

        // Arrays da tabela de alunos
        alunoId = new ArrayList<>();
        alunoRgm = new ArrayList<>();
        alunoNome = new ArrayList<>();
        // Arrays da tabela de eventos
        eventosId = new ArrayList<>();
        eventosNome = new ArrayList<>();
        eventosHorario = new ArrayList<>();
        eventosQtdAlunos = new ArrayList<>();
        // Arrays da tabela de alunoeventos
        eventosPorAluno = new ArrayList<>();

        adicionaDadosAosArrays();

        EventoAdapter eventoAdapter = new EventoAdapter(this, eventosId, eventosNome, eventosHorario, eventosQtdAlunos);

        eventosView.setAdapter(eventoAdapter);
        eventosView.setLayoutManager(new LinearLayoutManager(this));

        btnAddEvento.setOnClickListener(v -> {
            Intent in = new Intent(this, AddEventoActivity.class);
            startActivity(in);
        });
    }

    public void adicionaDadosAosArrays() {
        Cursor eventosCursor = dbHelper.lerTodosOsEventos();
        if (eventosCursor != null) {
            while(eventosCursor.moveToNext()) {
                eventosId.add(String.valueOf(eventosCursor.getString(0)));
                eventosNome.add(String.valueOf(eventosCursor.getString(1)));
                eventosHorario.add(String.valueOf(eventosCursor.getString(2)));
                eventosQtdAlunos.add(String.valueOf(dbHelper.quantidadeDeAlunosEmUmEvento(eventosCursor.getString(0))));
            }
        } else {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        }
    }
}