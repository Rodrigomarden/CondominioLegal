package br.com.condominiolegal.condominiolegal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.adapter.ApartamentoAdapter;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.LerApartamento;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;

public class ListaApartamentoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<LerApartamento> listaApartamentos;
    private TextView inf;

    private DatabaseReference firebase;

    private String tipoCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_apartamento);

        //Recuperar os dados do Contato para conversa pela intent
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            tipoCadastro = extra.getString("tipoCadastro");
        }

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_lista_apartamento);
        toolbar.setTitle("Escolha o apartamento");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        inf = (TextView) findViewById(R.id.tv_lista_apartamento_inf);

        listaApartamentos = new ArrayList<>();

        listView = (ListView) findViewById(R.id.lv_apartamento);
        adapter = new ApartamentoAdapter(this, listaApartamentos);

        listView.setAdapter(adapter);

        //Recuperar apartamentos do firebase
        Preferencia preferencia = new Preferencia(ListaApartamentoActivity.this);
        String idCondominio = preferencia.getIdCondominio();

        Query query = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").orderByChild("numeroBloco");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaApartamentos.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    LerApartamento apartamento = dados.getValue(LerApartamento.class);
                    apartamento.setId(dados.getKey());
                    listaApartamentos.add(apartamento);
                }

                if(!listaApartamentos.isEmpty()) {
                    inf.setText("");
                } else {
                    inf.setText("Não há itens cadastrados.");
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent();
                intent.putExtra("idApartamento", listaApartamentos.get(i).getId());
                intent.putExtra("numeroApartamento", Integer.toString(listaApartamentos.get(i).getNumero()));
                intent.putExtra("blocoApartamento", listaApartamentos.get(i).getBloco());

                switch (tipoCadastro) {
                    case "Cadastro de Usuário":
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "Morador":
                        intent.setClass(ListaApartamentoActivity.this, ListaMoradorActivity.class);
                        break;
                    case "Carro":
                        intent.setClass(ListaApartamentoActivity.this, ListaCarroActivity.class);
                        break;
                    case "Pessoa de Livre Acesso":
                        intent.setClass(ListaApartamentoActivity.this, ListaPessoaLivreAcessoActivity.class);
                        break;
                    case "Autorização Vaga de Garagem":
                        intent.setClass(ListaApartamentoActivity.this, ListaAutorizacaoVagaGaragemActivity.class);
                        break;
                    case "Correspondência":
                        intent.setClass(ListaApartamentoActivity.this, ListaCorrespondeciaActivity.class);
                        break;
                    case "Cadastro de Reserva":
                        intent.setClass(ListaApartamentoActivity.this, CadastroReservaActivity.class);
                        break;
                    default:
                        intent = new Intent(ListaApartamentoActivity.this, MainActivity.class);
                }

                if(!tipoCadastro.equals("Cadastro de Usuário")) {
                    startActivity(intent);
                }


            }
        });

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.tv_lista_apartamento);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/

}
