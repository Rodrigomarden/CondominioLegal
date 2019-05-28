package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.Carro;

/**
 * Created by rodri on 16/05/2019.
 */
public class ListaCarroAdapter extends ArrayAdapter<Carro> {
    private ArrayList<Carro> carros;
    private Context context;

    public ListaCarroAdapter(Context c, ArrayList<Carro> objects) {
        super(c, 0, objects);

        this.carros = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!carros.isEmpty() || carros != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_carro, parent, false);

            //Recupera elemento para exibição
            TextView marca = (TextView) view.findViewById(R.id.tv_marca_adapter_lista_carro);
            TextView modelo = (TextView) view.findViewById(R.id.tv_modelo_adapter_lista_carro);
            TextView placa = (TextView) view.findViewById(R.id.tv_placa_adapter_lista_carro);

            marca.setText(carros.get(position).getMarca());
            modelo.setText(carros.get(position).getModelo());
            placa.setText(carros.get(position).getPlaca());

        }

        return view;
    }


}
