package com.gustavomacedo.inout.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gustavomacedo.inout.R;
import com.gustavomacedo.inout.model.DbHelper;

import java.util.Date;


public class AddAlunoActivity extends AppCompatActivity {

    private EditText nome,rgm, idEvento;
    private Button addButton;
    private DbHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_aluno);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nome = findViewById(R.id.alunoNome);
        rgm = findViewById(R.id.alunoRgm);
        idEvento = findViewById(R.id.alunoIdEvento);
        addButton = findViewById(R.id.btnAdicionar);

        idEvento.setText(getIntent().getStringExtra("id_evento"));

        myDB = new DbHelper(this);

        addButton.setOnClickListener(v -> {
            myDB.adcAluno(nome.getText().toString(),
                    Integer.parseInt(rgm.getText().toString()),
                    Integer.parseInt(idEvento.getText().toString()),
                    new Date());
            Intent in = new Intent(AddAlunoActivity.this, EventosActivity.class);
            startActivity(in);
        });

    }

}