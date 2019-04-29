package br.com.condominiolegal.condominiolegal.model;

/**
 * Created by rodri on 18/04/2019.
 */
public class Anexo extends Identificador {
    private String nome;
    private String url;
    private String caminho;

    public Anexo() {
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

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
