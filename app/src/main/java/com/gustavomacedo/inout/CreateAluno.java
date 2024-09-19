package com.gustavomacedo.inout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class CreateAluno extends AppCompatActivity {
    EditText nome,rgm, codigo;
    EditText data;
    EditText horaE;
    EditText horaS;
    EditText perm;
    Button addButton;
    SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss");

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
        nome = findViewById(R.id.editTextText4);
        rgm = findViewById(R.id.editTextText6);
        codigo = findViewById(R.id.editTextText7);
        data = findViewById(R.id.editTextDate3);
        horaE = findViewById(R.id.editTextTime4);
        horaS = findViewById(R.id.editTextTime5);
        perm = findViewById(R.id.editTextTime6);
        addButton = findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper myDB = new DbHelper(CreateAluno.this);
                try {
                    myDB.adcAluno(nome.getText().toString().trim(),
                    Integer.valueOf(rgm.getText().toString().toString().trim()),
                            Integer.valueOf(codigo.getText().toString().toString().trim()),
                            formatD.parse(data.getText().toString().trim()),
                            hora.parse(horaE.getText().toString().trim()),
                            hora.parse(horaS.getText().toString().trim()),
                            hora.parse(perm.getText().toString().trim()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Intent in = new Intent(CreateAluno.this, MainActivity.class);
                startActivity(in);
            }

        });

    }

}