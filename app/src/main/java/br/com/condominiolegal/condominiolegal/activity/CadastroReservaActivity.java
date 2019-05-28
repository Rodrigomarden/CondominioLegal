package br.com.condominiolegal.condominiolegal.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Espaco;
import br.com.condominiolegal.condominiolegal.model.Morador;
import br.com.condominiolegal.condominiolegal.model.Reserva;

public class CadastroReservaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private TextView valor;
    private EditText dataReserva;
    private Spinner espaco;
    private Spinner horario;

    private ArrayList<Espaco> listaEspacos = new ArrayList<>();
    private int posicaoSpinner = 0;

    private ArrayList<String> listaHorarios = new ArrayList<>();
    private ArrayList<String> listaHorariosDisponíveis;
    private ArrayList<String> listaHorariosReservados;
    private ArrayList<Reserva> listaReservas = new ArrayList<>();
    private ArrayAdapter adapterHorarios;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private String idCondominio;

    private Reserva reserva;

    private DatabaseReference firebase;
    private Query query;
    private ValueEventListener valueEventListenerHorarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_reserva);

        //Recuperar os dados pela intent
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            idApartamento = extra.getString("idApartamento");
            numeroApartamento = extra.getString("numeroApartamento");
            blocoApartamento = extra.getString("blocoApartamento");
        }

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_reserva);
        toolbar.setTitle("Cadastro de Reserva");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Preencher horarios
        listaHorarios.add("08:00 - 08:59");
        listaHorarios.add("09:00 - 09:59");
        listaHorarios.add("10:00 - 10:59");
        listaHorarios.add("11:00 - 11:59");
        listaHorarios.add("12:00 - 12:59");
        listaHorarios.add("13:00 - 13:59");
        listaHorarios.add("14:00 - 14:59");
        listaHorarios.add("15:00 - 15:59");
        listaHorarios.add("16:00 - 16:59");
        listaHorarios.add("17:00 - 17:59");
        listaHorarios.add("18:00 - 18:59");
        listaHorarios.add("19:00 - 19:59");
        listaHorarios.add("20:00 - 20:59");
        listaHorarios.add("21:00 - 21:59");
        listaHorarios.add("22:00 - 22:59");
        listaHorarios.add("23:00 - 23:59");

        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_reserva_blocoNumeroApartamento);
        dataReserva = (EditText) findViewById(R.id.edit_cadastro_reserva_data);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_reserva_salvar);
        valor = (TextView) findViewById(R.id.tv_cadastro_reserva_valor);
        espaco = (Spinner) findViewById(R.id.spinner_cadastro_reserva_espaco);
        horario = (Spinner) findViewById(R.id.spinner_cadastro_reserva_horario);

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText("Bloco: " + blocoApartamento + " Apto: " + numeroApartamento);

        listaEspacos = new ArrayList<>();

        //Configurando Spinner Espaco
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEspacos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        espaco.setAdapter(adapter);

        listaHorariosDisponíveis = new ArrayList<>();

        //Configurando Spinner Espaco
        adapterHorarios = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaHorariosDisponíveis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horario.setAdapter(adapterHorarios);
        horario.setVisibility(View.INVISIBLE);

        //Recuperar espacos do firebase
        Preferencia preferencia = new Preferencia(CadastroReservaActivity.this);
        idCondominio = preferencia.getIdCondominio();
        Query queryBuscarEspaco = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("espacos").orderByChild("nome");

        queryBuscarEspaco.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaEspacos.clear();

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


        //Método para capturar o item selecionado do Spinner espaco
        espaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                posicaoSpinner = i;
                valor.setText("Valor: R$ " + listaEspacos.get(posicaoSpinner).getValor().toString());
                filtrarEspaco();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Cria o calendário e mostra a data selecionada
        dataReserva.setKeyListener(null);
        dataReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleReserva(view);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dataReserva.getText().toString().isEmpty() && !horario.getSelectedItem().toString().equals("Não há horários disponíveis para este dia")) {
                    reserva = new Reserva();
                    reserva.setData(dataReserva.getText().toString());
                    reserva.setValor(valor.getText().toString().replace("Valor: R$ ", ""));
                    reserva.setHora(horario.getSelectedItem().toString());
                    reserva.setNomeEspaco(espaco.getSelectedItem().toString());
                    reserva.setIdApartamento(idApartamento);
                    reserva.setBlocoApartamento(blocoApartamento);
                    reserva.setNumeroApartamento(numeroApartamento);

                    Boolean retornoCadastro = cadastrarReserva();
                    if (retornoCadastro) {
                        Toast.makeText(CadastroReservaActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CadastroReservaActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CadastroReservaActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean cadastrarReserva() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroReservaActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            reserva.setIdUsuario(idUsuario);
            reserva.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("reservas");
            firebase.push()
                    .setValue(reserva);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
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
        buscarHorariosReservados(dataReserva.getText().toString());

    }

    public void buscarHorariosReservados(String data) {

        //Recuperar Items para listar
        query = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("reservas").orderByChild("data").equalTo(data);

        valueEventListenerHorarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaReservas.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Reserva reserva = dados.getValue(Reserva.class);
                    reserva.setId(dados.getKey());
                    listaReservas.add(reserva);
                }

                filtrarEspaco();
                horario.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListenerHorarios);

    }

    //Filtrar Espaco
    public void filtrarEspaco() {
        listaHorariosReservados = new ArrayList<>();
        if(!listaReservas.isEmpty()) {
            for (Reserva reserva : listaReservas) {
                if (reserva.getNomeEspaco().equals(listaEspacos.get(posicaoSpinner).getNome())) {
                    listaHorariosReservados.add(reserva.getHora());
                }
            }
        }
        filtrarHorario();
    }

    //Filtrar Horario
    public void filtrarHorario() {
        listaHorariosDisponíveis.clear();
        if(!listaHorariosReservados.isEmpty()) {
            for (int i = 0; i < listaHorarios.size(); i++) {
                for (int j = 0; j < listaHorariosReservados.size(); j++) {
                    if (!listaHorarios.get(i).equals(listaHorariosReservados.get(j))) {
                        listaHorariosDisponíveis.add(listaHorarios.get(i));
                    }
                }
            }
        } else {
            for (int i = 0; i < listaHorarios.size(); i++) {
                listaHorariosDisponíveis.add(listaHorarios.get(i));
            }
        }

        if(listaHorariosDisponíveis.isEmpty()) {
            listaHorariosDisponíveis.add("Não há horários disponíveis para este dia");
        }

        adapterHorarios.notifyDataSetChanged();
    }


}
