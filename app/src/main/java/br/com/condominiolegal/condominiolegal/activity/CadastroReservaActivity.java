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
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Espaco;
import br.com.condominiolegal.condominiolegal.model.Reserva;

public class CadastroReservaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private TextView valor;
    private EditText dataReserva;
    private EditText horarioReserva;
    private Spinner espaco;

    private String[] nomesEspaco = new String[]{""};
    private ArrayList<Espaco> listaEspacos = new ArrayList<>();;
    private int posicaoSpinner;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    ArrayList<String> horarios;

    private Reserva reserva;

    private DatabaseReference firebase;
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

        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_reserva_blocoNumeroApartamento);
        dataReserva = (EditText) findViewById(R.id.edit_cadastro_reserva_data);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_reserva_salvar);
        valor = (TextView) findViewById(R.id.tv_cadastro_reserva_valor);
        espaco = (Spinner) findViewById(R.id.spinner_cadastro_reserva_espaco);

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText("Bloco: " + blocoApartamento + " Apto: " + numeroApartamento);

        listaEspacos = new ArrayList<>();
        //listaEspacos.add(new Espaco("-LeDvgyRyD3KPux7AEDi", "QUADRA FUTEBOL", Float.parseFloat("50")));
        //listaEspacos.add(new Espaco("-LeDx7aLtEngRswSk1Ow", "SALAO DE FESTAS", Float.parseFloat("30")));

        //Configurando Spinner
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEspacos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        espaco.setAdapter(adapter);

        //Recuperar espacos do firebase
        Preferencia preferencia = new Preferencia(CadastroReservaActivity.this);
        String idCondominio = preferencia.getIdCondominio();
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
                    nomesEspaco[i] = espaco.getNome();
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
                valor.setText("Valor: " + listaEspacos.get(posicaoSpinner).getValor().toString());
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

        horarioReserva = new EditText(this);
        horarioReserva.setKeyListener(null);
        horarioReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void selecionarHorario() {

//        horarios = new ArrayList<>();
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setTitle("Selecione uma Cor");
//        builder.setItems(horarios, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int selecionado) {
//                Toast.makeText(CadastroReservaActivity.this, "Cor Selecionada: " + horarios.get(selecionado),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.create().show();
    }


}
