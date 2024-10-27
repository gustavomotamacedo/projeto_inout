package com.gustavomacedo.inout.model;

import androidx.annotation.NonNull;

import com.opencsv.bean.CsvBindByName;

public class AlunoBean {

    @CsvBindByName(column = "_id")
    private String id;
    @CsvBindByName(column = "rgm")
    private String rgm;
    @CsvBindByName(column = "nome")
    private String nome;

    public AlunoBean(String id, String rgm, String nome) {
        this.id = id;
        this.rgm = rgm;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getRgm() {
        return rgm;
    }

    public String getNome() {
        return nome;
    }

    @NonNull
    @Override
    public String toString() {
        return "AlunoBean{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", rgm='" + rgm + '\'' +
                '}';
    }
}
