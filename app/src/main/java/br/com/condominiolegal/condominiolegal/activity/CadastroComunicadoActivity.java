package br.com.condominiolegal.condominiolegal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.condominiolegal.condominiolegal.R;
import br.com.condominiolegal.condominiolegal.config.ConfiguracaoFirebase;
import br.com.condominiolegal.condominiolegal.helper.DateValidator;
import br.com.condominiolegal.condominiolegal.helper.Mask;
import br.com.condominiolegal.condominiolegal.helper.Preferencia;
import br.com.condominiolegal.condominiolegal.model.Anexo;
import br.com.condominiolegal.condominiolegal.model.Comunicado;

public class CadastroComunicadoActivity extends AppCompatActivity {

    public Toolbar toolbar;

    private ListView listaArquivos;
    private Button botaoSalvar;
    private Button escolherArquivo;
    private EditText titulo;
    private EditText descricao;
    private TextInputLayout tilDescricao;
    private EditText dataFim;
    private AlertDialog.Builder dialog;

    private Comunicado comunicado;
    private ArrayList<Uri> listaLocalArquivoSelecionado = new ArrayList<>();
    private List<String> listaNomeArquivos = new ArrayList<>();
    private ArrayList<Anexo> anexos = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private DatabaseReference firebase;

    private Boolean retornoCadastro = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_comunicado);

        //Configurando toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_cadastro_comunicado);
        toolbar.setTitle("Cadastro de Comunicado");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Recuperar IDs da tela
        listaArquivos = (ListView) findViewById(R.id.lv_comunicado_arquivo);
        titulo = (EditText) findViewById(R.id.edit_cadastro_comunicado_titulo);
        tilDescricao = (TextInputLayout) findViewById(R.id.til_cadastro_comunicado_descricao);
        descricao = (EditText) findViewById(R.id.edit_cadastro_comunicado_descricao);
        dataFim = (EditText) findViewById(R.id.edit_cadastro_comunicado_dataFim);
        botaoSalvar = (Button) findViewById(R.id.bt_cadastro_comunicado_salvar);

        //Criar botão escolher arquivo
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout_linear_cadastro_comunicado);
        escolherArquivo = new Button(CadastroComunicadoActivity.this);
        escolherArquivo.setText("ESCOLHER ARQUIVO");
        escolherArquivo.setLayoutParams(botaoSalvar.getLayoutParams());
        layout.addView(escolherArquivo, 3);

        //Definindo máximo de caracters para 8000 na descrição
        tilDescricao.setCounterEnabled(true);
        tilDescricao.setCounterMaxLength(8000);

        //Mascaras
        dataFim.addTextChangedListener(Mask.maskData(dataFim));

        //Listar Arquivos
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.adapter_lista_arquivo, R.id.tv_lista_arquivo, listaNomeArquivos);

        listaArquivos.setAdapter(adapter);

        listaArquivos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //Criar alert dialog
                dialog = new AlertDialog.Builder(CadastroComunicadoActivity.this);

                //Configurar o titulo
                dialog.setTitle("Deletar Arquivo");

                //Configurar Mensagem
                dialog.setMessage("Deseja realmente deletar este arquivo?");

                //Botao negativo
                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //Botao positivo
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listaLocalArquivoSelecionado.remove(position);
                        listaNomeArquivos.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                dialog.create();
                dialog.show();
                return true;
            }
        });

        escolherArquivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolherArquivo();
            }
        });

        //Botão Salvar
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!titulo.getText().toString().isEmpty() && !descricao.getText().toString().isEmpty() && !dataFim.getText().toString().isEmpty()) {
                    comunicado = new Comunicado();
                    comunicado.setTitulo(titulo.getText().toString());
                    comunicado.setDescricao(descricao.getText().toString());
                    comunicado.setDataFim(dataFim.getText().toString());

                    //Verificação de datas
                    if(!DateValidator.validacaoData(comunicado.getDataFim())) {
                        Toast.makeText(CadastroComunicadoActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
                    } else {

                        Boolean retornoCadastro = cadastrarComunicado();

                        if (retornoCadastro) {
                            Toast.makeText(CadastroComunicadoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CadastroComunicadoActivity.this, "Problema ao realizar cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(CadastroComunicadoActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void escolherArquivo() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Testar processo de retorno dos dados
        if(requestCode==1 && resultCode == RESULT_OK && data != null) {

            //Verificar tamanho do arquivo
            File f = new File(data.getDataString());
            Log.v("Arquivo", String.valueOf(f.length()));
            Log.v("Arquivo", f.getName());

            //Recupera local do recurso
            listaLocalArquivoSelecionado.add(data.getData());
            listaNomeArquivos.add(data.getData().getLastPathSegment());
            adapter.notifyDataSetChanged();

        }
    }


    private boolean cadastrarComunicado() {
        try{
            retornoCadastro = true;

            //Recupera o ID do condominio
            Preferencia preferencia = new Preferencia(CadastroComunicadoActivity.this);
            final String idCondominio = preferencia.getIdCondominio();
            String idUsuario = preferencia.getId();
            comunicado.setIdUsuario(idUsuario);
            comunicado.setDataInsercao(DateValidator.obterDataAtual());

            firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados");
            firebase.push()
                    .setValue(comunicado, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            final String keyComunicado = databaseReference.getKey();

                            //Fazer Upload dos arquivos
                            for(final Uri uri : listaLocalArquivoSelecionado) {
                                //Salvar
                                StorageReference arquivoRef = ConfiguracaoFirebase.getFirebaseStorage().child(idCondominio+"/comunicado/"+uri.getLastPathSegment());
                                UploadTask uploadTask = arquivoRef.putFile(uri);
                                arquivoRef.getDownloadUrl();

                                // Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados").child(keyComunicado);
                                        firebase.removeValue();
                                        retornoCadastro = false;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Anexo anexo = new Anexo();
                                        anexo.setNome(uri.getLastPathSegment());
                                        anexo.setUrl(taskSnapshot.getDownloadUrl().toString());
                                        anexos.add(anexo);
                                        firebase = ConfiguracaoFirebase.getFirebase().child("condominios").child(idCondominio).child("comunicados").child(keyComunicado).child("anexos");
                                        firebase.push()
                                                .setValue(anexo);
                                    }
                                });
                            }

                        }
                    });

            finish();
            return retornoCadastro;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
