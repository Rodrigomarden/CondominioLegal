package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Apartamento;

public class CadastroApartamentoActivity extends AppCompatActivity {

    public Toolbar toolbar;

    public Button botaoSalvar;
    private EditText numero;
    private EditText bloco;
    private EditText vagaEstacionamento;
    private EditText nomeProprietario;

    private Apartamento apartamento;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_apartamento);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_apartamento);
        toolbar.setTitle("Cadastro de Apartamento");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        numero = (EditText) findViewById(R.id.edit_cadastro_apartamento_numero);
        bloco = (EditText) findViewById(R.id.edit_cadastro_apartamento_bloco);
        vagaEstacionamento = (EditText) findViewById(R.id.edit_cadastro_apartamento_vaga_estaciomento);
        nomeProprietario = (EditText) findViewById(R.id.edit_cadastro_apartamento_nome_proprietario);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_apartamento_salvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!numero.getText().toString().isEmpty() && !bloco.getText().toString().isEmpty() && !vagaEstacionamento.getText().toString().isEmpty() && !nomeProprietario.getText().toString().isEmpty()) {
                    apartamento = new Apartamento();
                    apartamento.setNumero(Integer.parseInt(numero.getText().toString()));
                    apartamento.setBloco(bloco.getText().toString());
                    apartamento.setNumeroVagaEstacionamento(Integer.parseInt(vagaEstacionamento.getText().toString()));
                    apartamento.setNomeProprietario(nomeProprietario.getText().toString());
                    apartamento.setNumeroBloco(bloco.getText().toString()+" "+numero.getText().toString());

                    Boolean retornoCadastro = cadastrarApartamento();
                    if(retornoCadastro) {
                        Toast.makeText(CadastroApartamentoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CadastroApartamentoActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroApartamentoActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean cadastrarApartamento() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroApartamentoActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            apartamento.setIdUsuario(idUsuario);
            apartamento.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos");
            firebase.push()
                    .setValue(apartamento);
            finish();

            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
