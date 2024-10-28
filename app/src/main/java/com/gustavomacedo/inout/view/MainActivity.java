package com.gustavomacedo.inout.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gustavomacedo.inout.R;
import com.gustavomacedo.inout.model.AlunoBean;
import com.gustavomacedo.inout.model.AlunoEventoBean;
import com.gustavomacedo.inout.model.DbHelper;
import com.gustavomacedo.inout.model.EventoBean;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtInOut;
    private final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos.csv" ;
    private final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos.csv";
    private final String CSV_PATH_ALUNOS_EVENTOS = "/data/data/com.gustavomacedo.inout/files/alunos_eventos.csv";
    private List<AlunoBean> alunoBeanList;
    private List<EventoBean> eventoBeanList;
    private List<AlunoEventoBean> associacaoBeanList;
    private DbHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        File file = new File("/data/data/com.gustavomacedo.inout/databases", "InOut.db");
        boolean deleted = file.delete();

        dbHelper = new DbHelper(this);
        dbHelper.limparBanco();
        eventoBeanList = new ArrayList<>();
        alunoBeanList = new ArrayList<>();
        associacaoBeanList = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_PATH_EVENTOS))
                    .withSkipLines(1)
                    .build();
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                eventoBeanList.add(new EventoBean(nextLine[0], nextLine[1], nextLine[2]));
            }
        } catch (Exception e) {
            Toast.makeText(this, "FUDEU", Toast.LENGTH_SHORT).show();
        }

        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_PATH_ALUNOS))
                    .withSkipLines(1)
                    .build();
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                alunoBeanList.add(new AlunoBean(nextLine[0], nextLine[1], nextLine[2]));
            }
        } catch (Exception e) {
            Toast.makeText(this, "FUDEU", Toast.LENGTH_SHORT).show();
        }

        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_PATH_ALUNOS_EVENTOS))
                    .withSkipLines(1)
                    .build();
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                associacaoBeanList.add(new AlunoEventoBean(nextLine[0], nextLine[1], nextLine[2]));
            }
        } catch (Exception e) {
            Toast.makeText(this, "FUDEU", Toast.LENGTH_SHORT).show();
        }

        adicionarEventosNaBaseDeDados(eventoBeanList);
        adicionarAlunosNaBaseDeDados(alunoBeanList);
        adicionarAssociacaoNaBaseDeDados(associacaoBeanList);
        txtInOut = findViewById(R.id.txtInOut);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);

        anim.reset();
        txtInOut.clearAnimation();
        txtInOut.setAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent in = new Intent(MainActivity.this, EventosActivity.class);
                MainActivity.this.finish();
                startActivity(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void adicionarAssociacaoNaBaseDeDados(List<AlunoEventoBean> alunoEventoBeanList) {
        for (AlunoEventoBean ae : alunoEventoBeanList) {
            dbHelper.addAlunoEvento(ae.getIdAluno(), ae.getIdEvento());
        }
    }

    private void adicionarAlunosNaBaseDeDados(List<AlunoBean> alunoBeanList) {
        for (AlunoBean a : alunoBeanList) {
            dbHelper.addAluno(a.getRgm(), a.getNome());
        }
    }

    private void adicionarEventosNaBaseDeDados(List<EventoBean> eventos) {
        for (EventoBean e : eventos) {
            dbHelper.addEvento(e.getNome(), e.getDataHora());
        }
    }
}