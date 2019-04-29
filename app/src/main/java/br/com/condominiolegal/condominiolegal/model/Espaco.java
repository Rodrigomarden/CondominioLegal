package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

/**
 * Created by rodri on 02/04/2019.
 */
public class Espaco extends Identificador {
    private String id;
    private String nome;
    private Float valor;

    public Espaco() {
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
}
