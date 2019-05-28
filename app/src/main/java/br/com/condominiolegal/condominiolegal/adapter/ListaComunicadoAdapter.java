package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.Comunicado;

/**
 * Created by rodri on 20/05/2019.
 */
public class ListaComunicadoAdapter extends ArrayAdapter<Comunicado> {
    private ArrayList<Comunicado> comunicados;
    private Context context;

    public ListaComunicadoAdapter(Context c, ArrayList<Comunicado> objects) {
        super(c, 0, objects);

        this.comunicados = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!comunicados.isEmpty() || comunicados != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_comunicado, parent, false);

            //Recupera elemento para exibição
            TextView titulo = (TextView) view.findViewById(R.id.tv_titulo_adapter_lista_comunicado);
            TextView data = (TextView) view.findViewById(R.id.tv_data_adapter_lista_comunicado);

            titulo.setText(comunicados.get(position).getTitulo());
            data.setText(comunicados.get(position).getDataInsercao());

        }

        return view;
    }
}
