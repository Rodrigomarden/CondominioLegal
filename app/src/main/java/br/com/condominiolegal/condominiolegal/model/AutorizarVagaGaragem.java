package br.com.condominiolegal.condominiolegal.model;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class AutorizarVagaGaragem {
    private String id;
    private String placaVeiculo;
    private String modelo;
    private String marca;
    private String idApartamento;
    private String numeroBlocoApartamento;
    private String numeroVagaEstacionamento;
    private Date dataInicio;
    private Date dataFim;

    public AutorizarVagaGaragem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getIdApartamento() {
        return idApartamento;
    }

    public void setIdApartamento(String idApartamento) {
        this.idApartamento = idApartamento;
    }

    public String getNumeroBlocoApartamento() {
        return numeroBlocoApartamento;
    }

    public void setNumeroBlocoApartamento(String numeroBlocoApartamento) {
        this.numeroBlocoApartamento = numeroBlocoApartamento;
    }

    public String getNumeroVagaEstacionamento() {
        return numeroVagaEstacionamento;
    }

    public void setNumeroVagaEstacionamento(String numeroVagaEstacionamento) {
        this.numeroVagaEstacionamento = numeroVagaEstacionamento;
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
}
