package br.com.condominiolegal.condominiolegal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.LerApartamento;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Usuario;

/**
 * Created by rodri on 15/05/2019.
 */
public class EditarUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button botaoSalvar;
    private EditText nome;
    private EditText email;
    private EditText cpf;
    private EditText telefone;
    private EditText dataNascimento;
    private EditText apartamento;
    private RadioGroup radioGroup;
    private RadioButton radioButtonEscolhido;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private Usuario usuario;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_usuario);
        toolbar.setTitle("Editar Usuário");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        nome = (EditText) findViewById(R.id.edit_cadastro_usuario_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_usuario_email);
        cpf = (EditText) findViewById(R.id.edit_cadastro_usuario_cpf);
        telefone = (EditText) findViewById(R.id.edit_cadastro_usuario_telefone);
        dataNascimento = (EditText) findViewById(R.id.edit_cadastro_usuario_dataNascimento);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupId_cadastro_usuario);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_usuario_salvar);

        //Criar EditText para apartamento
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout_linear_cadastro_usuario);
        apartamento = new EditText(EditarUsuarioActivity.this);
        apartamento.setHint("Apartamento");
        apartamento.setLayoutParams(nome.getLayoutParams());

        //Recupera dados para editar
        if(getIntent().getSerializableExtra("usuario") != null) {

            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            nome.setText(usuario.getNome());
            email.setText(usuario.getEmail());
            cpf.setText(usuario.getCpf());
            telefone.setText(usuario.getTelefone());
            dataNascimento.setText(usuario.getDataNascimento());

            if(usuario.getPerfil().equals("Morador")) {
                apartamento.setText(LerApartamento.exibicaoApartamento(usuario.getBlocoApartamento(), usuario.getNumeroApartamento()));
            }

            nome.setKeyListener(null);
            email.setKeyListener(null);
            cpf.setKeyListener(null);
            dataNascimento.setKeyListener(null);

            if(usuario.getPerfil().equals("Síndico")) {
                radioButtonEscolhido = (RadioButton) findViewById(R.id.radioButton_cadastro_usuario_sindico);
                radioButtonEscolhido.setChecked(true);
            } else if(usuario.getPerfil().equals("Secretário")) {
                radioButtonEscolhido = (RadioButton) findViewById(R.id.radioButton_cadastro_usuario_secretario);
                radioButtonEscolhido.setChecked(true);
            } else if (usuario.getPerfil().equals("Morador")) {
                radioButtonEscolhido = (RadioButton) findViewById(R.id.radioButton_cadastro_usuario_morador);
                radioButtonEscolhido.setChecked(true);
                layout.addView(apartamento, 7);
            }

        }

        //Mascaras
        telefone.addTextChangedListener(Mask.maskTelefone(telefone));
        dataNascimento.addTextChangedListener(Mask.maskData(dataNascimento));
        cpf.addTextChangedListener(Mask.maskCpf(cpf));

        //Capturando RadioButton
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButtonEscolhido = (RadioButton) findViewById(i);
                if(radioButtonEscolhido.getText().equals("Morador")) {
                    layout.addView(apartamento, 7);
                } else {
                    layout.removeView(apartamento);
                }
            }
        });

        apartamento.setKeyListener(null);
        apartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolherApartamento();
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!nome.getText().toString().isEmpty() && !email.getText().toString().isEmpty() &&  !cpf.getText().toString().isEmpty()
                        && !telefone.getText().toString().isEmpty() && !dataNascimento.getText().toString().isEmpty() && !radioButtonEscolhido.getText().toString().isEmpty()) {

                    usuario.setNome(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setCpf(cpf.getText().toString());
                    usuario.setTelefone(telefone.getText().toString());
                    usuario.setDataNascimento(dataNascimento.getText().toString());
                    usuario.setPerfil(radioButtonEscolhido.getText().toString());
                    if(!radioButtonEscolhido.getText().equals("Morador")) {
                        usuario.setIdApartamento(null);
                        usuario.setBlocoApartamento(null);
                        usuario.setNumeroApartamento(null);
                    } else if(radioButtonEscolhido.getText().equals("Morador") && !apartamento.getText().toString().isEmpty()) {
                        usuario.setIdApartamento(idApartamento);
                        usuario.setNumeroApartamento(numeroApartamento);
                        usuario.setBlocoApartamento(blocoApartamento);
                    } else {
                        Toast.makeText(EditarUsuarioActivity.this, "Preencha o campo apartamento.", Toast.LENGTH_SHORT).show();
                    }

                    //Recupera informações para salvar
                    Preferencia preferencia = new Preferencia(EditarUsuarioActivity.this);
                    String idUsuario = preferencia.getId();
                    usuario.setIdUsuario(idUsuario);
                    usuario.setDataAlteracao(DateValidator.obterDataAtual());

                    //Verificação de datas
                    if(!DateValidator.validacaoData(usuario.getDataNascimento())) {
                        Toast.makeText(EditarUsuarioActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean retornoEdicao = editarUsuario();
                        if(retornoEdicao) {
                            Toast.makeText(EditarUsuarioActivity.this, "Edição realizada com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditarUsuarioActivity.this, "Problema ao realizar edição, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(EditarUsuarioActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void escolherApartamento() {
        Intent intent = new Intent(EditarUsuarioActivity.this, ListaApartamentoActivity.class);
        intent.putExtra("tipoCadastro", "Cadastro de Usuário");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Testar processo de retorno dos dados
        if(requestCode==1 && resultCode == RESULT_OK && data != null) {
            idApartamento = data.getStringExtra("idApartamento");
            numeroApartamento = data.getStringExtra("numeroApartamento");
            blocoApartamento = data.getStringExtra("blocoApartamento");

            apartamento.setText(LerApartamento.exibicaoApartamento(blocoApartamento, numeroApartamento));
        }
    }

    private boolean editarUsuario() {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(usuario.getId());
            firebase.setValue(usuario);
            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
