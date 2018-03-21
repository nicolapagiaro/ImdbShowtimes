package com.nicola.imdbshowtimes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe per gestire i film e tutti i loro attributi
 * Created by Nicola on 19/03/2018.
 */
public class Film implements Serializable{
    private String link;
    private String nome;
    private String durata;
    private String descrizione;
    private String[] attori;
    private String[] direttori;
    private String imageUrl;
    private ArrayList<Programmazione> programmazioni;

    /**
     * Costruttore senza niente
     */
    public Film() {
        programmazioni = new ArrayList<>();
    }

    /**
     * Costruttore parametrico
     * @param link link alla pagina del film
     * @param nome nome del film
     * @param durata durata del film (es: 180 min)
     * @param descrizione descrizione del film
     * @param attori lista di attori
     * @param direttori lista di direttori
     * @param imageUrl link all'immagine di copertina
     */
    public Film(String link, String nome, String durata, String descrizione, String[] attori, String[]direttori, String imageUrl) {
        this.link= link;
        this.nome = nome;
        this.durata = durata;
        this.descrizione = descrizione;
        this.attori = attori;
        this.direttori = direttori;
        this.imageUrl = imageUrl;
        programmazioni = new ArrayList<>();
    }

    /**
     * Aggiungi una programmazione
     * @param p oggetto programmazione
     */
    public void addProgrammazione(Programmazione p) {
        programmazioni.add(p);
    }

    /**
     * Preleva una programmazione a seconda della posizione
     * @param position posizione
     * @return l'oggetto programmazione
     */
    public Programmazione getProgrammazione(int position) {
        return programmazioni.get(position);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String[] getAttori() {
        return attori;
    }

    public void setAttori(String[] attori) {
        this.attori = attori;
    }

    public String[] getDirettori() {
        return direttori;
    }

    public void setDirettori(String[] direttori) {
        this.direttori = direttori;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Programmazione> getProgrammazioni() {
        return programmazioni;
    }

    public void setProgrammazioni(ArrayList<Programmazione> programmazioni) {
        this.programmazioni = programmazioni;
    }

    @Override
    public String toString() {
        return "Film{" +
                "link='" + link + '\'' +
                ", nome='" + nome + '\'' +
                ", durata='" + durata + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", attori=" + Arrays.toString(attori) +
                ", direttori=" + Arrays.toString(direttori) +
                ", imageUrl='" + imageUrl + '\'' +
                ", programmazioni=" + programmazioni +
                '}';
    }
}
