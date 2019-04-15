package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.Base64Custom;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button botaoSalvar;
    private Button botaoOculto;
    private EditText nome;
    private EditText email;
    private EditText cpf;
    private EditText telefone;
    private EditText dataNascimento;
    private EditText numeroApartamento;
    private EditText blocoApartamento;
    private RadioGroup radioGroup;
    private RadioButton radioButtonEscolhido;

    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_usuario);
        toolbar.setTitle("Cadastro de Usuário");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);


        nome = (EditText) findViewById(R.id.edit_cadastro_usuario_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_usuario_email);
        cpf = (EditText) findViewById(R.id.edit_cadastro_usuario_cpf);
        telefone = (EditText) findViewById(R.id.edit_cadastro_usuario_telefone);
        dataNascimento = (EditText) findViewById(R.id.edit_cadastro_usuario_dataNascimento);
        numeroApartamento = (EditText) findViewById(R.id.edit_cadastro_usuario_numeroApartamento);
        blocoApartamento = (EditText) findViewById(R.id.edit_cadastro_usuario_blocoApartamento);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupId_cadastro_usuario);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_usuario_salvar1);
        botaoOculto = (Button) findViewById(R.id.bt_cadastro_usuario_salvar2);

        //Mascaras
        telefone.addTextChangedListener(Mask.maskTelefone(telefone));
        dataNascimento.addTextChangedListener(Mask.maskData(dataNascimento));
        cpf.addTextChangedListener(Mask.maskCpf(cpf));

        //Visibilidade
        numeroApartamento.setVisibility(View.INVISIBLE);
        blocoApartamento.setVisibility(View.INVISIBLE);
        botaoOculto.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButtonEscolhido = (RadioButton) findViewById(i);
                if(radioButtonEscolhido.getText().equals("Morador")) {
                    numeroApartamento.setVisibility(View.VISIBLE);
                    blocoApartamento.setVisibility(View.VISIBLE);
                    botaoSalvar = (Button) findViewById(R.id.bt_cadastro_usuario_salvar2);
                    botaoOculto = (Button) findViewById(R.id.bt_cadastro_usuario_salvar1);
                    botaoSalvar.setVisibility(View.VISIBLE);
                    botaoOculto.setVisibility(View.INVISIBLE);
                } else {
                    numeroApartamento.setVisibility(View.INVISIBLE);
                    blocoApartamento.setVisibility(View.INVISIBLE);
                    botaoSalvar = (Button) findViewById(R.id.bt_cadastro_usuario_salvar1);
                    botaoOculto = (Button) findViewById(R.id.bt_cadastro_usuario_salvar2);
                    botaoSalvar.setVisibility(View.VISIBLE);
                    botaoOculto.setVisibility(View.INVISIBLE);
                }
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!nome.getText().toString().isEmpty() && !email.getText().toString().isEmpty() &&  !cpf.getText().toString().isEmpty()
                        && !telefone.getText().toString().isEmpty() && !dataNascimento.getText().toString().isEmpty() && !radioButtonEscolhido.getText().toString().isEmpty()) {
                    usuario = new Usuario();
                    usuario.setNome(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setCpf(cpf.getText().toString());
                    usuario.setTelefone(telefone.getText().toString());
                    usuario.setDataNascimento(dataNascimento.getText().toString());
                    usuario.setPerfil(radioButtonEscolhido.getText().toString());
                    if(radioButtonEscolhido.getText().equals("Morador")) {
                        usuario.setIdApartamento("Teste123");
                        usuario.setNumeroBlocoApartamento(numeroApartamento.getText().toString() + "_" + blocoApartamento.getText().toString());
                    }

                    Preferencia preferencia = new Preferencia(CadastroUsuarioActivity.this);
                    usuario.setIdCondominio(preferencia.getIdCondominio());
                    usuario.setNomeCondominio(preferencia.getNomeCondominio());

                    cadastrarUsuario();
                }
                else{
                    Toast.makeText(CadastroUsuarioActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), "123456").addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuario", Toast.LENGTH_SHORT).show();

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    finish();
                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está sendo usado!";
                    } catch (Exception e) {
                        erroExcecao = "erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " +erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
