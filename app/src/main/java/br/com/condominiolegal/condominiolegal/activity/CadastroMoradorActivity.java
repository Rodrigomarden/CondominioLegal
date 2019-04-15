package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Morador;

public class CadastroMoradorActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private EditText nome;
    private EditText cpf;
    private EditText dataNascimento;
    private EditText telefone;
    private Spinner sexo;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private Morador morador;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_morador);

        //Recuperar os dados do Contato para conversa pela intent
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            idApartamento = extra.getString("idApartamento");
            numeroApartamento = extra.getString("numeroApartamento");
            blocoApartamento = extra.getString("blocoApartamento");
        }

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_morador);
        toolbar.setTitle("Cadastro de Morador");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_morador_blocoNumeroApartamento);
        nome = (EditText) findViewById(R.id.edit_cadastro_morador_nome);
        cpf = (EditText) findViewById(R.id.edit_cadastro_morador_cpf);
        dataNascimento = (EditText) findViewById(R.id.edit_cadastro_morador_data_nascimento);
        telefone = (EditText) findViewById(R.id.edit_cadastro_morador_telefone);
        sexo = (Spinner) findViewById(R.id.spinner_cadastro_morador_sexo);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_morador_salvar);

        //Mascaras
        telefone.addTextChangedListener(Mask.maskTelefone(telefone));
        dataNascimento.addTextChangedListener(Mask.maskData(dataNascimento));
        cpf.addTextChangedListener(Mask.maskCpf(cpf));

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText("Bloco: " + blocoApartamento + " Apto: " + numeroApartamento);

        //Configurando Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nome.getText().toString().isEmpty() && !cpf.getText().toString().isEmpty() && !dataNascimento.getText().toString().isEmpty() && !telefone.getText().toString().isEmpty()) {
                    morador = new Morador();
                    morador.setNome(nome.getText().toString());
                    morador.setCpf(cpf.getText().toString());
                    morador.setDataNascimento(dataNascimento.getText().toString());
                    morador.setSexo(sexo.getSelectedItem().toString());
                    morador.setTelefone(telefone.getText().toString());

                    Boolean retornoCadastro = cadastrarMorador();
                    if(retornoCadastro) {
                        Toast.makeText(CadastroMoradorActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CadastroMoradorActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroMoradorActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean cadastrarMorador() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroMoradorActivity.this);
            String idCondominio = preferencia.getIdCondominio();

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").child(idApartamento).child("moradores");
            firebase.push()
                    .setValue(morador);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
