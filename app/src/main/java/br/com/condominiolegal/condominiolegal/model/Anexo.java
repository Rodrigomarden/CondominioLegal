package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

/**
 * Created by rodri on 18/04/2019.
 */
public class Anexo extends Identificador {
    private String id;
    private String nome;
    private String url;
    private String tipo;
    private String caminho;

    public Anexo() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
