package com.nicola.imdbshowtimes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Collezione di metodi utili per formattare le date e gli orari
 * Created by Nicola on 20/03/2018.
 */
class DateTimeUtils {

    /**
     * Funzione che trasforma gli orari scritti in 12H in 24H
     * @param orari array di orari degli spettacoli
     * @return un array con gli orari in formato 24H
     */
    static String[] formatDatetime(String[] orari) {
        String[] t = new String[orari.length];

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.ITALY);
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ITALY);
        for(int i=0; i<orari.length; i++) {
            String ora = "";
            try {
                ora = orari[i].trim();
                if(!ora.contains("pm")) {
                    ora += " pm";
                }
                Date date = parseFormat.parse(ora);
                t[i] = displayFormat.format(date);
            } catch (ParseException e) {
                t[i] = ora;
            }
        }

        return t;
    }


    static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        return simpleDateFormat.format(Calendar.getInstance(Locale.ITALY).getTime());
    }
}
