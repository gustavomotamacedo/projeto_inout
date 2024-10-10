package com.gustavomacedo.inout;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EventosDbHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "eventos";

    //definindo as colunas da tabela de eventos
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_QUANTIDADE_ALUNOS = "qtd_alunos";

    public EventosDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOME + " VARCHAR(255) NOT NULL, " +
                COLUMN_QUANTIDADE_ALUNOS + " INT DEFAULT 0)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void adcEvento(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOME, nome);
        long resultado = db.insert(TABLE_NAME, null, cv);
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnQuantidadeAlunos() {
        return COLUMN_QUANTIDADE_ALUNOS;
    }
}
