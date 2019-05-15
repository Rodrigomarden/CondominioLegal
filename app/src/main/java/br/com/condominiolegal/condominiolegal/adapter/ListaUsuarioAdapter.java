package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.model.Usuario;

/**
 * Created by rodri on 13/05/2019.
 */
public class ListaUsuarioAdapter extends ArrayAdapter<Usuario> {
    private ArrayList<Usuario> usuarios;
    private Context context;

    public ListaUsuarioAdapter(Context c, ArrayList<Usuario> objects) {
        super(c, 0, objects);

        this.usuarios = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(!usuarios.isEmpty() || usuarios != null) {
            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.adapter_lista_usuario, parent, false);

            //Recupera elemento para exibição
            TextView nome = (TextView) view.findViewById(R.id.tv_nome_adapter_lista_usuario);
            TextView perfil = (TextView) view.findViewById(R.id.tv_perfil_adapter_lista_usuario);
            TextView email = (TextView) view.findViewById(R.id.tv_email_adapter_lista_usuario);

            nome.setText(usuarios.get(position).getNome());
            perfil.setText(usuarios.get(position).getPerfil());
            email.setText(usuarios.get(position).getEmail());

        }

        return view;
    }

}
