package com.gustavomacedo.inout;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView alunosView;
    private Button btnScan;

    private DbHelper dbHelper;
    private ArrayList<String> alunosNome, alunosRGM, alunosData, alunosHoraEntrada, alunosHoraSaida, alunosPermanencia;

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

        alunosNome = new ArrayList<>();
        alunosRGM = new ArrayList<>();
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

    public void adicionarTodosDadosNosArrays() {
        Cursor cursor = dbHelper.lerTodosOsDados();
        if (cursor == null) {
            Toast.makeText(this, "Não há dados", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alunosNome.add(cursor.getString(1));
                alunosRGM.add(cursor.getString(2));
                alunosData.add(cursor.getString(4));
                alunosHoraEntrada.add(cursor.getString(5));
                alunosHoraSaida.add(cursor.getString(6));
                alunosPermanencia.add(cursor.getString(7));
            }
        }
    }

    public void mudarTela(View v){
        Intent in = new Intent(MainActivity.this, CreateAluno.class);
        startActivity(in);
    }
}