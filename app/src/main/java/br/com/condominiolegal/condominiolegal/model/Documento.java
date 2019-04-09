package br.com.condominiolegal.condominiolegal.model;

/**
 * Created by rodri on 02/04/2019.
 */
public class Documento {
    private String id;
    private String titulo;
    private String urlAnexo;

    public Documento() {
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

    public String getUrlAnexo() {
        return urlAnexo;
    }

    public void setUrlAnexo(String urlAnexo) {
        this.urlAnexo = urlAnexo;
    }
}
