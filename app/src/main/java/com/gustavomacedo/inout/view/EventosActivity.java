package com.gustavomacedo.inout.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.gustavomacedo.inout.controller.EventoAdapter;
import com.gustavomacedo.inout.model.AlunoBean;
import com.gustavomacedo.inout.model.DbHelper;
import com.gustavomacedo.inout.model.EventoBean;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView eventosView;
    private ArrayList<String> eventosId, eventosNome, eventosHorario, eventosQtdAlunos;
    private ArrayList<String> alunoId, alunoRgm, alunoNome;
    private ArrayList<Cursor> eventosPorAluno;
    private ArrayList<EventoBean> eventoBeanArrayList;
    private ArrayList<AlunoBean> alunoBeanArrayList;

    private Button btnAddEvento, btnExportarCsv;
    private ImageButton btnScanner;
    private DbHelper dbHelper;

    private static final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos_export.csv";
    private static final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos_export.csv";
    private static final String CSV_PATH_ALUNO_EVENTOS = "/data/data/com.gustavomacedo.inout/files/aluno_eventos_export.csv";

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

        File dir = getDataDir();
        File file = new File(dir, "InOut.db");
        boolean deleted = file.delete();

        eventosView = findViewById(R.id.eventosView);
        btnAddEvento = findViewById(R.id.btnAddEvento);
        btnExportarCsv = findViewById(R.id.btnExportarCsv);
        btnScanner = findViewById(R.id.btnScanner);

        dbHelper = new DbHelper(this);

        // Arrays da tabela de alunos
        alunoId = new ArrayList<>();
        alunoRgm = new ArrayList<>();
        alunoNome = new ArrayList<>();
        // Arrays da tabela de eventos
        eventosId = new ArrayList<>();
        eventosNome = new ArrayList<>();
        eventosHorario = new ArrayList<>();
        eventosQtdAlunos = new ArrayList<>();
        // Arrays da tabela de alunoeventos
        eventosPorAluno = new ArrayList<>();

        eventoBeanArrayList = new ArrayList<>();
        alunoBeanArrayList = new ArrayList<>();

        adicionaDadosAosArrays();
        criarBeansDasTabelas();

        EventoAdapter eventoAdapter = new EventoAdapter(this, eventosId, eventosNome, eventosHorario, eventosQtdAlunos);

        eventosView.setAdapter(eventoAdapter);
        eventosView.setLayoutManager(new LinearLayoutManager(this));

        btnAddEvento.setOnClickListener(v -> {
            Intent in = new Intent(this, AddEventoActivity.class);
            startActivity(in);
        });

        btnScanner.setOnClickListener(view -> scanCode());

        btnExportarCsv.setOnClickListener(view -> {
            try {
                CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_PATH_EVENTOS))
                        .withSeparator(',')
                        .build();
                // feed in your array (or convert your data to an array)
                String entrie = "";
                for (EventoBean e : eventoBeanArrayList) {
                    entrie += e.toString() + ",";
                }
                String[] entries = entrie.split(",");
                writer.writeNext(entries);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_PATH_ALUNOS))
                        .withSeparator(',')
                        .build();
                // feed in your array (or convert your data to an array)
                String entrie = "";
                for (AlunoBean a : alunoBeanArrayList) {
                    entrie += a.toString() + ",";
                }
                String[] entries = entrie.split(",");
                writer.writeNext(entries);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void criarBeansDasTabelas() {
        for (int i = 0; i < eventosId.size(); i++){
            eventoBeanArrayList.add(new EventoBean(
                    eventosId.get(i),
                    eventosNome.get(i),
                    eventosHorario.get(i)));
        }
        for (int i = 0; i < alunoId.size(); i++) {
            alunoBeanArrayList.add(new AlunoBean(
                    alunoId.get(i),
                    alunoRgm.get(i),
                    alunoNome.get(i)
            ));
        }
    }

    public void adicionaDadosAosArrays() {
        Cursor eventosCursor = dbHelper.lerTodosOsEventos();
        if (eventosCursor != null) {
            while(eventosCursor.moveToNext()) {
                eventosId.add(String.valueOf(eventosCursor.getString(0)));
                eventosNome.add(String.valueOf(eventosCursor.getString(1)));
                eventosHorario.add(String.valueOf(eventosCursor.getString(2)));
                eventosQtdAlunos.add(String.valueOf(dbHelper.quantidadeDeAlunosEmUmEvento(eventosCursor.getString(0))));
            }
        } else {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        }
    }

    public void scanCode() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume para cima para ligar o flash");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureScreenActivity.class);
        scanOptions.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        barLauncher.launch(scanOptions);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            
            String rgm = result.getContents().split(",")[0];
            String nome = result.getContents().split(",")[1].replace("%20", " ");

            showAlertDialog("RGM : " + rgm
                    + "\nNome : " + nome);
        }
    });

    private void showAlertDialog(String alunoStr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
        builder.setTitle("Informações do Aluno");
        builder.setMessage(alunoStr);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recreate();
                dialogInterface.dismiss();
                // dbHelper.addAluno(nome, rgm);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recreate();
                dialogInterface.dismiss();
                // Toast.makeText(AlunosActivity.this, "Aluno não criado.", Toast.LENGTH_SHORT).show();
            }
        });

        // Aqui você chama o show() para exibir o diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
