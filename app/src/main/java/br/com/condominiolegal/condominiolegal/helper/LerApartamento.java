package br.com.condominiolegal.condominiolegal.helper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;

/**
 * Created by rodri on 10/04/2019.
 */
public class LerApartamento {
    private String id;
    private String numero;
    private String bloco;

    public LerApartamento() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public static String exibicaoApartamento(String bloco, String apartamento) {
        return "Bloco " + bloco + ", Apto. " + apartamento;
    }

    public static ArrayList<LerApartamento> listarApartamento(String idCondominio) {
        final ArrayList<LerApartamento> listaApartamento = new ArrayList<>();
        DatabaseReference firebase;
        firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos");

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaApartamento.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    LerApartamento apartamento = dados.getValue(LerApartamento.class);
                    listaApartamento.add(apartamento);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listaApartamento;
    }

}
