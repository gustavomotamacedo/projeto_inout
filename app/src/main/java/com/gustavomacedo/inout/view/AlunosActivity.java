package com.gustavomacedo.inout.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavomacedo.inout.R;
import com.gustavomacedo.inout.model.DbHelper;

import java.util.ArrayList;

public class AlunosActivity extends AppCompatActivity {

    private RecyclerView alunosView;
    private Button btnScan;
    private String idEventoStr;
    private Intent in;
    private DbHelper dbHelper;

    private ArrayList<String> alunoRgm, alunoNome, alunoHorario;

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

//        alunosView = findViewById(R.id.alunosView);
//        btnScan = findViewById(R.id.btnScanner);
//
//        alunoNome = new ArrayList<>();
//        alunoRgm = new ArrayList<>();
//        alunoHorario = new ArrayList<>();
//        in = getIntent();
//
//        idEventoStr = in.getStringExtra("_id_evento");
//
//        dbHelper = new DbHelper(this);
//
//        inserirAlunosNosArrays();
//
//        AlunoAdapter alunoAdapter = new AlunoAdapter(this, alunoNome, alunoRgm, alunoHorario);
//
//        alunosView.setAdapter(alunoAdapter);
//        alunosView.setLayoutManager(new LinearLayoutManager(this));
//
//        btnScan.setOnClickListener(v -> {
//            scanCode();
//        });
    }
//
//    private void inserirAlunosNosArrays() {
//        Cursor cursor = dbHelper.lerAlunosEmUmEvento(idEventoStr);
//        if (cursor == null) {
//            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
//        } else {
//            while(cursor.moveToNext()) {
//                alunoRgm.add(String.valueOf(cursor.getString(1)));
//                alunoNome.add(String.valueOf(cursor.getString(2)));
//                alunoHorario.add(String.valueOf(cursor.getString(3)));
//            }
//        }
//    }
//
//    public void scanCode() {
//        ScanOptions scanOptions = new ScanOptions();
//        scanOptions.setPrompt("Volume para cima para ligar o flash");
//        scanOptions.setBeepEnabled(true);
//        scanOptions.setOrientationLocked(true);
//        scanOptions.setCaptureActivity(CaptureScreenActivity.class);
//        scanOptions.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
//        barLauncher.launch(scanOptions);
//    }
//
//    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
//        if (result.getContents() != null) {
//
//            String rgm = result.getContents().split(",")[0];
//            String nome = result.getContents().split(",")[1].replace("%20", " ");
//
//            String eventoId = in.getStringExtra("_id_evento");
//
//            Cursor alunoIdCursor = dbHelper.lerIdAlunoPorRGM(rgm);
//            alunoIdCursor.moveToNext();
//            int alunoId = Integer.parseInt(alunoIdCursor.getString(0));
//            Cursor alunoEventos = dbHelper.lerEventosPorAluno(alunoId);
//
//            try {
//                while (alunoEventos.moveToNext()) {
//                    if (alunoEventos.getString(0).equals(eventoId)) {
//                        dbHelper.atualizarEntradaDoAlunoEmUmEvento(rgm, eventoId);
//                    }
//                }
//            } catch (CursorIndexOutOfBoundsException e){
//                Log.d("PUNHETINHA", e.getMessage());
////                AlertDialog.Builder builder = getBuilder(alunoInfo[0], alunoInfo[1], in.getStringExtra("_id_evento"));
////                builder.show();
//            }
//
//            showAlertDialog("RGM : " + rgm
//                            + "\nNome : " + nome);
//        }
//    });
//
//    private void showAlertDialog(String alunoStr) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(AlunosActivity.this);
//        builder.setTitle("Informações do Aluno");
//        builder.setMessage(alunoStr);
//        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                recreate();
//                dialogInterface.dismiss();
//                // dbHelper.addAluno(nome, rgm);
//            }
//        });
//        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                recreate();
//                dialogInterface.dismiss();
//                // Toast.makeText(AlunosActivity.this, "Aluno não criado.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Aqui você chama o show() para exibir o diálogo
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
}
