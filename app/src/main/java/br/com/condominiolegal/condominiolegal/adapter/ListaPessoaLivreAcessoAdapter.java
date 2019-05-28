package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.PessoasLivreAcesso;

/**
 * Created by rodri on 16/05/2019.
 */
public class ListaPessoaLivreAcessoAdapter extends ArrayAdapter<PessoasLivreAcesso> {
    private ArrayList<PessoasLivreAcesso> pessoasLivreAcessos;
    private Context context;

    public ListaPessoaLivreAcessoAdapter(Context c, ArrayList<PessoasLivreAcesso> objects) {
        super(c, 0, objects);

        this.pessoasLivreAcessos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!pessoasLivreAcessos.isEmpty() || pessoasLivreAcessos != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_pessoa_livre_acesso, parent, false);

            //Recupera elemento para exibição
            TextView nome = (TextView) view.findViewById(R.id.tv_nome_adapter_lista_pessoa_livre_acesso);

            nome.setText(pessoasLivreAcessos.get(position).getNome());

        }

        return view;
    }
}
