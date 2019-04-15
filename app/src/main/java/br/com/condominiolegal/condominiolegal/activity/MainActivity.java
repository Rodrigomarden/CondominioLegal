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

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Toolbar toolbar;

    private Button botaoCadastrarUsuario;
    private Button botaoCadastrarApartamento;
    private Button botaoCadastrarMorador;
    private Button botaoCadastrarCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        toolbar.setTitle("Condomínio Legal");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        botaoCadastrarUsuario = (Button) findViewById(R.id.bt_usuario);
        botaoCadastrarApartamento = (Button) findViewById(R.id.bt_apartamento);
        botaoCadastrarMorador = (Button) findViewById(R.id.bt_morador);
        botaoCadastrarCarro = (Button) findViewById(R.id.bt_carro);

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
}
