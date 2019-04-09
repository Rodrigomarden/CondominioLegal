package br.com.condominiolegal.condominiolegal.model;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class Correspondencia {
    private String id;
    private Date data;
    private String status;
    private String tipo;
    private String idApartamento;
    private String numeroBlocoApartamento;

    public Correspondencia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
}
