package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

/**
 * Created by rodri on 02/04/2019.
 */
public class Fornecedor extends Identificador {
    private String id;
    private String nome;
    private String descricao;
    private String tipoServico;
    private String urlImagem;

    public Fornecedor() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
