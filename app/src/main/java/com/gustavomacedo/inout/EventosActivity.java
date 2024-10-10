package com.gustavomacedo.inout;

import android.annotation.SuppressLint;
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

public class EventosActivity extends AppCompatActivity {

    private RecyclerView eventosView;
    private ArrayList<String> eventosId, eventosNome, eventosQtdAlunos;
    private DbHelper dbHelper;

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

        eventosId = new ArrayList<>();
        eventosNome = new ArrayList<>();
        eventosQtdAlunos = new ArrayList<>();

        dbHelper = new DbHelper(this);

        adicionarTodosDadosNosArrays();

        EventoAdapter eventoAdapter = new EventoAdapter(this,
                eventosId,
                eventosNome,
                eventosQtdAlunos);

        eventosView.setAdapter(eventoAdapter);
        eventosView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void adicionarTodosDadosNosArrays() {
        Cursor cursor = dbHelper.lerTodosOsEventos();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                eventosId.add(cursor.getString(0));
                eventosNome.add(cursor.getString(1));
                eventosQtdAlunos.add(cursor.getString(2));
            }
        }
    }
}