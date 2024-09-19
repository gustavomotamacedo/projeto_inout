package com.gustavomacedo.inout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView alunosView;

    private DbHelper dbHelper;
    private ArrayList<String> alunosNome, alunosRGM, alunosData, alunosHoraEntrada, alunosHoraSaida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        alunosView = findViewById(R.id.alunosView);

        alunosNome = new ArrayList<>();
        alunosRGM = new ArrayList<>();
        alunosData = new ArrayList<>();
        alunosHoraEntrada = new ArrayList<>();
        alunosHoraSaida = new ArrayList<>();

        dbHelper = new DbHelper(getApplicationContext());

        adicionarTodosDadosNosArrays();

        AlunoAdapter adapter = new AlunoAdapter(getApplicationContext(),
                alunosNome,
                alunosRGM,
                alunosData,
                alunosHoraEntrada,
                alunosHoraSaida);

        alunosView.setAdapter(adapter);
        alunosView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public void adicionarTodosDadosNosArrays() {
        Cursor cursor = dbHelper.lerTodosOsDados();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alunosNome.add(cursor.getString(0));
                alunosRGM.add(cursor.getString(1));
                alunosData.add(cursor.getString(3));
                alunosHoraEntrada.add(cursor.getString(4));
                alunosHoraSaida.add(cursor.getString(5));
            }
        }
    }

    public void mudarTela(View v){
        Intent in = new Intent(MainActivity.this, CreateAluno.class);
        startActivity(in);
    }
}