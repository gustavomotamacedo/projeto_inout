package com.gustavomacedo.inout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
                COLUMN_RGM +" INTEGER NOT NULL," +
                COLUMN_CODIGO +" INTEGER NOT NULL," +
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
    // TODO : Função para retornar todos os dados
    // TODO : Função para retornar dados por nome
    // TODO : Função para retornar dados por rgm
    // TODO : Função para retornar dados por codigo
    // TODO : Função para atualizar os dados
    // TODO : Função para deletar os dados
}
