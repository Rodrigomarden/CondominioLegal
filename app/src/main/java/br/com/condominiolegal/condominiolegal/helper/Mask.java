package br.com.condominiolegal.condominiolegal.helper;

import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

/**
 * Created by rodri on 08/04/2019.
 */
public class Mask {

    public static MaskTextWatcher maskTelefone(EditText telefone) {
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);

        return maskTelefone;
    }

    public static MaskTextWatcher maskData(EditText data) {
        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskData = new MaskTextWatcher(data, simpleMaskData);

        return maskData;
    }

    public static MaskTextWatcher maskCpf(EditText cpf) {
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskCpf = new MaskTextWatcher(cpf, simpleMaskCpf);

        return maskCpf;
    }

    public static MaskTextWatcher maskPlaca(EditText placa) {
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("LLL-NNNN");
        MaskTextWatcher maskPlaca = new MaskTextWatcher(placa, simpleMaskCpf);

        return maskPlaca;
    }


}
