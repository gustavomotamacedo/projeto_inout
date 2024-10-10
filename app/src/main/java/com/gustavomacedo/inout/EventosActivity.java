package com.gustavomacedo.inout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView eventosView;
    private ArrayList<String> eventosId, eventosNome, eventosQtdAlunos;
    private Button btnAddEvento, btnLerQrCode;
    private DbHelper dbHelper;

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

        eventosId = new ArrayList<>();
        eventosNome = new ArrayList<>();
        eventosQtdAlunos = new ArrayList<>();

        dbHelper = new DbHelper(this);

        adicionarTodosEventosNosArrays();

        EventoAdapter eventoAdapter = new EventoAdapter(this,
                eventosId,
                eventosNome,
                eventosQtdAlunos);

        eventosView.setAdapter(eventoAdapter);
        eventosView.setLayoutManager(new LinearLayoutManager(this));

        btnAddEvento.setOnClickListener(v -> {
            Intent in = new Intent(this, CreateEvento.class);
            finish();
            startActivity(in);
        });

        btnLerQrCode.setOnClickListener(v -> {
            scanCode();
        });
    }

    public void adicionarTodosEventosNosArrays() {
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
            Log.d("PORRA", alunoInfo[0]);
            Log.d("PORRA", alunoInfo[1]);
            Log.d("PORRA", alunoInfo[2]);
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