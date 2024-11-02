package com.gustavomacedo.inout.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 3; // Atualize para a nova versão

    // armazenando nomes das tabelas em constantes
    private static final String ALUNOS_TABLE_NAME = "alunos";

    // definindo as colunas da tabela de alunos como constantes
    private static final String ALUNOS_COLUMN_ID = "_id"; // chave primaria
    private static final String ALUNOS_COLUMN_RGM = "rgm";
    private static final String ALUNOS_COLUMN_NOME = "nome";
    private static final String ALUNOS_COLUMN_HORA_DE_ENTRADA = "hora_de_entrada"; // HH:mm:ss

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // cria as tabelas no banco assim que ele inicializa
    @Override
    public void onCreate(SQLiteDatabase db) {
        String alunos_query = "CREATE TABLE "+ ALUNOS_TABLE_NAME +" (" +
                "    "+ ALUNOS_COLUMN_ID +" INTEGER PRIMARY KEY," +
                "    "+ ALUNOS_COLUMN_RGM +" INTEGER NOT NULL UNIQUE," +
                "    "+ ALUNOS_COLUMN_HORA_DE_ENTRADA +" DATETIME," +
                "    "+ ALUNOS_COLUMN_NOME +" VARCHAR(255) NOT NULL);";

        db.execSQL(alunos_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS " + ALUNOS_TABLE_NAME + ";");

            String alunos_query = "CREATE TABLE "+ ALUNOS_TABLE_NAME +" (" +
                    "    "+ ALUNOS_COLUMN_ID +" INTEGER PRIMARY KEY," +
                    "    "+ ALUNOS_COLUMN_RGM +" INTEGER NOT NULL UNIQUE," +
                    "    "+ ALUNOS_COLUMN_HORA_DE_ENTRADA +" DATETIME," +
                    "    "+ ALUNOS_COLUMN_NOME +" VARCHAR(255) NOT NULL);";

            db.execSQL(alunos_query);
        }
    }

    // CODIGOS PARA O ALUNO

    // função de adicionar aluno
    public void addAluno(String rgm, String nome, String entrada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_NOME, nome);
        cv.put(ALUNOS_COLUMN_HORA_DE_ENTRADA, entrada);
        long resultado = db.insert(ALUNOS_TABLE_NAME, null, cv);

        if (resultado == -1){
            Toast.makeText(context, "Falha ao adicionar o aluno", Toast.LENGTH_SHORT).show();
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
}
