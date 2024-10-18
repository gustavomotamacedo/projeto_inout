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

public class AddEventoActivity extends AppCompatActivity {

    private EditText edtEventoNome;
    private Button btnAdcEvento;

    private DbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_evento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEventoNome = findViewById(R.id.edtEventoNome);
        btnAdcEvento = findViewById(R.id.btnAdcEvento);

        myDb = new DbHelper(this);

        btnAdcEvento.setOnClickListener(v -> {
            myDb.adcEvento(edtEventoNome.getText().toString());
            Intent in = new Intent(this, EventosActivity.class);
            finish();
            startActivity(in);
        });
    }
}