package com.gustavomacedo.inout.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
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
    private static final String ALUNOS_EVENTOS_COLUMN_HORA_DE_ENTRADA = "hora_de_entrada"; // HH:mm:ss

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
        db.execSQL("DROP TABLE IF EXISTS " + ALUNOS_EVENTOS_TABLE_NAME);
        onCreate(db);
    }

    // CODIGOS PARA O ALUNO

    // função de adicionar aluno
    void addAluno(String rgm, String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ALUNOS_COLUMN_RGM, rgm);
        cv.put(ALUNOS_COLUMN_NOME, nome);
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

    public Cursor lerAlunosEmUmEvento(int idEvento) {
        String query = "SELECT "+ ALUNOS_TABLE_NAME +"."+ ALUNOS_COLUMN_RGM + ALUNOS_TABLE_NAME +"."+ ALUNOS_COLUMN_NOME +
                " FROM "+ ALUNOS_TABLE_NAME +
                " INNER JOIN "+ALUNOS_EVENTOS_TABLE_NAME+" ON "+ALUNOS_TABLE_NAME+"."+ALUNOS_COLUMN_ID+" = "+ALUNOS_EVENTOS_TABLE_NAME+"."+ALUNOS_EVENTOS_COLUMN_ALUNO_ID+
                " INNER JOIN "+EVENTOS_TABLE_NAME+" ON "+EVENTOS_TABLE_NAME+"."+EVENTOS_COLUMN_ID+" = "+ALUNOS_EVENTOS_TABLE_NAME+"."+ALUNOS_EVENTOS_COLUMN_EVENTO_ID+
                " WHERE "+EVENTOS_COLUMN_ID+" = ?;";

        // retorna, respectivamento, o RGM e nome de todos os alunos cadastrados em um evento determinado

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {String.valueOf(idEvento)});
        }

        return cursor;
    }

    public Cursor lerAlunoPorRGM(int rgm) {
        String query = "SELECT * FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_RGM + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {String.valueOf(rgm)});
        }

        return cursor;
    }

    public Cursor lerAlunoPorRGM(String rgm) {
        String query = "SELECT * FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_RGM + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {rgm});
        }

        return cursor;
    }

    public Integer lerIdAlunoPorRGM(String rgm) {
        String query = "SELECT "+ALUNOS_COLUMN_ID+" FROM " + ALUNOS_TABLE_NAME + " WHERE " + ALUNOS_COLUMN_RGM + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[] {rgm});
        }

        return cursor.getInt(0);
    }

    public boolean atualizarEntradaDoAlunoEmUmEvento(String rgm, String eventoId) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String alunoIdStr = String.valueOf(lerIdAlunoPorRGM(rgm));
        long now = new Date().getTime();
        String horaAtualStr = hourFormat.format(now);

        Cursor line = null;

        long result = 0;

        if (dbWrite != null) {
            cv.put(ALUNOS_EVENTOS_COLUMN_HORA_DE_ENTRADA, horaAtualStr);
            result = dbWrite.update(ALUNOS_EVENTOS_TABLE_NAME, cv, ALUNOS_EVENTOS_COLUMN_ALUNO_ID +
                    "=? and "+ ALUNOS_EVENTOS_COLUMN_EVENTO_ID +
                    "=? and " + ALUNOS_EVENTOS_COLUMN_HORA_DE_ENTRADA +
                    " IS NULL", new String[]{alunoIdStr, eventoId});
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
        this.addAluno(
                "12345678",
                "Teste 001");
        this.addAluno(
                "12345677",
                "Teste 002");
        this.addAluno(
                "12345676",
                "Teste 003");
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

    // @Param : int id
    public Cursor lerEventoPorId(int id) {
        String query = "SELECT * FROM " + EVENTOS_TABLE_NAME + " WHERE " + EVENTOS_COLUMN_ID + "= ?;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        }

        return cursor;
    }

    // @Param : String id
    public Cursor lerEventoPorId(String id) {
        String query = "SELECT * FROM " + EVENTOS_TABLE_NAME + " WHERE " + EVENTOS_COLUMN_ID + "= ?;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{id});
        }

        return cursor;
    }

    public void addEvento(String nome, Date dataHora) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dataHoraDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String dataHoraFormatada = dataHoraDf.format(dataHora);

        cv.put(EVENTOS_COLUMN_NOME, nome);
        cv.put(EVENTOS_COLUMN_DATA_HORA, dataHoraFormatada);
        long resultado = db.insert(EVENTOS_TABLE_NAME, null, cv);

        if (resultado == -1) {
            Toast.makeText(context, "Falha ao adicionar evento", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Evento adcionado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addEvento(String nome, String dataHora) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dataHoraDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String dataHoraFormatada = dataHoraDf.format(dataHora);

        cv.put(EVENTOS_COLUMN_NOME, nome);
        cv.put(EVENTOS_COLUMN_DATA_HORA, dataHoraFormatada);
        long resultado = db.insert(EVENTOS_TABLE_NAME, null, cv);

        if (resultado == -1) {
            Toast.makeText(context, "Falha ao adicionar evento", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Evento adcionado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void limparTabelaEventos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENTOS_TABLE_NAME, null, null);
    }

    private void mockarDadosEventos() {
        this.addEvento(
                "Teste 001",
                "2024-10-28 09:30:00"
        );
        this.addEvento(
                "Teste 002",
                "2024-10-28 10:00:00"
        );
    }

    public void resetarTestesEventos() {
        limparTabelaEventos();
        mockarDadosEventos();
    }
}
