package com.gustavomacedo.inout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtInOut;
    private final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos.csv" ;
    private final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos.csv" ;
    private final String CSV_PATH_ALUNOS_EVENTOS = "/data/data/com.gustavomacedo.inout/files/alunos_eventos.csv" ;
    private List<AlunoBean> alunoBeanList;
    private List<EventoBean> eventoBeanList;
    private List<AlunoEventoBean> alunoEventoBeanList;
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

        DbHelper myDb = new DbHelper(this);

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