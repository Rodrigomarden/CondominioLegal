package br.com.condominiolegal.condominiolegal.model;

/**
 * Created by rodri on 02/04/2019.
 */
public class Espaco {
    private String id;
    private String nome;
    private Float valor;

    public Espaco() {
    }

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
