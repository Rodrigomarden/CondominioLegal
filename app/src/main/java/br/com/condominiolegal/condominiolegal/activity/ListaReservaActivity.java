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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.com.condominiolegal.condominiolegal.R;

import br.com.condominiolegal.condominiolegal.adapter.ListaReservaAdapter;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Espaco;
import br.com.condominiolegal.condominiolegal.model.Reserva;

public class ListaReservaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    private Toolbar toolbar;
    private AlertDialog.Builder dialogDelete;

    private ListView listView;
    private ArrayAdapter adapter;
    private EditText dataReserva;
    private Spinner espaco;
    private ArrayList<Reserva> listaReservas;
    private ArrayList<Reserva> listaReservasFiltrada;
    private TextView inf;

    private ArrayList<Espaco> listaEspacos = new ArrayList<>();
    private int posicaoSpinner;

    private ValueEventListener valueEventListenerMensagem;

    private DatabaseReference firebase;
    private Query query;

    private String tipoUsuario;
    private String idCondominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reserva);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_lista_reserva);
        toolbar.setTitle("Reserva");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        dataReserva = (EditText) findViewById(R.id.edit_lista_reserva_data);
        espaco = (Spinner) findViewById(R.id.spinner_lista_reserva_espaco);
        inf = (TextView) findViewById(R.id.tv_lista_reserva_inf);

        //Esconde Spinner
        espaco.setVisibility(View.INVISIBLE);

        //Recupera dados do usuário
        Preferencia preferencia = new Preferencia(ListaReservaActivity.this);
        idCondominio = preferencia.getIdCondominio();
        tipoUsuario = preferencia.getPerfil();

        //Cria o calendário e mostra a data selecionada
        dataReserva.setKeyListener(null);
        dataReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleReserva(view);
            }
        });

        //CONFIGURANDO SPINNER
        listaEspacos = new ArrayList<>();
        final ArrayAdapter adapterSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEspacos);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        espaco.setAdapter(adapterSpinner);

        //Recuperar espacos do firebase
        Query queryBuscarEspaco = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("espacos").orderByChild("nome");

        queryBuscarEspaco.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaEspacos.clear();
                int i = 0;

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Espaco espaco = dados.getValue(Espaco.class);
                    espaco.setId(dados.getKey());
                    listaEspacos.add(espaco);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Método para capturar o item selecionado do Spinner
        espaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                posicaoSpinner = i;
                filtrarEspaco();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Configurando Adapter Reservas
        listView = (ListView) findViewById(R.id.lv_lista_reserva);
        adapter = new ListaReservaAdapter(this, listaReservasFiltrada);
        listView.setAdapter(adapter);



        //Editar Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListaReservaActivity.this, EditarReservaActivity.class);
                intent.putExtra("reserva", listaReservas.get(i));
                startActivity(intent);
            }
        });

        //Deletar items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                //Criar alert dialog
                dialogDelete = new AlertDialog.Builder(ListaReservaActivity.this);

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
                        firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("reservas").child(listaReservas.get(position).getId());
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
        Intent intent = new Intent(ListaReservaActivity.this, CadastroReservaActivity.class);
        startActivity(intent);
    }

    //Escolher data e horário
    private int year, month, day;

    public void scheduleReserva(View view) {
        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar cMin = Calendar.getInstance();
        datePickerDialog.setMinDate(cMin);
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

    }

    private void initDateTimeData() {
        if(year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {
        year = month = day = 0;

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        Calendar myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dataReserva.setText(sdf.format(myCalendar.getTime()));

        buscarReservas(dataReserva.getText().toString());

    }

    //Carregar Reservas
    public void buscarReservas(String data) {

        listaReservas = new ArrayList<>();

        //Recuperar Items para listar
        query = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("reservas").orderByChild("data").equalTo(data);

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaReservas.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Reserva reserva = dados.getValue(Reserva.class);
                    reserva.setId(dados.getKey());
                    listaReservas.add(reserva);
                }

                if(!listaReservas.isEmpty()) {
                    inf.setText("");
                } else {
                    inf.setText("Não há itens cadastrados.");
                }

                filtrarEspaco();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListenerMensagem);

    }

    //Filtrar Espaco
    public void filtrarEspaco() {
        listaReservasFiltrada = new ArrayList<>();
        for(Reserva reserva : listaReservas) {
            if(reserva.getIdEspaco().equals(listaEspacos.get(posicaoSpinner).getId())) {
                listaReservasFiltrada.add(reserva);
            }
        }
    }

}
