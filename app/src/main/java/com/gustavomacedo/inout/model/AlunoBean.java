package com.gustavomacedo.inout.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class AlunoBean {

    @CsvBindByName(column = "_id")
    private String id;
    @CsvBindByName(column = "rgm")
    private String rgm;
    @CsvBindByName(column = "nome")
    private String nome;
    @CsvBindByName(column = "hora_de_entrada")
    @CsvDate
    private String entrada;

    public AlunoBean(String id, String rgm, String nome, String entrada) {
        this.id = id;
        this.rgm = rgm;
        this.nome = nome;
        this.entrada = entrada;
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

    public String getEntrada() {
        return entrada;
    }

    @Override
    public String toString() {
        return "AlunoBean{" +
                "id='" + id + '\'' +
                ", rgm='" + rgm + '\'' +
                ", nome='" + nome + '\'' +
                ", entrada='" + entrada + '\'' +
                '}';
    }
}
