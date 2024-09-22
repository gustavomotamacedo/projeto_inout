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

public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "alunos";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_RGM = "rgm";
    private static final String COLUMN_CODIGO = "codigo";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_HORA_ENTRADA = "hora_entrada";
    private static final String COLUMN_HORA_SAIDA = "hora_saida";
    private static final String COLUMN_TEMPO_PERMANENCIA = "tempo_permanencia";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_NAME +" (" +
                COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOME +" VARCHAR(255) NOT NULL," +
                COLUMN_RGM +" INT NOT NULL," +
                COLUMN_CODIGO +" INT NOT NULL," +
                COLUMN_DATA +" DATE DEFAULT NULL," +
                COLUMN_HORA_ENTRADA +" TIME DEFAULT NULL," +
                COLUMN_HORA_SAIDA +" TIME DEFAULT NULL," +
                COLUMN_TEMPO_PERMANENCIA +" TIME DEFAULT NULL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // TODO : Função para adicionar
    void adcAluno(String nome, int rgm, int codigo, @Nullable Date data, @Nullable Date horaE, @Nullable Date horaS, @Nullable Date perm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOME, nome);
        cv.put(COLUMN_RGM, rgm);
        cv.put(COLUMN_CODIGO, codigo);
        cv.put(COLUMN_DATA, data == null ? String.valueOf(DateFormat.format("yyyy-MM-dd", new Date())) : String.valueOf(DateFormat.format("yyyy-MM-dd", data)));
        cv.put(COLUMN_HORA_ENTRADA, horaE == null ? null : String.valueOf(DateFormat.format("hh:mm:ss", horaE)));
        cv.put(COLUMN_HORA_SAIDA, horaS == null ? null :  String.valueOf(DateFormat.format("hh:mm:ss", horaS)));
        cv.put(COLUMN_TEMPO_PERMANENCIA, perm == null ? null : String.valueOf(DateFormat.format("hh:mm:ss", perm)));
        long resultado = db.insert(TABLE_NAME, null, cv);

        if (resultado == -1){
            Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    void adcAluno(String nome, int rgm, int codigo, Date data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOME, nome);
        cv.put(COLUMN_RGM, rgm);
        cv.put(COLUMN_CODIGO, codigo);
        cv.put(COLUMN_DATA, String.valueOf(DateFormat.format("yyyy-MM-dd", data)));
        long resultado = db.insert(TABLE_NAME, null, cv);

        if (resultado == -1){
            Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }
    
    public Cursor lerTodosOsDados() {

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor pesquisarPorCodigo(int codigo) {

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CODIGO + "=" + codigo;

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
    public void atualizarEntradaESaida(String codigo) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        String hourFormat = "hh:mm:ss";
        Date now = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        
        Cursor line = null;
        
        long result = 0;

        if (dbWrite != null) {
            cv.put(COLUMN_HORA_ENTRADA, (String) DateFormat.format(hourFormat, now));
            result = dbWrite.update(TABLE_NAME, cv, COLUMN_CODIGO + "=? and " + COLUMN_HORA_ENTRADA + " IS NULL", new String[]{codigo});
            if (result == 0) {
                cv.clear();
                cv.put(COLUMN_HORA_SAIDA, (String) DateFormat.format(hourFormat, now));
                result = dbWrite.update(TABLE_NAME, cv, COLUMN_CODIGO + "=? and " + COLUMN_HORA_SAIDA + " IS NULL", new String[]{codigo});
                cv.clear();
                line = dbWrite.rawQuery("SELECT "+ COLUMN_HORA_ENTRADA + ", " + COLUMN_HORA_SAIDA +" FROM " + TABLE_NAME + " WHERE " + COLUMN_CODIGO + "=?", new String[]{codigo});
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

                    cv.put(COLUMN_TEMPO_PERMANENCIA, strDiff);
                    result = dbWrite.update(TABLE_NAME, cv, COLUMN_CODIGO + "=?", new String[]{codigo});
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

    public void limparTabela() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void resetarTestes() {
        limparTabela();
        mockarDados();
    }

    private void mockarDados() {
        this.adcAluno(
                "Teste 001",
                12345678,
                919591948,
                new Date()
        );
        this.adcAluno(
                "Teste 002",
                12345677,
                681627626,
                new Date()
        );
        this.adcAluno(
                "Teste 003",
                12345676,
                41959718,
                new Date()
        );
    }
}
