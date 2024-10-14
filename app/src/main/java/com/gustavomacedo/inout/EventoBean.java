package com.gustavomacedo.inout;

import com.opencsv.bean.CsvBindByName;

public class EventoBean {

    @CsvBindByName(column = "_id")
    private String id;
    @CsvBindByName(column = "nome")
    private String nome;
    @CsvBindByName(column = "qtd_alunos")
    private String qtdAlunos;

    public String getId() { return id; }

    public String getNome() { return nome; }

    public String getQtdAlunos() { return qtdAlunos; }
}
