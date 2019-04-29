package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class PessoasLivreAcesso extends Identificador {
    private String id;
    private String nome;
    private String dataNascimento;
    private String dataLimiteAcesso;
    private String cpf;

    public PessoasLivreAcesso() {
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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataLimiteAcesso() {
        return dataLimiteAcesso;
    }

    public void setDataLimiteAcesso(String dataLimiteAcesso) {
        this.dataLimiteAcesso = dataLimiteAcesso;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
