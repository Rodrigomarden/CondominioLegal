package br.com.condominiolegal.condominiolegal.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Correspondencia;

/**
 * Created by rodri on 10/05/2019.
 */
public class ListaCorrespondenciaAdapter extends ArrayAdapter<Correspondencia> {

    private ArrayList<Correspondencia> correspondencias;
    private Context context;
    private String idApartamento;
    private int posicao;
    private AlertDialog.Builder dialog;
    private DatabaseReference firebase;
    private String idCondominio;
    private String idUsuario;

    public ListaCorrespondenciaAdapter(Context c, ArrayList<Correspondencia> objects, String idApartamento) {
        super(c, 0, objects);

        this.correspondencias = objects;
        this.context = c;
        this.idApartamento = idApartamento;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View convertViewHolder = convertView;

        //Recupera o ID do condominio
        Preferencia preferencia = new Preferencia(getContext());
        idCondominio = preferencia.getIdCondominio();
        idUsuario = preferencia.getId();

        if(!correspondencias.isEmpty() || correspondencias != null) {

            if ((convertViewHolder == null) && (context != null)) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertViewHolder = layoutInflater.inflate(R.layout.adapter_lista_correspondencia, parent, false);
            }

            if ((convertViewHolder != null)) {

                //Recupera elemento para exibição
                TextView data = (TextView) convertViewHolder.findViewById(R.id.tv_data_lista_correspondencia);
                TextView tipo = (TextView) convertViewHolder.findViewById(R.id.tv_tipo_adapter_lista_correspondencia);
                TextView status = (TextView) convertViewHolder.findViewById(R.id.tv_status_adapter_lista_correspondencia);
                ImageButton check = (ImageButton) convertViewHolder.findViewById(R.id.bt_check_lista_correspondencia);

                data.setText(correspondencias.get(position).getData());
                tipo.setText(correspondencias.get(position).getTipo());
                status.setText(correspondencias.get(position).getStatus());

                check.setTag(position); //registra tag

                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();  //recupera tag
                        posicao = position;

                        //Criar alert dialog
                        dialog = new AlertDialog.Builder(getContext());

                        //Configurar o titulo
                        //dialog.setTitle("Confirmar");

                        //Configurar Mensagem
                        dialog.setMessage("Deseja realmente mudar o status para entregue?");

                        //Botao negativo
                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        //Botao positivo
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                correspondencias.get(posicao).setIdUsuario(idUsuario);
                                correspondencias.get(posicao).setDataInsercao(DateValidator.obterDataAtual());
                                correspondencias.get(posicao).setStatus("Entregue");

                                firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").child(idApartamento).child("correspondencias").child(correspondencias.get(posicao).getId());
                                firebase.setValue(correspondencias.get(posicao));
                            }
                        });

                        dialog.create();
                        dialog.show();
                    }
                });
            }

        }

        return convertViewHolder;
    }
}
