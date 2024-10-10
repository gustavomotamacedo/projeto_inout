package com.gustavomacedo.inout;

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
import java.util.Arrays;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper { //TODO: REFATORAR TIPO DE DATA PARA LOCALDATETIME

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 2;

    private static final String ALUNOS_TABLE_NAME = "alunos";
    private static final String EVENTOS_TABLE_NAME = "eventos";

    // definindo as colunas da tabela de alunos como constantes
    private static final String ALUNOS_COLUMN_ID = "_id";
    private static final String ALUNOS_COLUMN_NOME = "nome";
    private static final String ALUNOS_COLUMN_RGM = "rgm";
    private static final String ALUNOS_COLUMN_ID_EVENTO = "id_evento";
    private static final String ALUNOS_COLUMN_DATA = "data";
    private static final String ALUNOS_COLUMN_HORA_ENTRADA = "hora_entrada";
    private static final String ALUNOS_COLUMN_HORA_SAIDA = "hora_saida";
    private static final String ALUNOS_COLUMN_TEMPO_PERMANENCIA = "tempo_permanencia";

    // definindo as colunas da tabela de eventos como constantes
    private static final String EVENTOS_COLUMN_ID = "_id";
    private static final String EVENTOS_COLUMN_NOME = "nome";
    private static final String EVENTOS_COLUMN_QUANTIDADE_ALUNOS = "qtd_alunos";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // comando para criar a tabela
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryAlunos = "CREATE TABLE "+ ALUNOS_TABLE_NAME +" (" +
                ALUNOS_COLUMN_ID +" INTEGER  PRIMARY KEY AUTOINCREMENT," +
                ALUNOS_COLUMN_NOME +" VARCHAR(255) NOT NULL," +
                ALUNOS_COLUMN_RGM +" INTEGER  NOT NULL," +
                ALUNOS_COLUMN_ID_EVENTO +" INTEGER  NOT NULL," +
                ALUNOS_COLUMN_DATA +" DATE DEFAULT NULL," +
                ALUNOS_COLUMN_HORA_ENTRADA +" TIME DEFAULT NULL," +
                ALUNOS_COLUMN_HORA_SAIDA +" TIME DEFAULT NULL," +
                ALUNOS_COLUMN_TEMPO_PERMANENCIA +" TIME DEFAULT NULL," +
                "FOREIGN KEY ("+ ALUNOS_COLUMN_ID_EVENTO +") REFERENCES eventos("+EVENTOS_COLUMN_ID+"));";

        String queryEventos = "CREATE TABLE " + EVENTOS_TABLE_NAME + " (" +
                EVENTOS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EVENTOS_COLUMN_NOME + " VARCHAR(255) NOT NULL," +
                EVENTOS_COLUMN_QUANTIDADE_ALUNOS + " INTEGER DEFAULT 0);";

        String trigger = "CREATE TRIGGER trigger_quantidade_alunos " + "\n" +
                "AFTER INSERT ON " + ALUNOS_TABLE_NAME + "\n"+
                "FOR EACH ROW \n" +
                "BEGIN \n" +
                "    UPDATE " + EVENTOS_TABLE_NAME+ "\n" +
                "    SET " + EVENTOS_COLUMN_QUANTIDADE_ALUNOS + " = " + EVENTOS_COLUMN_QUANTIDADE_ALUNOS + " + 1 " + "\n" +
                "    WHERE " + EVENTOS_COLUMN_ID + " = NEW." + ALUNOS_COLUMN_ID_EVENTO + ";\n" +
                "END;";

        db.execSQL(queryEventos);
        db.execSQL(queryAlunos);
        db.execSQL(trigger);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALUNOS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTOS_TABLE_NAME);
        onCreate(db);
    }

    // CODIGOS PARA O ALUNO

    // função de adicionar aluno
    void adcAluno(String nome, int rgm, int idEvento, @Nullable Date data, @Nullable Date horaE, @Nullable Date horaS, @Nullable Date perm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_NOME, nome);
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_ID_EVENTO, idEvento);
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
    void adcAluno(String nome, int rgm, int idEvento, Date data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_NOME, nome);
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_ID_EVENTO, idEvento);
        cv.put(ALUNOS_COLUMN_DATA, String.valueOf(DateFormat.format("yyyy-MM-dd", data)));
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

    public Cursor pesquisarAlunoPorIdEvento(int idEvento) {

        String query = "SELECT * FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_ID_EVENTO + "=" + idEvento;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
    // TODO : Função para retornar dados por nome
    // TODO : Função para retornar dados por rgm
    // TODO : Função para atualizar os dados
    // TODO : Função para deletar os dados

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"Range", "Recycle"})
    public void atualizarEntradaESaidaDoAluno(String codigo) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String hourFormat = "hh:mm:ss";
        Date now = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        
        Cursor line = null;
        
        long result = 0;

        if (dbWrite != null) {
            cv.put(ALUNOS_COLUMN_HORA_ENTRADA, (String) DateFormat.format(hourFormat, now));
            result = dbWrite.update(ALUNOS_TABLE_NAME, cv, ALUNOS_COLUMN_ID_EVENTO + "=? and " + ALUNOS_COLUMN_HORA_ENTRADA + " IS NULL", new String[]{codigo});
            if (result == 0) {
                cv.clear();
                cv.put(ALUNOS_COLUMN_HORA_SAIDA, (String) DateFormat.format(hourFormat, now));
                result = dbWrite.update(ALUNOS_TABLE_NAME, cv, ALUNOS_COLUMN_ID_EVENTO + "=? and " + ALUNOS_COLUMN_HORA_SAIDA + " IS NULL", new String[]{codigo});
                cv.clear();
                line = dbWrite.rawQuery("SELECT "+ ALUNOS_COLUMN_HORA_ENTRADA + ", " + ALUNOS_COLUMN_HORA_SAIDA +" FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_ID_EVENTO + "=?", new String[]{codigo});
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
                    result = dbWrite.update(ALUNOS_TABLE_NAME, cv, ALUNOS_COLUMN_ID_EVENTO + "=?", new String[]{codigo});
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("BUCETA", Arrays.toString(e.getStackTrace()));
                }
            }
        }

        if (result == -1) {
            Toast.makeText(context, "Deu ruim aqui", Toast.LENGTH_SHORT).show();
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
