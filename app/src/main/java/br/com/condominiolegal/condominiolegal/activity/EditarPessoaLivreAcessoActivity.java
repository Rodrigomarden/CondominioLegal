package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import br.com.condominiolegal.condominiolegal.model.Morador;
import br.com.condominiolegal.condominiolegal.model.PessoasLivreAcesso;

/**
 * Created by rodri on 16/05/2019.
 */
public class EditarPessoaLivreAcessoActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private EditText nome;
    private EditText cpf;
    private EditText dataNascimento;
    private EditText dataLimiteAcesso;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private PessoasLivreAcesso pessoasLivreAcesso;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoa_livre_acesso);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_pessoa_livre_acesso);
        toolbar.setTitle("Editar Pessoa de Livre acesso");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_pessoa_livre_acesso_blocoNumeroApartamento);
        nome = (EditText) findViewById(R.id.edit_cadastro_pessoa_livre_acesso_nome);
        cpf = (EditText) findViewById(R.id.edit_cadastro_pessoa_livre_acesso_cpf);
        dataNascimento = (EditText) findViewById(R.id.edit_cadastro_pessoa_livre_acesso_data_nascimento);
        dataLimiteAcesso = (EditText) findViewById(R.id.edit_cadastro_pessoa_livre_acesso_data_limite_acesso);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_pessoa_livre_acesso_salvar);

        //Recupera dados para editar
        if(getIntent().getSerializableExtra("pessoaLivreAcesso") != null) {
            pessoasLivreAcesso = (PessoasLivreAcesso) getIntent().getSerializableExtra("pessoaLivreAcesso");
            nome.setText(pessoasLivreAcesso.getNome());
            cpf.setText(pessoasLivreAcesso.getCpf());
            dataNascimento.setText(pessoasLivreAcesso.getDataNascimento());
            dataLimiteAcesso.setText(pessoasLivreAcesso.getDataLimiteAcesso());

            //Recuperar os dados do Apartamento para conversa pela intent
            Bundle extra = getIntent().getExtras();
            if(extra != null) {
                idApartamento = extra.getString("idApartamento");
                numeroApartamento = extra.getString("numeroApartamento");
                blocoApartamento = extra.getString("blocoApartamento");
            }
        }

        //Mascaras
        dataNascimento.addTextChangedListener(Mask.maskData(dataNascimento));
        dataLimiteAcesso.addTextChangedListener(Mask.maskData(dataLimiteAcesso));
        cpf.addTextChangedListener(Mask.maskCpf(cpf));

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText(LerApartamento.exibicaoApartamento(blocoApartamento, numeroApartamento));

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nome.getText().toString().isEmpty() && !cpf.getText().toString().isEmpty() && !dataNascimento.getText().toString().isEmpty() && !dataLimiteAcesso.getText().toString().isEmpty()) {
                    pessoasLivreAcesso.setNome(nome.getText().toString());
                    pessoasLivreAcesso.setCpf(cpf.getText().toString());
                    pessoasLivreAcesso.setDataNascimento(dataNascimento.getText().toString());
                    pessoasLivreAcesso.setDataLimiteAcesso(dataLimiteAcesso.getText().toString());

                    //Verificação de datas
                    if(!DateValidator.validacaoData(pessoasLivreAcesso.getDataNascimento()) || !DateValidator.validacaoData(pessoasLivreAcesso.getDataLimiteAcesso())) {
                        Toast.makeText(EditarPessoaLivreAcessoActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
                    } else if(!DateValidator.validacaoDataHoje(pessoasLivreAcesso.getDataLimiteAcesso())) {
                        Toast.makeText(EditarPessoaLivreAcessoActivity.this, "A data limite não pode ser anterior a data atual!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean retornoCadastro = editarPessoaLivreAcesso();

                        if (retornoCadastro) {
                            Toast.makeText(EditarPessoaLivreAcessoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditarPessoaLivreAcessoActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EditarPessoaLivreAcessoActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean editarPessoaLivreAcesso() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(EditarPessoaLivreAcessoActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            pessoasLivreAcesso.setIdUsuario(idUsuario);
            pessoasLivreAcesso.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").child(idApartamento).child("pessoaLivreAcesso").child(pessoasLivreAcesso.getId());
            firebase.setValue(pessoasLivreAcesso);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
