package com.gustavomacedo.inout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "alunos";

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

    // TODO : Função para deletar os dados
    public void deleta(String codigo){
        SQLiteDatabase db = this.getWritableDatabase();
        long resultado = db.delete(TABLE_NAME,COLUMN_CODIGO+"=?",new String[]{codigo});

        if (resultado == -1){
            Toast.makeText(context,"Erro ao deletar",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Deletado com sucesso",Toast.LENGTH_SHORT).show();
        }

    }

    
    // TODO : Função para retornar dados por nome
    // TODO : Função para retornar dados por rgm
    // TODO : Função para atualizar os dados

    // TODO : LIMPAR A TABELA
}
