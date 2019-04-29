package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class Enquete extends Identificador {
    private String id;
    private String titulo;
    private String dataEncerramento;
    private String tipo;

    public Enquete() {
    }

    @Exclude
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

    public String getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(String dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
