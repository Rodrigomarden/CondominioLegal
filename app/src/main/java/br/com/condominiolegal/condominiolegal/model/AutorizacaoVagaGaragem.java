package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class AutorizacaoVagaGaragem extends Identificador {
    private String id;
    private String placa;
    private String modelo;
    private String marca;
    private String idApartamento;
    private String dataLimiteAcesso;

    public AutorizacaoVagaGaragem() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placaVeiculo) {
        this.placa = placa;
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

    public String getDataLimiteAcesso() {
        return dataLimiteAcesso;
    }

    public void setDataLimiteAcesso(String dataLimiteAcesso) {
        this.dataLimiteAcesso = dataLimiteAcesso;
    }

}
