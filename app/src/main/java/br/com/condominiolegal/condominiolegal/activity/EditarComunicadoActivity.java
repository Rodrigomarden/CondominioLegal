package br.com.condominiolegal.condominiolegal.activity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.adapter.ListaArquivoDownloadAdapter;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Anexo;
import br.com.condominiolegal.condominiolegal.model.Comunicado;

/**
 * Created by rodri on 20/05/2019.
 */
public class EditarComunicadoActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private ListView listView;
    private Button botaoSalvar;
    private EditText titulo;
    private EditText descricao;
    private TextInputLayout tilDescricao;
    private EditText dataFim;
    private AlertDialog.Builder dialogError;

    private Comunicado comunicado;
    private ArrayList<Anexo> listaAnexos = new ArrayList<>();
    private ArrayAdapter adapter;

    private String idCondominio;

    private DatabaseReference firebase;

    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_comunicado);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_comunicado);
        toolbar.setTitle("Editar Comunicado");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        listView = (ListView) findViewById(R.id.lv_comunicado_arquivo);
        titulo = (EditText) findViewById(R.id.edit_cadastro_comunicado_titulo);
        tilDescricao = (TextInputLayout) findViewById(R.id.til_cadastro_comunicado_descricao);
        descricao = (EditText) findViewById(R.id.edit_cadastro_comunicado_descricao);
        dataFim = (EditText) findViewById(R.id.edit_cadastro_comunicado_dataFim);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_comunicado_salvar);

        //Definindo máximo de caracters para 8000 na descrição
        tilDescricao.setCounterEnabled(true);
        tilDescricao.setCounterMaxLength(8000);

        //Recupera dados para editar
        if(getIntent().getSerializableExtra("comunicado") != null) {
            comunicado = (Comunicado) getIntent().getSerializableExtra("comunicado");
            titulo.setText(comunicado.getTitulo());
            descricao.setText(comunicado.getDescricao());
            dataFim.setText(comunicado.getDataFim());

        }

        //Mascaras
        dataFim.addTextChangedListener(Mask.maskData(dataFim));

        //Recupera dados do usuário
        Preferencia preferencia = new Preferencia(EditarComunicadoActivity.this);
        idCondominio = preferencia.getIdCondominio();

        listaAnexos = new ArrayList<>();
        adapter = new ListaArquivoDownloadAdapter(this, listaAnexos);

        listView.setAdapter(adapter);

        //Recuperar arquivos
        Query query = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados").child(comunicado.getId()).child("anexos").orderByChild("nome");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaAnexos.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Anexo anexo = dados.getValue(Anexo.class);
                    anexo.setId(dados.getKey());
                    listaAnexos.add(anexo);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Fazer download do arquivo
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(listaAnexos.get(i).getUrl());

                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).mkdirs();

                downloadManager.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(listaAnexos.get(i).getNome())
                        .setDescription("Condominio Legal")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Teste"));

            }
        });

        //Botão Salvar
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!titulo.getText().toString().isEmpty() && !descricao.getText().toString().isEmpty() && !dataFim.getText().toString().isEmpty()) {
                    comunicado.setTitulo(titulo.getText().toString());
                    comunicado.setDescricao(descricao.getText().toString());
                    comunicado.setDataFim(dataFim.getText().toString());

                    //Verificação de datas
                    if(!DateValidator.validacaoData(comunicado.getDataFim())) {
                        Toast.makeText(EditarComunicadoActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
                    } else {

                        Boolean retornoCadastro = editarComunicado();

                        if (retornoCadastro) {
                            Toast.makeText(EditarComunicadoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditarComunicadoActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EditarComunicadoActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    private boolean editarComunicado() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(EditarComunicadoActivity.this);
            final String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            comunicado.setIdUsuario(idUsuario);
            comunicado.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados").child(comunicado.getId());
            firebase.setValue(comunicado);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
