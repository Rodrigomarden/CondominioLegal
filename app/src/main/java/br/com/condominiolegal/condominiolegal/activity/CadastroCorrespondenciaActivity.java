package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Correspondencia;

public class CadastroCorrespondenciaActivity extends AppCompatActivity {
    
    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private RadioGroup radioGroup;
    private RadioButton radioButtonEscolhido;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private Correspondencia correspondencia;

    private DatabaseReference firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_correspondencia);

        //Recuperar os dados pela intent
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            idApartamento = extra.getString("idApartamento");
            numeroApartamento = extra.getString("numeroApartamento");
            blocoApartamento = extra.getString("blocoApartamento");
        }

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_correspondencia);
        toolbar.setTitle("Cadastro de Correspondência");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_correspondencia_blocoNumeroApartamento);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupId_cadastro_correspondencia_tipo);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_correspondencia_salvar);

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText("Bloco: " + blocoApartamento + " Apto: " + numeroApartamento);

        //Capturando RadioButton
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButtonEscolhido = (RadioButton) findViewById(i);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correspondencia = new Correspondencia();
                correspondencia.setData(DateValidator.obterDataAtual());
                correspondencia.setTipo(radioButtonEscolhido.getText().toString());
                correspondencia.setStatus("Iniciada");

                Boolean retornoCadastro = cadastrarCorrespondencia();

                if (retornoCadastro) {
                    Toast.makeText(CadastroCorrespondenciaActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CadastroCorrespondenciaActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean cadastrarCorrespondencia() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroCorrespondenciaActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            correspondencia.setIdUsuario(idUsuario);
            correspondencia.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").child(idApartamento).child("correspondencias");
            firebase.push()
                    .setValue(correspondencia);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
