package com.gustavomacedo.inout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtInOut;
    private final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos.csv" ;
    private final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos.csv" ;
    private List<AlunoBean> alunoBeanList;
    private List<EventoBean> eventoBeanList;
    private DbHelper dbHelper;

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

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        try {
            //noinspection unchecked,rawtypes
            alunoBeanList = new CsvToBeanBuilder(new InputStreamReader(new FileInputStream(CSV_PATH_ALUNOS), StandardCharsets.ISO_8859_1))
                    .withType(AlunoBean.class)
                    .build()
                    .parse();
            //noinspection unchecked,rawtypes
            eventoBeanList = new CsvToBeanBuilder(new InputStreamReader(new FileInputStream(CSV_PATH_EVENTOS), StandardCharsets.ISO_8859_1))
                    .withType(EventoBean.class)
                    .build()
                    .parse();

            dbHelper = new DbHelper(this);

            Cursor eventos = dbHelper.lerTodosOsEventos();
            Cursor alunos = dbHelper.lerTodosOsAlunos();

            if (eventos.getCount() == 0) {
                // Tabela de eventos vazia
                for (int i = 0; i < eventoBeanList.size(); i++) {
                    dbHelper.adcEvento(eventoBeanList.get(i).getNome());
                }
            }
            else {
                // Tabela de eventos com valores
                Toast.makeText(this, "Eventos já adicionados", Toast.LENGTH_SHORT).show();
            }

            if (alunos.getCount() == 0) {
                // Tabela de alunos vazia
                for (int i = 0; i < alunoBeanList.size(); i++) {
                    AlunoBean alunoAux = alunoBeanList.get(i);
                    dbHelper.adcAluno(alunoAux.getNome(), Integer.parseInt(alunoAux.getRgm()), Integer.parseInt(alunoAux.getIdEvento()), alunoAux.getData());
                }
            } else {
                // Tabela de alunos com valores
                Toast.makeText(this, "Alunos já adicionados", Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        txtInOut = findViewById(R.id.txtInOut);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);

        anim.reset();
        txtInOut.clearAnimation();

        txtInOut.setAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent in = new Intent(MainActivity.this, EventosActivity.class);
                MainActivity.this.finish();
                startActivity(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}