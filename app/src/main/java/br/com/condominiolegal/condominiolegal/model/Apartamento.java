package br.com.condominiolegal.condominiolegal.model;

/**
 * Created by rodri on 02/04/2019.
 */
public class Apartamento {
    private String id;
    private int numero;
    private String bloco;
    private String numeroBloco;
    private int numeroVagaEstacionamento;
    private String nomeProprietario;

    public Apartamento() {
    }

    public Apartamento(String id, int numero, String bloco, String numeroBloco, int numeroVagaEstacionamento, String nomeProprietario) {
        this.id = id;
        this.numero = numero;
        this.bloco = bloco;
        this.numeroBloco = numeroBloco;
        this.numeroVagaEstacionamento = numeroVagaEstacionamento;
        this.nomeProprietario = nomeProprietario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getNumeroBloco() {
        return numeroBloco;
    }

    public void setNumeroBloco(String numeroBloco) {
        this.numeroBloco = numeroBloco;
    }

    public int getNumeroVagaEstacionamento() {
        return numeroVagaEstacionamento;
    }

    public void setNumeroVagaEstacionamento(int numeroVagaEstacionamento) {
        this.numeroVagaEstacionamento = numeroVagaEstacionamento;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }
}
