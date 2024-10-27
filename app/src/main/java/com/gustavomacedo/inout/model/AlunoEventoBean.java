package com.gustavomacedo.inout.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class AlunoEventoBean {
    @CsvBindByName(column = "aluno_id")
    private int idAluno;
    @CsvBindByName(column = "evento_id")
    private int idEvento;
    @CsvBindByName(column = "hora_de_entrada")
    @CsvDate("HH:mm:ss")
    private Date horaEntrada;

    public AlunoEventoBean(int idAluno, int idEvento) {
        this.idAluno = idAluno;
        this.idEvento = idEvento;
    }

    public AlunoEventoBean(int idAluno, int idEvento, Date horaEntrada) {
        this.idAluno = idAluno;
        this.idEvento = idEvento;
        this.horaEntrada = horaEntrada;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }
}
