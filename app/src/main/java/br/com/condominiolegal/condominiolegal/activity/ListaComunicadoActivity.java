package br.com.condominiolegal.condominiolegal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;

import br.com.condominiolegal.condominiolegal.adapter.ListaComunicadoAdapter;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Comunicado;

public class ListaComunicadoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AlertDialog.Builder dialogDelete;

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Comunicado> listaComunicados;
    private TextView inf;

    private ValueEventListener valueEventListenerMensagem;

    private DatabaseReference firebase;
    private Query query;

    private String tipoUsuario;
    private String idCondominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comunicado);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_lista_comunicado);
        toolbar.setTitle("Comunicado");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        inf = (TextView) findViewById(R.id.tv_lista_comunicado_inf);

        //Recupera dados do usuário
        Preferencia preferencia = new Preferencia(ListaComunicadoActivity.this);
        idCondominio = preferencia.getIdCondominio();
        tipoUsuario = preferencia.getPerfil();

        listaComunicados = new ArrayList<>();

        listView = (ListView) findViewById(R.id.lv_lista_comunicado);
        adapter = new ListaComunicadoAdapter(this, listaComunicados);

        listView.setAdapter(adapter);

        //Recuperar Items para listar
        query = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados").orderByChild("dataInsercao");

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaComunicados.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Comunicado comunicado = dados.getValue(Comunicado.class);
                    comunicado.setId(dados.getKey());
                    listaComunicados.add(comunicado);
                }

                if(!listaComunicados.isEmpty()) {
                    inf.setText("");
                } else {
                    inf.setText("Não há itens cadastrados.");
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListenerMensagem);

        //Editar Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListaComunicadoActivity.this, EditarComunicadoActivity.class);
                intent.putExtra("comunicado", listaComunicados.get(i));
                startActivity(intent);
            }
        });

        //Deletar items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                //Criar alert dialog
                dialogDelete = new AlertDialog.Builder(ListaComunicadoActivity.this);

                //Configurar o titulo
                //dialog.setTitle("Deletar Arquivo");

                //Configurar Mensagem
                dialogDelete.setMessage("Deseja realmente deletar este item?");

                //Botao negativo
                dialogDelete.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //Botao positivo
                dialogDelete.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados").child(listaComunicados.get(position).getId());
                        firebase.removeValue();
                    }
                });

                dialogDelete.create();
                dialogDelete.show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        query.addValueEventListener(valueEventListenerMensagem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        query.removeEventListener(valueEventListenerMensagem);
    }


    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(valueEventListenerMensagem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(tipoUsuario.equals("Síndico")) {
            getMenuInflater().inflate(R.menu.menu_cadastro_novo, menu);
        }
        return true;
    }

    //Capta os itens selecionados no Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_novo_cadastro:
                abrirCadastro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastro() {
        Intent intent = new Intent(ListaComunicadoActivity.this, CadastroComunicadoActivity.class);
        startActivity(intent);
    }
}
