package com.gustavomacedo.inout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class CreateAluno extends AppCompatActivity {
    EditText nome,rgm, codigo;
    EditText data;
    EditText horaE;
    EditText horaS;
    EditText perm;
    Button addButton;
    AlunoDbHelper myDB;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

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

        myDB = new AlunoDbHelper(getApplicationContext());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myDB.adcAluno(nome.getText().toString().trim(),
                    Integer.parseInt(String.valueOf(rgm.getText()).trim()),
                            Integer.parseInt(String.valueOf(codigo.getText()).trim()),
                            data.getText().toString().isEmpty() ? null : dateFormat.parse(String.valueOf(data.getText()).trim()),
                            horaE.getText().toString().isEmpty() ? null : hourFormat.parse(String.valueOf(horaE.getText()).trim()),
                            horaS.getText().toString().isEmpty() ? null : hourFormat.parse(String.valueOf(horaS.getText()).trim()),
                            perm.getText().toString().isEmpty() ? null : hourFormat.parse(String.valueOf(perm.getText()).trim()));
                } catch (ParseException e) {
                    Toast.makeText(CreateAluno.this, "EITA PORRA", Toast.LENGTH_SHORT).show();
                    Log.d("FUDEU", Objects.requireNonNull(e.getMessage()));
                }
                Intent in = new Intent(CreateAluno.this, MainActivity.class);
                startActivity(in);
            }

        });

    }

}