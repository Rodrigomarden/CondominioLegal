package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.sql.Time;
import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class Reserva extends Identificador {
    private String id;
    private Date data;
    private Time hora;
    private String idEspaco;
    private String nomeEspaco;
    private Float valor;
    private String idApartamento;
    private String numeroBlocoApartamento;

    public Reserva() {
    }

    @Exclude
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

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getIdEspaco() {
        return idEspaco;
    }

    public void setIdEspaco(String idEspaco) {
        this.idEspaco = idEspaco;
    }

    public String getNomeEspaco() {
        return nomeEspaco;
    }

    public void setNomeEspaco(String nomeEspaco) {
        this.nomeEspaco = nomeEspaco;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
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
