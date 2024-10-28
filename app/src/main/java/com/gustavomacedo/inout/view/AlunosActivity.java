package com.gustavomacedo.inout.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavomacedo.inout.R;
import com.gustavomacedo.inout.controller.AlunoAdapter;
import com.gustavomacedo.inout.model.DbHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class AlunosActivity extends AppCompatActivity {

    private RecyclerView alunosView;
    private Button btnScan;
    private String idEventoStr;
    private Intent in;
    private DbHelper dbHelper;

    private ArrayList<String> alunoRgm, alunoNome, alunoEvento, alunoHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alunos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        alunosView = findViewById(R.id.alunosView);
        btnScan = findViewById(R.id.btnScan);

        alunoNome = new ArrayList<>();
        alunoRgm = new ArrayList<>();
        alunoEvento = new ArrayList<>();
        alunoHorario = new ArrayList<>();
        in = getIntent();

        idEventoStr = in.getStringExtra("_id_evento");

        dbHelper = new DbHelper(this);

        inserirAlunosNosArrays();

        AlunoAdapter alunoAdapter = new AlunoAdapter(this, alunoNome, alunoRgm, alunoEvento, alunoHorario);

        alunosView.setAdapter(alunoAdapter);
        alunosView.setLayoutManager(new LinearLayoutManager(this));

        btnScan.setOnClickListener(v -> {
            scanCode();
        });
    }

    private void inserirAlunosNosArrays() {
        Cursor cursor = dbHelper.lerAlunosEmUmEvento(idEventoStr);
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alunoNome.add(String.valueOf(cursor.getString(0)));
                alunoRgm.add(String.valueOf(cursor.getString(1)));
                alunoEvento.add(String.valueOf(cursor.getString(2)));
                alunoHorario.add(String.valueOf(cursor.getString(3)));
            }
        }
    }

    public void scanCode() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume para cima para ligar o flash");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureScreenActivity.class);
        barLauncher.launch(scanOptions);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {

            String[] alunoInfo = result.getContents().split(",");

            String eventoId = in.getStringExtra("_id_evento");

            Cursor alunoIdCursor = dbHelper.lerIdAlunoPorRGM(alunoInfo[0]);
            alunoIdCursor.moveToNext();
            int alunoId = Integer.parseInt(alunoIdCursor.getString(0));
            Cursor alunoEventos = dbHelper.lerEventosPorAluno(alunoId);

            try {
                while (alunoEventos.moveToNext()) {
                    if (alunoEventos.getString(0).equals(eventoId)) {
                        dbHelper.atualizarEntradaDoAlunoEmUmEvento(alunoInfo[0], eventoId);
                        recreate();
                    } else {
                        Toast.makeText(this, "Alunos cadastrado em outro evento", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (CursorIndexOutOfBoundsException e){
                Log.d("PUNHETINHA", e.getMessage());
                AlertDialog.Builder builder = getBuilder(alunoInfo[0], alunoInfo[1], in.getStringExtra("_id_evento"));
                builder.show();
            }
        }

    });

    private AlertDialog.Builder getBuilder(String nome, String rgm, String idEvento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AlunosActivity.this);
        builder.setTitle("Cadastrar aluno no evento atual?");
        builder.setMessage("Aluno não cadastrado!\nDeseja cadastra-lo no evento atual?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recreate();
                dialogInterface.dismiss();
                dbHelper.addAluno(nome, rgm);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recreate();
                dialogInterface.dismiss();
                Toast.makeText(AlunosActivity.this, "Aluno não criado.", Toast.LENGTH_SHORT).show();
            }
        });
        return builder;
    }
}