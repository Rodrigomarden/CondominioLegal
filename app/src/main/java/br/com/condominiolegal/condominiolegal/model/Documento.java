package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by rodri on 02/04/2019.
 */
public class Documento extends Identificador implements Serializable{
    private String id;
    private String titulo;

    public Documento() {
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

}
