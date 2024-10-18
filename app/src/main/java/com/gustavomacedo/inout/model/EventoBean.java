package com.gustavomacedo.inout.model;

import com.opencsv.bean.CsvBindByName;

public class EventoBean {

    @CsvBindByName(column = "_id")
    private String id;
    @CsvBindByName(column = "nome")
    private String nome;
    @CsvBindByName(column = "qtd_alunos")
    private String qtdAlunos;

    public EventoBean(String id, String nome, String qtdAlunos) {
        this.id = id;
        this.nome = nome;
        this.qtdAlunos = qtdAlunos;
    }
    public EventoBean() {}

    public String getId() { return id; }

    public String getNome() { return nome; }

    public String getQtdAlunos() { return qtdAlunos; }
}
