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
import br.com.condominiolegal.condominiolegal.model.Reserva;

/**
 * Created by rodri on 20/05/2019.
 */
public class ListaReservaAdapter extends ArrayAdapter<Reserva> {
    private ArrayList<Reserva> reservas;
    private Context context;

    public ListaReservaAdapter(Context c, ArrayList<Reserva> objects) {
        super(c, 0, objects);

        this.reservas = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!reservas.isEmpty() || reservas != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_reserva, parent, false);

            //Recupera elemento para exibição
            TextView espaco = (TextView) view.findViewById(R.id.tv_espaco_adapter_lista_reserva);
            TextView data = (TextView) view.findViewById(R.id.tv_data_adapter_lista_reserva);
            TextView apartamento = (TextView) view.findViewById(R.id.tv_apartamento_adapter_lista_reserva);

            espaco.setText(reservas.get(position).getNomeEspaco());
            data.setText(reservas.get(position).getDataInsercao());
            apartamento.setText(LerApartamento.exibicaoApartamento(reservas.get(position).getBlocoApartamento(), reservas.get(position).getNumeroApartamento()));

        }

        return view;
    }
}
