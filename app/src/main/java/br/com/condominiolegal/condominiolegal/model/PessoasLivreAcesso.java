package br.com.condominiolegal.condominiolegal.model;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class PessoasLivreAcesso {
    private String id;
    private String nome;
    private Date dataNascimento;
    private String idApartamento;
    private String numerBlocoApartamento;
    private Date dataLimiteAcesso;

    public PessoasLivreAcesso() {
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getIdApartamento() {
        return idApartamento;
    }

    public void setIdApartamento(String idApartamento) {
        this.idApartamento = idApartamento;
    }

    public String getNumerBlocoApartamento() {
        return numerBlocoApartamento;
    }

    public void setNumerBlocoApartamento(String numerBlocoApartamento) {
        this.numerBlocoApartamento = numerBlocoApartamento;
    }

    public Date getDataLimiteAcesso() {
        return dataLimiteAcesso;
    }

    public void setDataLimiteAcesso(Date dataLimiteAcesso) {
        this.dataLimiteAcesso = dataLimiteAcesso;
    }
}
