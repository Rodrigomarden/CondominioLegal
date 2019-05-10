package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

/**
 * Created by rodri on 24/04/2019.
 */
public class OpcaoEnquente {

    private String id;
    private String opcao;
    private int qntdVotos;

    public OpcaoEnquente() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public int getQntdVotos() {
        return qntdVotos;
    }

    public void setQntdVotos(int qntdVotos) {
        this.qntdVotos = qntdVotos;
    }
}
