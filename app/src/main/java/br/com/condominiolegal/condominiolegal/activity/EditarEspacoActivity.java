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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.adapter.ListaArquivoDownloadAdapter;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Anexo;
import br.com.condominiolegal.condominiolegal.model.Espaco;

/**
 * Created by rodri on 20/05/2019.
 */
public class EditarEspacoActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private EditText nome;
    private EditText valor;

    private Espaco espaco;
    private ArrayList<Anexo> listaAnexos = new ArrayList<>();
    private ArrayAdapter adapter;

    private String idCondominio;

    private DatabaseReference firebase;

    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_espaco);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_espaco);
        toolbar.setTitle("Editar Espaco");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        nome = (EditText) findViewById(R.id.edit_cadastro_espaco_nome);
        valor = (EditText) findViewById(R.id.edit_cadastro_espaco_valor);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_espaco_salvar);

        //Recupera dados para editar
        if(getIntent().getSerializableExtra("espaco") != null) {
            espaco = (Espaco) getIntent().getSerializableExtra("espaco");
            nome.setText(espaco.getNome());
            valor.setText("R$ " + espaco.getValor().toString());

        }

        //Botão Salvar
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!nome.getText().toString().isEmpty() && !valor.getText().toString().isEmpty()) {
                    espaco.setNome(nome.getText().toString());
                    espaco.setValor(Float.parseFloat(valor.getText().toString().replace("R$ ", "")));

                    Boolean retornoCadastro = editarEspaco();

                    if (retornoCadastro) {
                        Toast.makeText(EditarEspacoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarEspacoActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditarEspacoActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    private boolean editarEspaco() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(EditarEspacoActivity.this);
            final String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            espaco.setIdUsuario(idUsuario);
            espaco.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("espacos").child(espaco.getId());
            firebase.setValue(espaco);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
