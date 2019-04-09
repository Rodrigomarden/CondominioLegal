package br.com.condominiolegal.condominiolegal.model;

/**
 * Created by rodri on 02/04/2019.
 */
public class ServicoProduto {
    private String id;
    private String titulo;
    private String descricao;
    private Float valor;
    private String idApartamento;
    private String numeroBlocoApartamento;

    public ServicoProduto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
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
