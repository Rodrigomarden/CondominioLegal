package br.com.condominiolegal.condominiolegal.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.Base64Custom;
import br.com.condominiolegal.condominiolegal.helper.Permissao;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebase;
    private DatabaseReference atualizarUsuario;

    private String identificadorUsuarioLogado;

    private String[] permissoesNecessarias = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_DOCUMENTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1, this, permissoesNecessarias);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_logar);
        //botaoLogar.setVisibility(View.INVISIBLE);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!email.getText().toString().isEmpty() && !senha.getText().toString().isEmpty()) {
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    validarLogin();
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                            firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificadorUsuarioLogado);
                            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                                    Preferencia preferencias = new Preferencia(LoginActivity.this);

                                    if(usuarioRecuperado.getPerfil().equals("Morador")) {
                                        preferencias.salvarDadosMorador(identificadorUsuarioLogado, usuarioRecuperado.getNome(), usuarioRecuperado.getPerfil(), usuarioRecuperado.getIdCondominio(), usuarioRecuperado.getNomeCondominio(), usuarioRecuperado.getIdApartamento(), usuarioRecuperado.getNumeroApartamento(), usuarioRecuperado.getBlocoApartamento());
                                    } else {
                                        preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome(), usuarioRecuperado.getPerfil(), usuarioRecuperado.getIdCondominio(), usuarioRecuperado.getNomeCondominio());
                                    }

                                    abrirTelaPrincipal();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "E-mail ou senha incorreta!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    public void abrirCadastroUsuario(View view) {
//
//        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
//        startActivity(intent);
//
//    }
}
