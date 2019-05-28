package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by rodri on 02/04/2019.
 */
public class Espaco extends Identificador implements Serializable{
    private String id;
    private String nome;
    private Float valor;

    public Espaco() {
    }

    public Espaco(String id, String nome, Float valor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return nome;
    }
}
