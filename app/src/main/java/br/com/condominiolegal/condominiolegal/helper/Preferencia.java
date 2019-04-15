package br.com.condominiolegal.condominiolegal.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rodri on 02/04/2019.
 */
public class Preferencia {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "Condominio.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
    private final String CHAVE_PERFIL = "perfilUsuarioLogado";
    private final String CHAVE_ID_CONDOMINIO = "idCondominioUsuarioLogado";
    private final String CHAVE_NOME_CONDOMINIO = "nomeCondominioUsuarioLogado";
    private final String CHAVE_ID_APARTAMENTO = "idApartamentoUsuarioLogado";
    private final String CHAVE_NUMERO_BLOCO_APARTAMENTO = "numeroBlocoApartamentoUsuarioLogado";

    public Preferencia(Context contextoParametro) {
        contexto=contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuario, String nomeUsuario, String perfilUsuario, String idCondominio, String nomeCondominio) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.putString(CHAVE_PERFIL, perfilUsuario);
        editor.putString(CHAVE_ID_CONDOMINIO, idCondominio);
        editor.putString(CHAVE_NOME_CONDOMINIO, nomeCondominio);
        editor.commit();

    }

    public void salvarDadosMorador(String identificadorUsuario, String nomeUsuario, String perfilUsuario, String idCondominio, String nomeCondominio, String idApartamento, String numeroBlocoApartamento) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.putString(CHAVE_PERFIL, perfilUsuario);
        editor.putString(CHAVE_ID_CONDOMINIO, idCondominio);
        editor.putString(CHAVE_NOME_CONDOMINIO, nomeCondominio);
        editor.putString(CHAVE_ID_APARTAMENTO, idApartamento);
        editor.putString(CHAVE_NUMERO_BLOCO_APARTAMENTO, numeroBlocoApartamento);
        editor.commit();

    }

    public String getId() {
        String dados = "";
        dados = preferences.getString(CHAVE_IDENTIFICADOR, null);

        return dados;
    }

    public String getNome() {
        String dados = "";
        dados = preferences.getString(CHAVE_NOME, null);

        return dados;
    }

    public String getPerfil() {
        String dados = "";
        dados = preferences.getString(CHAVE_PERFIL, null);

        return dados;
    }

    public String getIdCondominio() {
        String dados = "";
        dados = preferences.getString(CHAVE_ID_CONDOMINIO, null);

        return dados;
    }

    public String getNomeCondominio() {
        String dados = "";
        dados = preferences.getString(CHAVE_NOME_CONDOMINIO, null);

        return dados;
    }

    public String getIdApartamento() {
        String dados = "";
        dados = preferences.getString(CHAVE_ID_APARTAMENTO, null);

        return dados;
    }

    public String getNumeroBlocoApartamento() {
        String dados = "";
        dados = preferences.getString(CHAVE_NUMERO_BLOCO_APARTAMENTO, null);

        return dados;
    }

}
