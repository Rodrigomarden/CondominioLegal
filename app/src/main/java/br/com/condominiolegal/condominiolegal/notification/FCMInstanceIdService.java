package br.com.condominiolegal.condominiolegal.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.condominiolegal.condominiolegal.helper.Preferencia;

/**
 * Created by rodri on 22/04/2019.
 */
public class FCMInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "FirebaseLog";

    @Override
    public void onTokenRefresh() {

        //Recuperar token do dispositivo atualizado
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Preferencia preferencia = new Preferencia(FCMInstanceIdService.this);
        preferencia.salvarToken(refreshedToken);

        Log.d(TAG, "Token: " + refreshedToken);

    }
}
