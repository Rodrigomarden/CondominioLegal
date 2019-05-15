package br.com.condominiolegal.condominiolegal.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by rodri on 02/04/2019.
 */
public class Correspondencia extends Identificador{
    private String id;
    private String data;
    private String status;
    private String tipo;

    public Correspondencia() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
