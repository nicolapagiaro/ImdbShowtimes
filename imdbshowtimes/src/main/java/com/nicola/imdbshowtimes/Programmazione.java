package com.nicola.imdbshowtimes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe che gestisce la programmazione di una data di un film
 * specifico
 * Created by Nicola on 19/03/2018.
 */
public class Programmazione implements Serializable {
    private String data;
    private String[] orari;

    /**
     * Costruttore vuoto
     */
    public Programmazione() {}

    /**
     * Costruttore parametrico
     * @param orari orari disponiibili
     */
    public Programmazione(String data, String[] orari) {
        this.data = data;
        this.orari = orari;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String[] getOrari() {
        return orari;
    }

    public void setOrari(String[] orari) {
        this.orari = orari;
    }

    @Override
    public String toString() {
        return "Programmazione{" +
                "data='" + data + '\'' +
                ", orari=" + Arrays.toString(orari) +
                '}';
    }
}
