package br.com.condominiolegal.condominiolegal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Documento;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Toolbar toolbar;

    private Button botaoCadastrarUsuario;
    private Button botaoCadastrarApartamento;
    private Button botaoCadastrarMorador;
    private Button botaoCadastrarCarro;
    private Button botaoCadastrarPessoaLivreAcesso;
    private Button botaoCadastrarAutorizacaoVagaGaragem;
    private Button botaoCadastrarCorrespondencia;
    private Button botaoCadastrarComunicado;
    private Button botaoCadastrarDocumento;
    private Button botaoCadastrarEnquete;
    private Button botaoCadastrarReclamacaoGeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        atualizarToken();

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        toolbar.setTitle("Condomínio Legal");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        botaoCadastrarUsuario = (Button) findViewById(R.id.bt_usuario);
        botaoCadastrarApartamento = (Button) findViewById(R.id.bt_apartamento);
        botaoCadastrarMorador = (Button) findViewById(R.id.bt_morador);
        botaoCadastrarCarro = (Button) findViewById(R.id.bt_carro);
        botaoCadastrarPessoaLivreAcesso = (Button) findViewById(R.id.bt_pessoaLivreAcesso);
        botaoCadastrarAutorizacaoVagaGaragem = (Button) findViewById(R.id.bt_autorizacaoVagaGaragem);
        botaoCadastrarCorrespondencia = (Button) findViewById(R.id.bt_correspondencia);
        botaoCadastrarComunicado = (Button) findViewById(R.id.bt_comunicado);
        botaoCadastrarDocumento = (Button) findViewById(R.id.bt_documento);
        botaoCadastrarEnquete = (Button) findViewById(R.id.bt_enquete);
        botaoCadastrarReclamacaoGeral = (Button) findViewById(R.id.bt_reclamacao_geral);


        botaoCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        });

        botaoCadastrarApartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroApartamentoActivity.class);
                startActivity(intent);
            }
        });

        botaoCadastrarMorador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaApartamentoActivity.class);
                intent.putExtra("tipoCadastro", "Cadastro de Morador");
                startActivity(intent);
            }
        });

        botaoCadastrarCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaApartamentoActivity.class);
                intent.putExtra("tipoCadastro", "Cadastro de Carro");
                startActivity(intent);
            }
        });

        botaoCadastrarPessoaLivreAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaApartamentoActivity.class);
                intent.putExtra("tipoCadastro", "Cadastro de Pessoa de Livre Acesso");
                startActivity(intent);
            }
        });

        botaoCadastrarAutorizacaoVagaGaragem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaApartamentoActivity.class);
                intent.putExtra("tipoCadastro", "Cadastro de Autorização Vaga de Garagem");
                startActivity(intent);
            }
        });

        botaoCadastrarComunicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroComunicadoActivity.class);
                startActivity(intent);
            }
        });

        botaoCadastrarCorrespondencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaApartamentoActivity.class);
                intent.putExtra("tipoCadastro", "Cadastro de Correspondência");
                startActivity(intent);
            }
        });

        botaoCadastrarDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroDocumentoActivity.class);
                startActivity(intent);
            }
        });

        botaoCadastrarEnquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroEnquenteActivity.class);
                startActivity(intent);
            }
        });

        botaoCadastrarReclamacaoGeral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroReclamacaoGeralActivity.class);
                startActivity(intent);
            }
        });


    }

    //Cria o menu baseado no XML
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //Capta os itens selecionados no Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configuracoes:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Deslogar usuário
    private void deslogarUsuario() {
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //Atualizar Token
    private void atualizarToken() {
        Preferencia preferencia = new Preferencia(MainActivity.this);
        String idUsuario = preferencia.getId();
        String token = preferencia.getToken();
        DatabaseReference atualizarUsuario = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idUsuario).child("token");
        atualizarUsuario.setValue(token);
    }
}
