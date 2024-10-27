package com.gustavomacedo.inout.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 1;

    // armazenando nomes das tabelas em constantes
    private static final String ALUNOS_TABLE_NAME = "alunos";
    private static final String EVENTOS_TABLE_NAME = "eventos";
    private static final String ALUNOS_EVENTOS_TABLE_NAME = "alunos_eventos"; // tabela associativa

    // definindo as colunas da tabela de alunos como constantes
    private static final String ALUNOS_COLUMN_ID = "_id"; // chave primaria
    private static final String ALUNOS_COLUMN_RGM = "rgm";
    private static final String ALUNOS_COLUMN_NOME = "nome";

    // definindo as colunas da tabela de eventos como constantes
    private static final String EVENTOS_COLUMN_ID = "_id"; // chave primaria
    private static final String EVENTOS_COLUMN_NOME = "nome";
    private static final String EVENTOS_COLUMN_DATA_HORA = "data_hora"; // armazena dia e horário do evento

    // definindo as colunas da tabela de associação entre alunos e eventos como constantes
    private static final String ALUNOS_EVENTOS_COLUMN_ALUNO_ID = "aluno_id"; // chave estrangeira e chave primaria
    private static final String ALUNOS_EVENTOS_COLUMN_EVENTO_ID = "evento_id"; // chave estrangeira e chave primaria
    private static final String ALUNOS_EVENTOS_COLUMN_HORA_DE_ENTRADA = "hora_de_entrada";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // cria as tabelas no banco assim que ele inicializa
    @Override
    public void onCreate(SQLiteDatabase db) {
        String alunos_query = "CREATE TABLE IF NOT EXISTS "+ ALUNOS_TABLE_NAME +" (\n" +
                "    "+ ALUNOS_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    "+ ALUNOS_COLUMN_RGM +" INTEGER NOT NULL UNIQUE,\n" +
                "    "+ ALUNOS_COLUMN_NOME +" VARCHAR(255) NOT NULL\n" +
                ");";
        String eventos_query = "CREATE TABLE IF NOT EXISTS "+ EVENTOS_TABLE_NAME +" (\n" +
                "    "+ EVENTOS_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    "+ EVENTOS_COLUMN_NOME +" VARCHAR(255) NOT NULL,\n" +
                "    "+ EVENTOS_COLUMN_DATA_HORA +" DATETIME NOT NULL\n" +
                ");";
        String alunos_eventos_query = "CREATE TABLE IF NOT EXISTS "+ ALUNOS_EVENTOS_TABLE_NAME +" (\n" +
                "    "+ ALUNOS_EVENTOS_COLUMN_ALUNO_ID +" INTEGER NOT NULL,\n" +
                "    "+ ALUNOS_EVENTOS_COLUMN_EVENTO_ID +" INTEGER NOT NULL,\n" +
                "    "+ ALUNOS_EVENTOS_COLUMN_HORA_DE_ENTRADA +" DATETIME,\n" +
                "    PRIMARY KEY (id_aluno, id_evento),\n" +
                "    FOREIGN KEY (id_aluno) REFERENCES alunos(_id),\n" +
                "    FOREIGN KEY (id_evento) REFERENCES eventos(_id)\n" +
                ");";

        db.execSQL(alunos_query);
        db.execSQL(eventos_query);
        db.execSQL(alunos_eventos_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALUNOS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTOS_TABLE_NAME);
        db.execSQL("DROP TRIGGER IF EXISTS trigger_decrementa_quantidade_alunos");
        db.execSQL("DROP TRIGGER IF EXISTS trigger_incrementa_quantidade_alunos");
        onCreate(db);
    }

    // CODIGOS PARA O ALUNO

    // função de adicionar aluno
    void adcAluno(String nome, int rgm, int idEvento, @Nullable Date data, @Nullable Date horaE, @Nullable Date horaS, @Nullable Date perm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_NOME, nome);
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_ID_EVENTO_1, idEvento);
        cv.put(ALUNOS_COLUMN_DATA, data == null ? String.valueOf(DateFormat.format("yyyy-MM-dd", new Date())) : String.valueOf(DateFormat.format("yyyy-MM-dd", data)));
        cv.put(ALUNOS_COLUMN_HORA_ENTRADA, horaE == null ? null : String.valueOf(DateFormat.format("hh:mm:ss", horaE)));
        cv.put(ALUNOS_COLUMN_HORA_SAIDA, horaS == null ? null :  String.valueOf(DateFormat.format("hh:mm:ss", horaS)));
        cv.put(ALUNOS_COLUMN_TEMPO_PERMANENCIA, perm == null ? null : String.valueOf(DateFormat.format("hh:mm:ss", perm)));
        long resultado = db.insert(ALUNOS_TABLE_NAME, null, cv);

        if (resultado == -1){
            Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    // função para adicionar aluno sem informar os horários de entrada e saída
    public void adcAluno(String nome, int rgm, int idEvento, Date data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_NOME, nome);
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_ID_EVENTO_1, idEvento);
        cv.put(ALUNOS_COLUMN_DATA, String.valueOf(DateFormat.format("yyyy-MM-dd", data)));
        long resultado = db.insert(ALUNOS_TABLE_NAME, null, cv);

        if (resultado == -1){
            Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    void adcAluno(String nome, int rgm, String idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_NOME, nome);
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_ID_EVENTO_1, Integer.parseInt(idEvento));
        cv.put(ALUNOS_COLUMN_DATA, String.valueOf(DateFormat.format("yyyy-MM-dd", new Date())));
        long resultado = db.insert(ALUNOS_TABLE_NAME, null, cv);

        if (resultado == -1){
            Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }
    
    public Cursor lerTodosOsAlunos() {

        String query = "SELECT * FROM " + ALUNOS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor lerAlunoPorIdEvento(int idEvento) {
        String query = "SELECT * FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_ID_EVENTO_1 + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {String.valueOf(idEvento)});
        }

        return cursor;
    }
    // TODO : Função para retornar dados por nome
    public Cursor lerAlunoPorRGM(int rgm) {
        String query = "SELECT * FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_RGM + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {String.valueOf(rgm)});
        }

        return cursor;
    }
    // TODO : Função para atualizar os dados
    // TODO : Função para deletar os dados

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"Range", "Recycle"})
    public boolean atualizarEntradaESaidaDoAluno(String rgm) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String hourFormat = "hh:mm:ss";
        Date now = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        
        Cursor line = null;
        
        long result = 0;

        if (dbWrite != null) {
            cv.put(ALUNOS_COLUMN_HORA_ENTRADA, (String) DateFormat.format(hourFormat, now));
            result = dbWrite.update(ALUNOS_TABLE_NAME, cv, ALUNOS_COLUMN_RGM + "=? and " + ALUNOS_COLUMN_HORA_ENTRADA + " IS NULL", new String[]{rgm});
            if (result == 0) {
                cv.clear();
                cv.put(ALUNOS_COLUMN_HORA_SAIDA, (String) DateFormat.format(hourFormat, now));
                result = dbWrite.update(ALUNOS_TABLE_NAME, cv, ALUNOS_COLUMN_RGM + "=? and " + ALUNOS_COLUMN_HORA_SAIDA + " IS NULL", new String[]{rgm});
                cv.clear();
                line = dbWrite.rawQuery("SELECT "+ ALUNOS_COLUMN_HORA_ENTRADA + ", " + ALUNOS_COLUMN_HORA_SAIDA +" FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_RGM + "=?", new String[]{rgm});
                line.moveToFirst();
                try {
                    DateTimeFormatter formatterExpecificoParaEssaConta = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime entrada = null;
                    LocalTime saida = null;
                    Duration diff = null;

                    entrada = LocalTime.parse(line.getString(0), formatterExpecificoParaEssaConta);
                    saida = LocalTime.parse(line.getString(1), formatterExpecificoParaEssaConta);

                    assert saida != null;
                    assert entrada != null;
                    diff = Duration.between(entrada, saida);

                    LocalTime diferencaFormatada = LocalTime.MIDNIGHT.plus(diff);
                    String strDiff = diferencaFormatada.format(formatterExpecificoParaEssaConta);

                    cv.put(ALUNOS_COLUMN_TEMPO_PERMANENCIA, strDiff);
                    result = dbWrite.update(ALUNOS_TABLE_NAME, cv, ALUNOS_COLUMN_RGM + "=?", new String[]{rgm});
                } catch (Exception e) {
                    Toast.makeText(context, "Aluno não cadastrado", Toast.LENGTH_SHORT).show();
                    Log.d("CARALHO", String.valueOf(result));
                    return false;
                }
            }
        }

        if (result == -1) {
            Toast.makeText(context, "Aluno não cadastrado", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Horario atualizado", Toast.LENGTH_SHORT).show();
            return true;
        }
        
    }

    public void limparTabelaAlunos() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ALUNOS_TABLE_NAME, null, null);
    }

    public void resetarTestesAlunos() {
        limparTabelaAlunos();
        mockarDadosAlunos();
    }

    private void mockarDadosAlunos() {
        this.adcAluno(
                "Teste 001",
                12345678,
                1,
                new Date()
        );
        this.adcAluno(
                "Teste 002",
                12345677,
                1,
                new Date()
        );
        this.adcAluno(
                "Teste 003",
                12345676,
                2,
                new Date()
        );
    }

    // CODIGOS PARA O EVENTO

    public Cursor lerTodosOsEventos() {
        String query = "SELECT * FROM " + EVENTOS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor lerEventoPorId(int id) {
        String query = "SELECT * FROM " + EVENTOS_TABLE_NAME + " WHERE " + EVENTOS_COLUMN_ID + "=" + ALUNOS_COLUMN_ID_EVENTO_1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void adcEvento(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EVENTOS_COLUMN_NOME, nome);
        long resultado = db.insert(EVENTOS_TABLE_NAME, null, cv);

        if (resultado == -1) {
            Toast.makeText(context, "DISGRAÇA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "BALA", Toast.LENGTH_SHORT).show();
        }
    }

    public void limparTabelaEventos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENTOS_TABLE_NAME, null, null);
    }

    private void mockarDadosEventos() {
        this.adcEvento(
                "Teste 001"
        );
        this.adcEvento(
                "Teste 002"
        );
        this.adcEvento(
                "Teste 003"
        );
    }

    public void resetarTestesEventos() {
        limparTabelaEventos();
        mockarDadosEventos();
    }
}
