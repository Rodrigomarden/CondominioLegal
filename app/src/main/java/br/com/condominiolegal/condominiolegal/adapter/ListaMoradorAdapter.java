package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.Morador;

public class ListaMoradorAdapter extends ArrayAdapter<Morador> {
    private ArrayList<Morador> moradores;
    private Context context;

    public ListaMoradorAdapter(Context c, ArrayList<Morador> objects) {
        super(c, 0, objects);

        this.moradores = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!moradores.isEmpty() || moradores != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_morador, parent, false);

            //Recupera elemento para exibição
            TextView nome = (TextView) view.findViewById(R.id.tv_nome_adapter_lista_morador);

            nome.setText(moradores.get(position).getNome());

        }

        return view;
    }
}
