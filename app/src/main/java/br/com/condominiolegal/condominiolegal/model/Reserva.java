package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class Reserva extends Identificador implements Serializable{
    private String id;
    private String data;
    private String hora;
    private String idEspaco;
    private String nomeEspaco;
    private String valor;
    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    public Reserva() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIdApartamento() {
        return idApartamento;
    }

    public void setIdApartamento(String idApartamento) {
        this.idApartamento = idApartamento;
    }

    public String getNumeroApartamento() {
        return numeroApartamento;
    }

    public void setNumeroApartamento(String numeroApartamento) {
        this.numeroApartamento = numeroApartamento;
    }

    public String getBlocoApartamento() {
        return blocoApartamento;
    }

    public void setBlocoApartamento(String blocoApartamento) {
        this.blocoApartamento = blocoApartamento;
    }
}
