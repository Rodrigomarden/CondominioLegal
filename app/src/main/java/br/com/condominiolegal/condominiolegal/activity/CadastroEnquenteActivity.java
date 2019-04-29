package br.com.condominiolegal.condominiolegal.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Enquete;
import br.com.condominiolegal.condominiolegal.model.OpcaoEnquente;
import br.com.condominiolegal.condominiolegal.model.PessoasLivreAcesso;

public class CadastroEnquenteActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private ArrayList<EditText> listaEditText = new ArrayList<>();

    private Button botaoSalvar;
    private ImageButton botaoAddOpcao;
    private ImageButton botaoRemoveOpcao;
    private EditText titulo;
    private EditText dataEncerramento;
    private EditText opcao1;
    private EditText opcao2;
    private Spinner tipo;

    private Enquete enquete;
    private ArrayList<OpcaoEnquente> listaOpcaoEnquentes = new ArrayList<>();

    private DatabaseReference firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enquente);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_enquete);
        toolbar.setTitle("Cadastro de Enquete");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        titulo = (EditText) findViewById(R.id.edit_cadastro_enquente_titulo);
        dataEncerramento = (EditText) findViewById(R.id.edit_cadastro_enquente_data_encerramento);
        opcao1 = (EditText) findViewById(R.id.edit_cadastro_enquente_opcao1);
        opcao2 = (EditText) findViewById(R.id.edit_cadastro_enquente_opcao2);
        tipo = (Spinner) findViewById(R.id.spinner_cadastro_enquente_tipo);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_enquete_salvar);
        botaoAddOpcao = (ImageButton) findViewById(R.id.imageButton_cadastro_enquete_add);
        botaoRemoveOpcao = (ImageButton) findViewById(R.id.imageButton_cadastro_enquete_remove);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout_linear_cadastro_enquete);

        //Mascaras
        dataEncerramento.addTextChangedListener(Mask.maskData(dataEncerramento));

        //Configurando Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipo_enquete_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapter);

        botaoAddOpcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(CadastroEnquenteActivity.this);
                editText.setHint("Opção " + (listaEditText.size()+3));
                editText.setLayoutParams(opcao1.getLayoutParams());
                listaEditText.add(editText);
                layout.addView(editText);
            }
        });

        botaoRemoveOpcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listaEditText.size() > 0) {
                    layout.removeView(listaEditText.get((listaEditText.size()-1)));
                    listaEditText.remove(listaEditText.size() - 1);
                }
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!titulo.getText().toString().isEmpty() && !dataEncerramento.getText().toString().isEmpty() && !opcao1.getText().toString().isEmpty() && !opcao2.getText().toString().isEmpty()) {
                    enquete = new Enquete();
                    enquete.setTitulo(titulo.getText().toString());
                    enquete.setDataEncerramento(dataEncerramento.getText().toString());
                    enquete.setTipo(tipo.getSelectedItem().toString());

                    //Inserir opção 1
                    OpcaoEnquente opcaoEnquente = new OpcaoEnquente();
                    opcaoEnquente.setOpcao(opcao1.getText().toString());
                    listaOpcaoEnquentes.add(opcaoEnquente);

                    //Inserir opção 2
                    opcaoEnquente = new OpcaoEnquente();
                    opcaoEnquente.setOpcao(opcao2.getText().toString());
                    listaOpcaoEnquentes.add(opcaoEnquente);

                    //Inserir opções dinamicas
                    for(int i = 0; i < listaEditText.size(); i++) {
                        if(listaEditText.get(i).getText().toString() != null) {
                            opcaoEnquente = new OpcaoEnquente();
                            opcaoEnquente.setOpcao(listaEditText.get(i).getText().toString());
                            listaOpcaoEnquentes.add(opcaoEnquente);
                        }
                    }

                    //Verificação de datas
                    if(!DateValidator.validacaoData(enquete.getDataEncerramento())) {
                        Toast.makeText(CadastroEnquenteActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
                    } else if(!DateValidator.validacaoDataHoje(enquete.getDataEncerramento())) {
                        Toast.makeText(CadastroEnquenteActivity.this, "A data limite não pode ser anterior a data atual!", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean retornoCadastro = cadastrarEnquete();

                        if (retornoCadastro) {
                            Toast.makeText(CadastroEnquenteActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CadastroEnquenteActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(CadastroEnquenteActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean cadastrarEnquete() {
        try{
            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroEnquenteActivity.this);
            final String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            enquete.setIdUsuario(idUsuario);
            enquete.setDataInsercao(DateValidator.obterDataAtual());

            //Cadastrar Enquetes
            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("enquetes");
            firebase.push()
                    .setValue(enquete, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            final String keyComunicado = databaseReference.getKey();

                            //Cadastrar Opções
                            for(int i = 0; i < listaOpcaoEnquentes.size(); i++) {
                                firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("enquetes").child(keyComunicado).child("opcoes");
                                firebase.push()
                                        .setValue(listaOpcaoEnquentes.get(i));
                            }
                        }
                    });

            finish();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
