package com.gustavomacedo.inout;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlunosActivity extends AppCompatActivity {

    private RecyclerView alunosView;
    private DbHelper myDb;
    private int idEvento;

    private ArrayList<String> alunoNome, alunoRgm, alunoData, alunoHoraEntrada, alunoHoraSaida, alunoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alunos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        alunoNome = new ArrayList<>();
        alunoRgm = new ArrayList<>();
        alunoEvento = new ArrayList<>();
        alunoData = new ArrayList<>();
        alunoHoraEntrada = new ArrayList<>();
        alunoHoraSaida = new ArrayList<>();

        alunosView = findViewById(R.id.alunosView);

        idEvento = Integer.parseInt(getIntent().getStringExtra("_id_evento").trim());

        myDb = new DbHelper(this);

        inserirAlunosNosArrays();

        AlunoAdapter alunoAdapter = new AlunoAdapter(this, alunoNome, alunoRgm, alunoEvento, alunoData, alunoHoraEntrada, alunoHoraSaida);

        alunosView.setAdapter(alunoAdapter);
        alunosView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void inserirAlunosNosArrays() {
        Cursor cursor = myDb.lerAlunoPorIdEvento(idEvento);
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alunoNome.add(String.valueOf(cursor.getString(1)));
                alunoRgm.add(String.valueOf(cursor.getString(2)));
                alunoEvento.add(String.valueOf(cursor.getString(3)));
                alunoData.add(String.valueOf(cursor.getString(4)));
                alunoHoraEntrada.add(String.valueOf(cursor.getString(5)));
                alunoHoraSaida.add(String.valueOf(cursor.getString(6)));
            }
        }
    }
}