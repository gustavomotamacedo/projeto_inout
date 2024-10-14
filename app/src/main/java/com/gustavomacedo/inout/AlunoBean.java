package com.gustavomacedo.inout;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class AlunoBean {

    @CsvBindByName(column = "_id")
    private String id;
    @CsvBindByName(column = "nome")
    private String nome;
    @CsvBindByName(column = "rgm")
    private String rgm;
    @CsvBindByName(column = "id_evento")
    private String idEvento;
    @CsvBindByName(column = "data")
    @CsvDate("yyyy-MM-dd")
    private Date data;
    @CsvBindByName(column = "hr_entrada")
    @CsvDate("hh:mm:ss")
    private Date entrada;
    @CsvBindByName(column = "hr_saida")
    @CsvDate("hh:mm:ss")
    private Date saida;
    @CsvBindByName(column = "hr_permanencia")
    @CsvDate("hh:mm:ss")
    private Date permanencia;

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRgm() {
        return rgm;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public Date getData() {
        return data;
    }

    public Date getEntrada() {
        return entrada;
    }

    public Date getSaida() {
        return saida;
    }

    public Date getPermanencia() {
        return permanencia;
    }
}
