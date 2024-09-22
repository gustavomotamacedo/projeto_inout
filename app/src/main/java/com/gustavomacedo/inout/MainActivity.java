package com.gustavomacedo.inout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView alunosView;
    private Button btnScan, btnCsv, btnExportar, btnLimpar;

    private DbHelper dbHelper;
    private ArrayList<String> alunosId, alunosNome, alunosRGM, alunosCodigo, alunosData, alunosHoraEntrada, alunosHoraSaida, alunosPermanencia;

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

        alunosView = findViewById(R.id.alunosView);
        btnScan = findViewById(R.id.btnScan);
        btnCsv = findViewById(R.id.btnCsv);
        btnExportar = findViewById(R.id.btnExportar);
        btnLimpar = findViewById(R.id.btnLimpar);

        alunosId = new ArrayList<>();
        alunosNome = new ArrayList<>();
        alunosRGM = new ArrayList<>();
        alunosCodigo = new ArrayList<>();
        alunosData = new ArrayList<>();
        alunosHoraEntrada = new ArrayList<>();
        alunosHoraSaida = new ArrayList<>();
        alunosPermanencia = new ArrayList<>();

        dbHelper = new DbHelper(getApplicationContext());


        adicionarTodosDadosNosArrays();

        AlunoAdapter adapter = new AlunoAdapter(getApplicationContext(),
                alunosNome,
                alunosRGM,
                alunosData,
                alunosHoraEntrada,
                alunosHoraSaida,
                alunosPermanencia);

        alunosView.setAdapter(adapter);
        alunosView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btnScan.setOnClickListener(v -> {
            scanCode();
        });

        btnCsv.setOnClickListener(v -> {
            lerDadosDoCsv();
        });

        btnExportar.setOnClickListener(v -> {
            exportarDadosParaCsv();
        });

        btnLimpar.setOnClickListener(v -> {
            dbHelper.limparTabela();
            recreate();
        });
    }

    private void lerDadosDoCsv() {
        dbHelper.limparTabela();
        try {
            @SuppressLint("SdCardPath") List<CsvFormat> csvFormatList = new CsvToBeanBuilder(new FileReader("/data/data/com.gustavomacedo.inout/files/alunos.csv"))
                    .withType(CsvFormat.class).build().parse();

            for (CsvFormat aluno : csvFormatList) {
                dbHelper.adcAluno(aluno.getNome(), Integer.parseInt(aluno.getRgm()), Integer.parseInt(aluno.getCodigo()), new Date(), aluno.getEntrada(), aluno.getSaida(), aluno.getPermanencia());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        recreate();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Codigo do aluno");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                    dialog.dismiss();
                }
            }).show();

            String codigo = result.getContents();
            Log.d("PORRA", codigo);
            Log.d("PORRA", String.valueOf(Integer.parseInt(codigo.trim())));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dbHelper.atualizarEntradaESaida(codigo);
            }
        }
    });

    public void resetarArrays() {
        Cursor cursor = dbHelper.lerTodosOsDados();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            alunosId.clear();
            alunosNome.clear();
            alunosRGM.clear();
            alunosCodigo.clear();
            alunosData.clear();
            alunosHoraEntrada.clear();
            alunosHoraSaida.clear();
            alunosPermanencia.clear();
            while(cursor.moveToNext()) {
                alunosId.add(cursor.getString(0));
                alunosNome.add(cursor.getString(1));
                alunosRGM.add(cursor.getString(2));
                alunosCodigo.add(cursor.getString(3));
                alunosData.add(cursor.getString(4));
                alunosHoraEntrada.add(cursor.getString(5));
                alunosHoraSaida.add(cursor.getString(6));
                alunosPermanencia.add(cursor.getString(7));
            }
        }
    }

    public void adicionarTodosDadosNosArrays() {
        Cursor cursor = dbHelper.lerTodosOsDados();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alunosId.add(cursor.getString(0));
                alunosNome.add(cursor.getString(1));
                alunosRGM.add(cursor.getString(2));
                alunosCodigo.add(cursor.getString(3));
                alunosData.add(cursor.getString(4));
                alunosHoraEntrada.add(cursor.getString(5));
                alunosHoraSaida.add(cursor.getString(6));
                alunosPermanencia.add(cursor.getString(7));
            }
        }
    }

    public void exportarDadosParaCsv() {
        try {
            PrintWriter printWriter = new PrintWriter(new File("/data/data/com.gustavomacedo.inout/files/alunos.csv"));
            Cursor cursor = dbHelper.lerTodosOsDados();
            ArrayList<String> linhasArrayList = new ArrayList<String>();
            String[] linhas;
            if (cursor == null) {
                Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
            } else {
                resetarArrays();
                printWriter.write("");
                printWriter.println("_id,nome,rgm,codigo,data,hr_entrada,hr_saida,hr_permanencia");
                for (int i = 0; i < alunosId.size(); i++) {
                    printWriter.println(alunosId.get(i) + "," +
                            alunosNome.get(i) + "," +
                            alunosRGM.get(i) + "," +
                            alunosCodigo.get(i) + "," +
                            alunosData.get(i) + "," +
                            (alunosHoraEntrada.get(i) != null ? alunosHoraEntrada.get(i) : "") + "," +
                            (alunosHoraSaida.get(i) != null ? alunosHoraSaida.get(i) : "") + "," +
                            (alunosPermanencia.get(i) != null ? alunosPermanencia.get(i) : ""));
                }
                printWriter.close();
                Toast.makeText(this, "CSV EXPORTADO", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "PUTA MERDA", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        recreate();
    }

    public void mudarTela(View v){
        Intent in = new Intent(MainActivity.this, CreateAluno.class);
        startActivity(in);
    }
}