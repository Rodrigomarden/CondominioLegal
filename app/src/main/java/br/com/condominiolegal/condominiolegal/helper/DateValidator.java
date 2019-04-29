package br.com.condominiolegal.condominiolegal.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rodri on 16/04/2019.
 */
public class DateValidator {

    public static boolean validacaoData(String dataTexto) {
        Date data = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.setLenient(false);
            data = format.parse(dataTexto);
            return true;
        } catch(ParseException e) {
            return false;
        }
    }

    public static boolean validacaoDataHoje(String dataTexto) {
        Date data = null;
        Date dataAtual = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.setLenient(false);
            data = format.parse(dataTexto);
            if(data.before(dataAtual)) {
                return false;
            }
            return true;
        } catch(ParseException e) {
            return false;
        }
    }

    public static String obterDataAtual() {
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(calendar.getTime());

    }

}
