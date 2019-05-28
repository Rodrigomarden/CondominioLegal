package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.Espaco;

/**
 * Created by rodri on 20/05/2019.
 */
public class ListaEspacoAdapter extends ArrayAdapter<Espaco> {
    private ArrayList<Espaco> espacos;
    private Context context;

    public ListaEspacoAdapter(Context c, ArrayList<Espaco> objects) {
        super(c, 0, objects);

        this.espacos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!espacos.isEmpty() || espacos != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_espaco, parent, false);

            //Recupera elemento para exibição
            TextView nome = (TextView) view.findViewById(R.id.tv_nome_adapter_lista_espaco);
            TextView valor = (TextView) view.findViewById(R.id.tv_valor_adapter_lista_espaco);

            nome.setText(espacos.get(position).getNome());
            valor.setText("R$ " + espacos.get(position).getValor().toString());

        }

        return view;
    }
}
