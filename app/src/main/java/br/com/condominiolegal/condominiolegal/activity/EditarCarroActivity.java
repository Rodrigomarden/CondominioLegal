package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.LerApartamento;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Carro;
import br.com.condominiolegal.condominiolegal.model.Morador;

/**
 * Created by rodri on 16/05/2019.
 */
public class EditarCarroActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private Button botaoSalvar;
    private TextView blocoNumeroApartamento;
    private EditText placa;
    private EditText modelo;
    private EditText marca;
    private EditText nomeDono;
    private EditText cpf;

    private String idApartamento;
    private String numeroApartamento;
    private String blocoApartamento;

    private Carro carro;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_carro);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_carro);
        toolbar.setTitle("Editar Carro");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        blocoNumeroApartamento = (TextView) findViewById(R.id.tv_cadastro_carro_blocoNumeroApartamento);
        placa = (EditText) findViewById(R.id.edit_cadastro_carro_placa);
        modelo = (EditText) findViewById(R.id.edit_cadastro_carro_modelo);
        marca = (EditText) findViewById(R.id.edit_cadastro_carro_marca);
        nomeDono = (EditText) findViewById(R.id.edit_cadastro_carro_nomeDono);
        cpf = (EditText) findViewById(R.id.edit_cadastro_carro_cpf);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_carro_salvar);

        //Mascaras
        cpf.addTextChangedListener(Mask.maskCpf(cpf));
        placa.addTextChangedListener(Mask.maskPlaca(placa));

        //Recupera dados para editar
        if(getIntent().getSerializableExtra("carro") != null) {
            carro = (Carro) getIntent().getSerializableExtra("carro");
            placa.setText(carro.getPlaca());
            modelo.setText(carro.getModelo());
            marca.setText(carro.getMarca());
            nomeDono.setText(carro.getNomeDono());
            cpf.setText(carro.getCpf());

            //Recuperar os dados do Apartamento para conversa pela intent
            Bundle extra = getIntent().getExtras();
            if(extra != null) {
                idApartamento = extra.getString("idApartamento");
                numeroApartamento = extra.getString("numeroApartamento");
                blocoApartamento = extra.getString("blocoApartamento");
            }
        }

        //Seta o número e o bloco do apartamento na tela
        blocoNumeroApartamento.setText(LerApartamento.exibicaoApartamento(blocoApartamento, numeroApartamento));

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!placa.getText().toString().isEmpty() && !modelo.getText().toString().isEmpty() && !marca.getText().toString().isEmpty() && !nomeDono.getText().toString().isEmpty() && !cpf.getText().toString().isEmpty()) {
                    carro.setPlaca(placa.getText().toString().toUpperCase());
                    carro.setModelo(modelo.getText().toString());
                    carro.setMarca(marca.getText().toString());
                    carro.setNomeDono(nomeDono.getText().toString());
                    carro.setCpf(cpf.getText().toString());

                    Boolean retornoEditar = editarCarro();
                    if(retornoEditar) {
                        Toast.makeText(EditarCarroActivity.this, "Editar realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarCarroActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarCarroActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean editarCarro() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(EditarCarroActivity.this);
            String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            carro.setIdUsuario(idUsuario);
            carro.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("apartamentos").child(idApartamento).child("carros").child(carro.getId());
            firebase.setValue(carro);

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
