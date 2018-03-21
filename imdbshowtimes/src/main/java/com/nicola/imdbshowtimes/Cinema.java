package com.nicola.imdbshowtimes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe per gestire i cinema e le sue robe
 * Created by Nicola on 19/03/2018.
 */
public class Cinema implements Serializable {
    private int distanceInKm;
    private String link;
    private String nome;
    private String indirizzo;
    private String telefono;
    private ArrayList<Film> films;

    /**
     * Costruttore vuoto
     */
    public Cinema() {
        films = new ArrayList<>();
    }

    /**
     * Costruttore parametrico
     * @param link link alla pagina del cinema
     * @param nome nome del cinema
     * @param indirizzo indirizzo del cinema
     * @param telefono numero di telefono del cinema
     */
    public Cinema(String link, String nome, String indirizzo, String telefono) {
        this.link = link;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        films = new ArrayList<>();
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public void addFilm(Film f) {
        films.add(f);
    }

    public Film getFilm(int position) {
        return films.get(position);
    }

    public ArrayList<Film> getFilms() {
        return films;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "link='" + link + '\'' +
                ", nome='" + nome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", films=" + films +
                '}';
    }
}
