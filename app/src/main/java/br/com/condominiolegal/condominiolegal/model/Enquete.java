package br.com.condominiolegal.condominiolegal.model;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class Enquete {
    private String id;
    private String titulo;
    private Date dataInicio;
    private Date dataFim;
    private String idOpcao;
    private String nomeOpcao;
    private String tipo;

    public Enquete() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getIdOpcao() {
        return idOpcao;
    }

    public void setIdOpcao(String idOpcao) {
        this.idOpcao = idOpcao;
    }

    public String getNomeOpcao() {
        return nomeOpcao;
    }

    public void setNomeOpcao(String nomeOpcao) {
        this.nomeOpcao = nomeOpcao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
