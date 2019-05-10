package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by rodri on 07/05/2019.
 */
public class ReservaEspacoAdapter extends BaseAdapter {
    private Context ctx;
    private String[] lista;

    public ReservaEspacoAdapter(Context context, String[] lista) {
        this.ctx = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public Object getItem(int i) {
        return lista[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView tv = new TextView((ctx));
        tv.setText(lista[i]);

        return tv;
    }
}
