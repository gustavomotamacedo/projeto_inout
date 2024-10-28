package com.gustavomacedo.inout.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class AlunoEventoBean {
    @CsvBindByName(column = "aluno_id")
    private String idAluno;
    @CsvBindByName(column = "evento_id")
    private String idEvento;
    @CsvBindByName(column = "hora_de_entrada")
    @CsvDate("HH:mm:ss")
    private String horaEntrada;

    public AlunoEventoBean(String idAluno, String idEvento) {
        this.idAluno = idAluno;
        this.idEvento = idEvento;
    }

    public AlunoEventoBean(String idAluno, String idEvento, String horaEntrada) {
        this.idAluno = idAluno;
        this.idEvento = idEvento;
        this.horaEntrada = horaEntrada;
    }

    public String getIdAluno() {
        return idAluno;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }
}
