package br.com.condominiolegal.condominiolegal.model;

/**
 * Created by rodri on 02/04/2019.
 */
public class Carro {
    private String id;
    private String placa;
    private String modelo;
    private String marca;
    private String nomeDono;
    private String cpf;
    private String idApartamento;
    private String numeroBlocoApartamento;

    public Carro() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIdApartamento() {
        return idApartamento;
    }

    public void setIdApartamento(String idApartamento) {
        this.idApartamento = idApartamento;
    }

    public String getNumeroBlocoApartamento() {
        return numeroBlocoApartamento;
    }

    public void setNumeroBlocoApartamento(String numeroBlocoApartamento) {
        this.numeroBlocoApartamento = numeroBlocoApartamento;
    }
}
