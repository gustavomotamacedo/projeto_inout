package com.gustavomacedo.inout.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
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

import com.gustavomacedo.inout.controller.EventoAdapter;
import com.gustavomacedo.inout.R;
import com.gustavomacedo.inout.model.DbHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import java.io.FileWriter;
import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView eventosView;
    private ArrayList<String> eventosId, eventosNome, eventosQtdAlunos;
    private ArrayList<String> alunoId, alunoNome, alunoRgm, alunoEvento, alunoData, alunoHoraEntrada, alunoHoraSaida, alunoPermanencia;

    private Button btnAddEvento, btnLerQrCode, btnExportarCsv;
    private DbHelper dbHelper;

    private static final String CSV_PATH_ALUNOS = "/data/data/com.gustavomacedo.inout/files/alunos_export.csv";
    private static final String CSV_PATH_EVENTOS = "/data/data/com.gustavomacedo.inout/files/eventos_export.csv";

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

        eventosView = findViewById(R.id.eventosView);
        btnAddEvento = findViewById(R.id.btnAdcEvento);
        btnLerQrCode = findViewById(R.id.btnLerQrCode);
        btnExportarCsv = findViewById(R.id.btnExportarCsv);

        eventosId = new ArrayList<>();
        eventosNome = new ArrayList<>();
        eventosQtdAlunos = new ArrayList<>();

        alunoId = new ArrayList<>();
        alunoNome = new ArrayList<>();
        alunoRgm = new ArrayList<>();
        alunoEvento = new ArrayList<>();
        alunoData = new ArrayList<>();
        alunoHoraEntrada = new ArrayList<>();
        alunoHoraSaida = new ArrayList<>();
        alunoPermanencia = new ArrayList<>();

        dbHelper = new DbHelper(this);

        adicionarTodosEventosNosArrays();
        adicionarTodosAlunosNosArrays();

        EventoAdapter eventoAdapter = new EventoAdapter(this,
                eventosId,
                eventosNome,
                eventosQtdAlunos);

        eventosView.setAdapter(eventoAdapter);
        eventosView.setLayoutManager(new LinearLayoutManager(this));

        btnAddEvento.setOnClickListener(v -> {
            Intent in = new Intent(this, AddEventoActivity.class);
            finish();
            startActivity(in);
        });

        btnLerQrCode.setOnClickListener(v -> {
            scanCode();
        });

        btnExportarCsv.setOnClickListener(v -> {
            adicionarTodosEventosNosArrays();
            adicionarTodosAlunosNosArrays();
            try {
                CSVWriter alunoWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_PATH_ALUNOS))
                        .withSeparator(',')
                        .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                        .build();

                alunoWriter.flush();

                CSVWriter eventoWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_PATH_EVENTOS))
                        .withSeparator(',')
                        .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                        .build();

                eventoWriter.flush();

                String[] alunoCabecalho = {"_id", "nome", "rgm", "id_evento", "data", "entrada", "saida", "permanencia"};
                alunoWriter.writeNext(alunoCabecalho);

                String[] eventoCabecalho = {"_id", "nome", "qtd_alunos"};
                eventoWriter.writeNext(eventoCabecalho);

                for (int i = 0; i < alunoId.size(); i++) {
                    String[] abroba = {alunoId.get(i),
                    alunoNome.get(i),
                    alunoRgm.get(i),
                    alunoEvento.get(i),
                    alunoData.get(i),
                    alunoHoraEntrada.get(i),
                    alunoHoraSaida.get(i),
                    alunoPermanencia.get(i)};

                    alunoWriter.writeNext(abroba);
                }

                for (int i = 0; i < eventosId.size(); i++) {
                    String[] abrobrinha = {eventosId.get(i),
                            eventosNome.get(i),
                            eventosQtdAlunos.get(i)};

                    eventoWriter.writeNext(abrobrinha);
                }

                alunoWriter.close();
                eventoWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void adicionarTodosEventosNosArrays() {

        eventosId.clear();
        eventosNome.clear();
        eventosQtdAlunos.clear();

        Cursor cursor = dbHelper.lerTodosOsEventos();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                eventosId.add(cursor.getString(0));
                eventosNome.add(cursor.getString(1));
                eventosQtdAlunos.add(cursor.getString(2));
            }
        }
    }

    private void adicionarTodosAlunosNosArrays() {

        alunoId.clear();
        alunoNome.clear();
        alunoRgm.clear();
        alunoEvento.clear();
        alunoData.clear();
        alunoHoraEntrada.clear();
        alunoHoraSaida.clear();
        alunoPermanencia.clear();

        Cursor cursor = dbHelper.lerTodosOsAlunos();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alunoId.add(String.valueOf(cursor.getString(0)));
                alunoNome.add(String.valueOf(cursor.getString(1)));
                alunoRgm.add(String.valueOf(cursor.getString(2)));
                alunoEvento.add(String.valueOf(cursor.getString(3)));
                alunoData.add(String.valueOf(cursor.getString(4)));
                alunoHoraEntrada.add(String.valueOf(cursor.getString(5)));
                alunoHoraSaida.add(String.valueOf(cursor.getString(6)));
                alunoPermanencia.add(String.valueOf(cursor.getString(7)));
            }
        }
    }



//    private void lerAlunosDoCsv() {
//        alunoDbHelper.limparTabela();
//        try {
//            @SuppressLint("SdCardPath") List<CsvFormat> csvFormatList = new CsvToBeanBuilder(new FileReader("/data/data/com.gustavomacedo.inout/files/alunos.csv"))
//                    .withType(CsvFormat.class).build().parse();
//
//            for (CsvFormat aluno : csvFormatList) {
//                alunoDbHelper.adcAluno(aluno.getNome(), Integer.parseInt(aluno.getRgm()), Integer.parseInt(aluno.getCodigo()), new Date(), aluno.getEntrada(), aluno.getSaida(), aluno.getPermanencia());
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        recreate();
//    }

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
            AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
            builder.setTitle("Codigo do aluno");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                    dialog.dismiss();
                }
            }).show();

            String[] alunoInfo = result.getContents().split(",");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dbHelper.atualizarEntradaESaidaDoAluno(alunoInfo[1]);
            }
        }
    });

//    public void exportarDadosParaCsv() {
//        try {
//            PrintWriter printWriter = new PrintWriter(new File("/data/data/com.gustavomacedo.inout/files/alunos.csv"));
//            Cursor cursor = dbHelper.lerTodosOsDados();
//            ArrayList<String> linhasArrayList = new ArrayList<String>();
//            String[] linhas;
//            if (cursor == null) {
//                Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
//            } else {
//                resetarArrays();
//                printWriter.write("");
//                printWriter.println("_id,nome,rgm,id_evento,data,hr_entrada,hr_saida,hr_permanencia");
//                for (int i = 0; i < alunosId.size(); i++) {
//                    printWriter.println(alunosId.get(i) + "," +
//                            alunosNome.get(i) + "," +
//                            alunosRGM.get(i) + "," +
//                            alunosIdEvento.get(i) + "," +
//                            alunosData.get(i) + "," +
//                            (alunosHoraEntrada.get(i) != null ? alunosHoraEntrada.get(i) : "") + "," +
//                            (alunosHoraSaida.get(i) != null ? alunosHoraSaida.get(i) : ""));
//                }
//                printWriter.close();
//                Toast.makeText(this, "CSV EXPORTADO", Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            Toast.makeText(this, "PUTA MERDA", Toast.LENGTH_SHORT).show();
//            throw new RuntimeException(e);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        recreate();
//    }
}