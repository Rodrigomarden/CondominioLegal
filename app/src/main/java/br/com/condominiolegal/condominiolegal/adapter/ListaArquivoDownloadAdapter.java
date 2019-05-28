package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.Anexo;

/**
 * Created by rodri on 21/05/2019.
 */
public class ListaArquivoDownloadAdapter extends ArrayAdapter<Anexo> {
    private ArrayList<Anexo> anexos;
    private Context context;

    public ListaArquivoDownloadAdapter(Context c, ArrayList<Anexo> objects) {
        super(c, 0, objects);

        this.anexos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!anexos.isEmpty() || anexos != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_arquivo_download, parent, false);

            //Recupera elemento para exibição
            TextView arquivo = (TextView) view.findViewById(R.id.tv_lista_arquivo_download);

            arquivo.setText(anexos.get(position).getNome());

        }

        return view;
    }
}
