package com.gustavomacedo.inout.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.gustavomacedo.inout.controller.AlunoAdapter;
import com.gustavomacedo.inout.model.AlunoBean;
import com.gustavomacedo.inout.model.DbHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView alunosView;
    private ArrayList<String> alunoId, alunosNome, alunosRgm, alunoEntrada;
    private ArrayList<AlunoBean> alunoBeanArrayList;

    private Button btnExportarCsv;
    private ImageButton btnScanner;
    private DbHelper dbHelper;

    private static final String CSV_PATH_ALUNOS = "/sdcard/Documents/alunos_export.csv";

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

        // instância do banco de dados
        dbHelper = new DbHelper(this);

        // instanciando views
        alunosView = findViewById(R.id.alunosView);
        btnExportarCsv = findViewById(R.id.btnExportarCsv);
        btnScanner = findViewById(R.id.btnScanner);

        // Arrays da tabela de alunos
        alunoId = new ArrayList<>();
        alunosNome = new ArrayList<>();
        alunosRgm = new ArrayList<>();
        alunoEntrada = new ArrayList<>();
        // Array de beans de alunos
        alunoBeanArrayList = new ArrayList<>();

        adicionaDadosAosArrays();
        criarBeansDasTabelas();

        AlunoAdapter alunoAdapter = new AlunoAdapter(this, alunosRgm, alunosNome, alunoEntrada);

        alunosView.setAdapter(alunoAdapter);
        alunosView.setLayoutManager(new LinearLayoutManager(this));

        btnScanner.setOnClickListener(view -> scanCode());

        btnExportarCsv.setOnClickListener(view -> {
            try {
                CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_PATH_ALUNOS))
                        .withSeparator(',')
                        .build();
                writer.flush();
                // feed in your array (or convert your data to an array)
                writer.writeNext(new String[] {"nome", "rgm"}, true);
                for (AlunoBean a : alunoBeanArrayList) {
                    String[] entrie = new String[] {a.getNome(), a.getRgm()};
                    writer.writeNext(entrie, true);
                }
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void criarBeansDasTabelas() {
        for (int i = 0; i < alunoId.size(); i++) {
            alunoBeanArrayList.add(new AlunoBean(
                    alunoId.get(i),
                    alunosNome.get(i),
                    alunosRgm.get(i),
                    alunoEntrada.get(i)
            ));
        }
    }

    public void adicionaDadosAosArrays() {
        Cursor alunosCursor = dbHelper.lerTodosOsAlunos();
        if (alunosCursor != null) {
            while(alunosCursor.moveToNext()) {
                try {
                    Log.d("alunoCursor", alunosCursor.getString(3));
                    alunoId.add(String.valueOf(alunosCursor.getString(0)));
                    alunosRgm.add(String.valueOf(alunosCursor.getString(1)));
                    alunoEntrada.add(String.valueOf(alunosCursor.getString(2)));
                    alunosNome.add(String.valueOf(alunosCursor.getString(3)));
                } catch (IllegalStateException e) {
                    Toast.makeText(this, "ERRO: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("alunoCursor", alunosCursor.getString(3));
                }
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
            String nome = result.getContents().split(",")[1].strip();
            String evento1 = result.getContents().split(",")[2].strip();
            String evento2 = result.getContents().split(",")[3].strip();

            showAlertDialog(new String[] {rgm, nome, evento1, evento2});
        }
    });

    private void showAlertDialog(String[] alunoStr) {
        String rgm = alunoStr[0];
        String nome = alunoStr[1];
        String evento1 = alunoStr[2];
        String evento2 = alunoStr[3];
        AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
        builder.setTitle("Informações do Aluno");
        builder.setMessage("NOME: " + nome +
                "\nRGM : " + rgm +
                "\nEVENTO 1 : " + evento1 +
                "\nEVENTO 2 : " + evento2);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String entrada = sdf.format(new Date());
                recreate();
                dialogInterface.dismiss();
                dbHelper.addAluno(nome, rgm, entrada);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recreate();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
