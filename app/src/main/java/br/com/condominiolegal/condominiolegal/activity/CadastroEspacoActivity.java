package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Espaco;

public class CadastroEspacoActivity extends AppCompatActivity {


    public Toolbar toolbar;

    private Button botaoSalvar;
    private EditText nome;
    private EditText valor;

    private Espaco espaco;

    private DatabaseReference firebase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_espaco);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_espaco);
        toolbar.setTitle("Cadastro de Espaco");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        nome = (EditText) findViewById(R.id.edit_cadastro_espaco_nome);
        valor = (EditText) findViewById(R.id.edit_cadastro_espaco_valor);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_espaco_salvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nome.getText().toString().isEmpty() && !valor.getText().toString().isEmpty()) {
                    espaco = new Espaco();
                    espaco.setNome(nome.getText().toString().toUpperCase());
                    espaco.setValor(Float.parseFloat(valor.getText().toString()));

                    Boolean retornoCadastro = cadastrarEspaco();
                    if(retornoCadastro) {
                        Toast.makeText(CadastroEspacoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CadastroEspacoActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroEspacoActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean cadastrarEspaco() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroEspacoActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            espaco.setIdUsuario(idUsuario);
            espaco.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("espacos");
            firebase.push()
                    .setValue(espaco);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
