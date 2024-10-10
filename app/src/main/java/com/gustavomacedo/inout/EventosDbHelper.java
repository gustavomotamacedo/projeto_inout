package com.gustavomacedo.inout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class EventosDbHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "InOut.db";
    private static final int DATABASE_VERSION = 2;

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
                COLUMN_NOME + " VARCHAR(255) NOT NULL," +
                COLUMN_QUANTIDADE_ALUNOS + " INTEGER DEFAULT 0);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor lerTodosOsDados() {
        String query = "SELECT * FROM " + TABLE_NAME;
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
        cv.put(COLUMN_NOME, nome);
        long resultado = db.insert(TABLE_NAME, null, cv);

        if (resultado == -1) {
            Toast.makeText(context, "DISGRAÇA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "BALA", Toast.LENGTH_SHORT).show();
        }
    }

    public void limparTabela() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    private void mockarDados() {
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

    public void resetarTestes() {
        limparTabela();
        mockarDados();
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static  String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnQuantidadeAlunos() {
        return COLUMN_QUANTIDADE_ALUNOS;
    }
}
