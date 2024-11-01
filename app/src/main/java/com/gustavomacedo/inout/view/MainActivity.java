package com.gustavomacedo.inout.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gustavomacedo.inout.R;

public class MainActivity extends AppCompatActivity {

    private TextView txtInOut;
//    private final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos.csv" ;
//    private final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos.csv";
//    private final String CSV_PATH_ALUNOS_EVENTOS = "/data/data/com.gustavomacedo.inout/files/alunos_eventos.csv";
//    private List<AlunoBean> alunoBeanList;
//    private List<EventoBean> eventoBeanList;
//    private List<AlunoEventoBean> associacaoBeanList;
//    private DbHelper dbHelper;

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

//        dbHelper = new DbHelper(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

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

//    public void inserirEventosOrdenados() {
//        dbHelper.addEvento("Pair programming com ChatGPT", "2024-11-06 08:30:00");
//        dbHelper.addEvento("Ciência de Dados Análise Preditiva", "2024-11-06 08:30:00");
//        dbHelper.addEvento("Provedores de serviços inteligentes", "2024-11-06 08:30:00");
//        dbHelper.addEvento("IA. Robótica e Igualdade de Gênero", "2024-11-06 19:10:00");
//        dbHelper.addEvento("Oportunidades e Estratégias de Social Listening com IA", "2024-11-06 19:10:00");
//        dbHelper.addEvento("Transformando Negócios com Ciência de Dados e AI", "2024-11-06 19:10:00");
//        dbHelper.addEvento("Engenharia de Prompt", "2024-11-06 19:10:00");
//        dbHelper.addEvento("Segurança Cibernética : Revolução da Indústria 4.0", "2024-11-07 08:30:00");
//        dbHelper.addEvento("Educação Financeira", "2024-11-07 08:30:00");
//        dbHelper.addEvento("Aspectos Legais da Inteligência Artificial", "2024-11-07 08:30:00");
//        dbHelper.addEvento("Desafios e Oportunidades da Cibersegurança com a IA", "2024-11-07 19:10:00");
//        dbHelper.addEvento("IA Aplicada a Comunicação", "2024-11-07 19:10:00");
//        dbHelper.addEvento("IA aplicada a Diagnostico de Perdas Energéticas", "2024-11-07 19:10:00");
//        dbHelper.addEvento("O mundo pós IA impactos, economia e profissões", "2024-11-07 19:10:00");
//    }

//    private void adicionarAssociacaoNaBaseDeDados(List<AlunoEventoBean> alunoEventoBeanList) {
//        for (AlunoEventoBean ae : alunoEventoBeanList) {
//            dbHelper.addAlunoEvento(ae.getIdAluno(), ae.getIdEvento());
//        }
//    }
//
//    private void adicionarAlunosNaBaseDeDados(List<AlunoBean> alunoBeanList) {
//        for (AlunoBean a : alunoBeanList) {
//            dbHelper.addAluno(a.getRgm(), a.getNome());
//        }
//    }
//
//    private void adicionarEventosNaBaseDeDados(List<EventoBean> eventos) {
//        for (EventoBean e : eventos) {
//            dbHelper.addEvento(e.getNome(), e.getDataHora());
//        }
//    }
}