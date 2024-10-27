package com.gustavomacedo.inout.model;

import com.opencsv.bean.CsvBindByName;

public class EventoBean {

    @CsvBindByName(column = "_id")
    private String id;
    @CsvBindByName(column = "nome")
    private String nome;
    @CsvBindByName(column = "data_hora")
    private String dataHora;

    public EventoBean(String id, String nome, String dataHora) {
        this.id = id;
        this.nome = nome;
        this.dataHora = dataHora;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDataHora() {
        return dataHora;
    }
}
