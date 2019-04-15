package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.helper.LerApartamento;

/**
 * Created by rodri on 10/04/2019.
 */
public class ApartamentoAdapter extends ArrayAdapter<LerApartamento> {

    private ArrayList<LerApartamento> lerApartamentos;
    private Context context;

    public ApartamentoAdapter(Context c, ArrayList<LerApartamento> objects) {
        super(c, 0, objects);

        this.lerApartamentos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!lerApartamentos.isEmpty() || lerApartamentos != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_apartamento, parent, false);

            //Recupera elemento para exibição
            TextView blocoApartamento = (TextView) view.findViewById(R.id.tv_lista_apartamento);

            blocoApartamento.setText("Bloco " + lerApartamentos.get(position).getBloco() + " Apto: " + lerApartamentos.get(position).getNumero());
        }

        return view;
    }

}
