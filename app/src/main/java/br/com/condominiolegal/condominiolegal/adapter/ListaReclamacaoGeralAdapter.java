package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.ReclamacaoGeral;

/**
 * Created by rodri on 20/05/2019.
 */
public class ListaReclamacaoGeralAdapter extends ArrayAdapter<ReclamacaoGeral> {
    private ArrayList<ReclamacaoGeral> reclamacaoGerais;
    private Context context;

    public ListaReclamacaoGeralAdapter(Context c, ArrayList<ReclamacaoGeral> objects) {
        super(c, 0, objects);

        this.reclamacaoGerais = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!reclamacaoGerais.isEmpty() || reclamacaoGerais != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_comunicado, parent, false);

            //Recupera elemento para exibição
            TextView titulo = (TextView) view.findViewById(R.id.tv_titulo_adapter_lista_comunicado);
            TextView data = (TextView) view.findViewById(R.id.tv_data_adapter_lista_comunicado);

            titulo.setText(reclamacaoGerais.get(position).getTitulo());
            data.setText(reclamacaoGerais.get(position).getDataInsercao());

        }

        return view;
    }
}
