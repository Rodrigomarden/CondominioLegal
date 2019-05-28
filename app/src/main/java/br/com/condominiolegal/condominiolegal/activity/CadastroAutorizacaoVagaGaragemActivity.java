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
import br.com.condominiolegal.condominiolegal.helper.LerApartamento;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.AutorizacaoVagaGaragem;
import br.com.condominiolegal.condominiolegal.model.Carro;

public class CadastroAutorizacaoVagaGaragemActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private EditText placa;
    private EditText modelo;
    private EditText marca;
    private EditText dataLimiteAcesso;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private AutorizacaoVagaGaragem autorizacaoVagaGaragem;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_autorizacao_vaga_garagem);

        //Recuperar os dados do Contato para conversa pela intent
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            idApartamento = extra.getString("idApartamento");
            numeroApartamento = extra.getString("numeroApartamento");
            blocoApartamento = extra.getString("blocoApartamento");
        }

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_autorizacao_vaga_garagem);
        toolbar.setTitle("Cadastro de Autorização Vaga de Garagem");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_autorizacao_vaga_garagem_blocoNumeroApartamento);
        placa = (EditText) findViewById(R.id.edit_cadastro_autorizacao_vaga_garagem_placa);
        modelo = (EditText) findViewById(R.id.edit_cadastro_autorizacao_vaga_garagem_modelo);
        marca = (EditText) findViewById(R.id.edit_cadastro_autorizacao_vaga_garagem_marca);
        dataLimiteAcesso = (EditText) findViewById(R.id.edit_cadastro_autorizacao_vaga_garagem_data_limite_acesso);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_autorizacao_vaga_garagem_salvar);

        //Mascaras
        placa.addTextChangedListener(Mask.maskPlaca(placa));
        dataLimiteAcesso.addTextChangedListener(Mask.maskData(dataLimiteAcesso));

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText(LerApartamento.exibicaoApartamento(blocoApartamento, numeroApartamento));

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!placa.getText().toString().isEmpty() && !modelo.getText().toString().isEmpty() && !marca.getText().toString().isEmpty() && !dataLimiteAcesso.getText().toString().isEmpty()) {
                    autorizacaoVagaGaragem = new AutorizacaoVagaGaragem();
                    autorizacaoVagaGaragem.setPlaca(placa.getText().toString().toUpperCase());
                    autorizacaoVagaGaragem.setModelo(modelo.getText().toString());
                    autorizacaoVagaGaragem.setMarca(marca.getText().toString());
                    autorizacaoVagaGaragem.setDataLimiteAcesso(dataLimiteAcesso.getText().toString());

                    //Verificação de datas
                    if(!DateValidator.validacaoData(autorizacaoVagaGaragem.getDataLimiteAcesso())) {
                        Toast.makeText(CadastroAutorizacaoVagaGaragemActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
                    } else if(!DateValidator.validacaoDataHoje(autorizacaoVagaGaragem.getDataLimiteAcesso())) {
                        Toast.makeText(CadastroAutorizacaoVagaGaragemActivity.this, "A data limite não pode ser anterior a data atual!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean retornoCadastro = cadastrarAutorizacaoVagaGaragem();

                        if(retornoCadastro) {
                            Toast.makeText(CadastroAutorizacaoVagaGaragemActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CadastroAutorizacaoVagaGaragemActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }



                } else {
                    Toast.makeText(CadastroAutorizacaoVagaGaragemActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean cadastrarAutorizacaoVagaGaragem() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroAutorizacaoVagaGaragemActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            autorizacaoVagaGaragem.setIdUsuario(idUsuario);
            autorizacaoVagaGaragem.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").child(idApartamento).child("autorizacaoVagaGaragem");
            firebase.push()
                    .setValue(autorizacaoVagaGaragem);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
