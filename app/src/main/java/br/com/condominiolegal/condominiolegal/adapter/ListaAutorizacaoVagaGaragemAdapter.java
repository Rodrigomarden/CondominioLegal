package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.AutorizacaoVagaGaragem;

/**
 * Created by rodri on 16/05/2019.
 */
public class ListaAutorizacaoVagaGaragemAdapter extends ArrayAdapter<AutorizacaoVagaGaragem> {
    private ArrayList<AutorizacaoVagaGaragem> autorizacaoVagaGaragems;
    private Context context;

    public ListaAutorizacaoVagaGaragemAdapter(Context c, ArrayList<AutorizacaoVagaGaragem> objects) {
        super(c, 0, objects);

        this.autorizacaoVagaGaragems = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!autorizacaoVagaGaragems.isEmpty() || autorizacaoVagaGaragems != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_autorizacao_vaga_garagem, parent, false);

            //Recupera elemento para exibição
            TextView marca = (TextView) view.findViewById(R.id.tv_marca_adapter_lista_autorizacao_vaga_garagem);
            TextView modelo = (TextView) view.findViewById(R.id.tv_titulo_adapter_lista_comunicado);
            TextView placa = (TextView) view.findViewById(R.id.tv_placa_adapter_lista_autorizacao_vaga_garagem);
            TextView dataLimite = (TextView) view.findViewById(R.id.tv_data_limite_adapter_lista_autorizacao_vaga_garagem);

            marca.setText(autorizacaoVagaGaragems.get(position).getMarca());
            modelo.setText(autorizacaoVagaGaragems.get(position).getModelo());
            placa.setText(autorizacaoVagaGaragems.get(position).getPlaca());
            dataLimite.setText(autorizacaoVagaGaragems.get(position).getDataLimiteAcesso());

        }

        return view;
    }
}
